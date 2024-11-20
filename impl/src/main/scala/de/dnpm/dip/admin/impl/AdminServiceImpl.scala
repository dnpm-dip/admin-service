package de.dnpm.dip.admin.impl


import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.SECONDS
import scala.util.{
  Left,
  Right,
  Success,
  Failure
}
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
  HttpMethod
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
  
  import HttpMethod._

  private val connector =
    System.getProperty("dnpm.dip.connector.type","broker") match {
      case HttpConnector.Type(typ) =>
        HttpConnector(
          typ,
          { case r: StatusRequest => (GET, "/api/peer2peer/status", Map.empty) }
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

//  import scala.concurrent.ExecutionContext.Implicits._

  private val statusRequest =
    StatusRequest(Site.local)

/*  
  private val executor =
    Executors.newSingleThreadScheduledExecutor

  private val report =
    new AtomicReference(ConnectionReport(List.empty))

  // Schedule report compilation every minute
  executor.scheduleAtFixedRate(() => compileConnectionReport, 0, 60, SECONDS)


  private def compileConnectionReport = {
    log.debug("Compiling network connection status report")
    (connector ! statusRequest)
      .map(
        rs =>
          ConnectionReport(
            ConnectionStatus.from(rs).toList
          )
      )
      .onComplete { 
        case Success(r) => report.set(r)
        case Failure(t) => log.error("During compilation of network connection status report",t)
      }
  }

  override def connectionReport(
    implicit env: ExecutionContext
  ): Future[ConnectionReport] =
    Future.successful(report.get)
*/


  override def connectionReport(
    implicit env: ExecutionContext
  ): Future[ConnectionReport] =
    (connector ! statusRequest)
      .map(
        rs =>
          ConnectionReport(
            ConnectionStatus.from(rs).toList
          )
      )

}
