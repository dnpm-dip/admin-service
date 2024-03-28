package de.dnpm.dip.admin.impl


import scala.util.{
  Left,
  Right
}
import scala.concurrent.{
  Future,
  ExecutionContext
}
import cats.Monad
import de.dnpm.dip.util.Logging
import de.dnpm.dip.coding.Coding
import de.dnpm.dip.model.Site
import de.dnpm.dip.service.Connector
import de.dnpm.dip.connector.{
  FakeConnector,
  HttpConnector
}
import de.dnpm.dip.connector.HttpMethod.GET
import de.dnpm.dip.admin.api._
import ConnectionReport.Status._



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
          "/api/peer2peer",
          { case r: StatusRequest => (GET, "status", Map.empty) }
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
    StatusRequest(connector.localSite)

  override def connectionReport(
    implicit env: ExecutionContext
  ): Future[ConnectionReport] = {

    (connector ! statusRequest)
      .map(
        _.map {
          case (site,result) =>
            result match {
              case Right(_)  =>
                ConnectionReport.Entry(site,Online,"-")
              case Left(err) =>
                ConnectionReport.Entry(site,Offline,err)
            }
        }
        .toList
      )
      .map(
        ConnectionReport(_)
      )

  }

}
