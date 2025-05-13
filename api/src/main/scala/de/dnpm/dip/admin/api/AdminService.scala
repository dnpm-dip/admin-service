package de.dnpm.dip.admin.api


import scala.concurrent.{
  Future,
  ExecutionContext
}
import de.dnpm.dip.util.{
  SPI,
  SPILoader
}
import de.dnpm.dip.model.Site
import de.dnpm.dip.service.ConnectionStatus


trait AdminServiceOps[F[_],Env]
{

  def connectionStatus(
    implicit env: Env
  ): F[ConnectionStatus]

  def connectionReport(
    implicit env: Env
  ): F[ConnectionReport]

}


trait AdminService extends AdminServiceOps[Future,ExecutionContext]
{

  // By default, if this method is reached, the service is "online"
  override def connectionStatus(
    implicit env: ExecutionContext
  ): Future[ConnectionStatus] =
    Future.successful(
      ConnectionStatus(
        Site.local,
        ConnectionStatus.Online,
        None
      )
    )

}

trait AdminServiceProvider extends SPI[AdminService]

object AdminService extends SPILoader[AdminServiceProvider]
