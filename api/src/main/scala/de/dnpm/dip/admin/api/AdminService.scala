package de.dnpm.dip.admin.api


import scala.concurrent.{
  Future,
  ExecutionContext
}
import de.dnpm.dip.util.{
  SPI,
  SPILoader
}


trait AdminServiceOps[F[_],Env]
{

  def connectionReport(
    implicit env: Env
  ): F[ConnectionReport]

}


trait AdminService extends AdminServiceOps[Future,ExecutionContext]

trait AdminServiceProvider extends SPI[AdminService]

object AdminService extends SPILoader[AdminServiceProvider]
