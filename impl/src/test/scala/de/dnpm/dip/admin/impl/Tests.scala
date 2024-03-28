package de.dnpm.dip.admin.impl


import scala.util.Success
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.must.Matchers._
import de.dnpm.dip.admin.api.AdminService
import play.api.libs.json.Json.{
  toJson,
  prettyPrint
}


class Tests extends AsyncFlatSpec
{

  System.setProperty("dnpm.dip.connector.type","fake")

  val serviceTry =
    AdminService.getInstance

  lazy val service =
    serviceTry.get


  "AdminService SPI" must "have worked" in {
    serviceTry.isSuccess mustBe true
  }


  "ConnectionReport" must "have worked" in {

    service.connectionReport
      .andThen {
        case Success(report) => println(prettyPrint(toJson(report)))
      }
      .map(_ => succeed)

  }

}

