package de.dnpm.dip.admin.impl


import scala.concurrent.{
  Future,
  ExecutionContext
}
import cats.Monad
import de.dnpm.dip.util.Logging
import de.dnpm.dip.coding.Coding
import de.dnpm.dip.model.Site
import de.dnpm.dip.service.{
  Connector,
  ConnectionStatus
}
import de.dnpm.dip.connector.{
  FakeConnector,
  HttpConnector,
}
import de.dnpm.dip.connector.HttpMethod.GET
import de.dnpm.dip.admin.api._


class AdminServiceProviderImpl extends AdminServiceProvider
{
  override def getInstance: AdminService =
    AdminServiceImpl.instance
}


object AdminServiceImpl extends Logging
{
  
  private val connector =
    System.getProperty("dnpm.dip.connector.type","broker") match {
      case HttpConnector.Type(typ) =>
        HttpConnector(
          typ,
          {
            case _: StatusRequest => (GET, "/api/peer2peer/status", Map.empty) 
          }
        )

      case _ =>
        import scala.concurrent.ExecutionContext.Implicits._
        log.warn("Falling back to Fake Connector!")
        FakeConnector[Future]
    }


  private[impl] val instance =
    new AdminServiceImpl(connector)

}


class AdminServiceImpl(
  private val connector: Connector[Future,Monad[Future]]
)
extends AdminService
with Logging
{

  private val statusRequest =
    StatusRequest(Site.local)

  import ConnectionStatus.Offline

  private val SELF_UNAVAILABLE =
"""Self-availability check via broker failed.
Your local node could still be correctly reachable by external peers, but cannot check this itself due to connection problems to the broker.
Check out the backend logs for details."""


  private def connectionStatus(
    site: Coding[Site],
    result: Either[String,ConnectionStatus]
  ): ConnectionStatus =
    result.fold(
      msg    => ConnectionStatus(site,Offline,Some(msg)),
      status => status  
    )

  override def connectionReport(
    implicit env: ExecutionContext
  ): Future[ConnectionReport] =
    for {
      self <-
        (connector ! (statusRequest,Site.local))
          .map(connectionStatus(Site.local,_))
          .recover {
            case t =>
              log.warn(s"Self-availability check failed, most likely due to connection problem to the broker itself: ${t.getMessage}")
              ConnectionStatus(Site.local,Offline,Some(SELF_UNAVAILABLE))
          }

      peers <-
        (connector ! statusRequest).map(
           _.foldLeft(List.empty[ConnectionStatus]){
             case (acc,(site,result)) => connectionStatus(site,result) :: acc
           }
           .sortBy(_.site.display.get)  // call of .get safe here
        )
    } yield ConnectionReport(
      self,
      peers
    )

}
