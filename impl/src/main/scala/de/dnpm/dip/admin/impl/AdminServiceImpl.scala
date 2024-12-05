package de.dnpm.dip.admin.impl


import scala.concurrent.{
  Future,
  ExecutionContext
}
import cats.Monad
import de.dnpm.dip.util.Logging
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


  import ConnectionStatus.{
    Online,
    Offline
  }

  override def connectionReport(
    implicit env: ExecutionContext
  ): Future[ConnectionReport] =
    for {
      self <-
        (connector ! (statusRequest,Site.local))
          .map {
            case Right(_) =>
              ConnectionStatus(
                Site.local,
                Online,
                None
              )

            case Left(msg) =>
              ConnectionStatus(
                Site.local,
                Offline,
                Some(msg)
              )
          }
          .recover {
            case t =>
              log.warn(s"Self-availability check failed, most likely due to connection problem to the broker itself: ${t.getMessage}")
              ConnectionStatus(
                Site.local,
                Offline,
                Some(
"""Self-availability check via broker failed.
Your local node could still be correctly reachable by external peers, but cannot check this itself due to connection problems to the broker.
Check out the backend logs for details"""
                )
              )
          }

      peers <-
        (connector ! statusRequest).map(
           ConnectionStatus.from(_)
             .toList
             .sortBy(_.site.display.get)  // call of .get safe here
        )
    } yield ConnectionReport(
      self,
      peers
    )

}
