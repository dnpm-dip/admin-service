package de.dnpm.dip.admin.api


import scala.util.Try
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.must.Matchers._
import de.dnpm.dip.service.auth.{
  Permissions,
  Roles
}


class Tests extends AsyncFlatSpec
{

  val permissionsTry =
    Permissions.getInstance

  val rolesTry =
    Roles.getInstance


  "PermissionSPI" must "have worked" in {
    permissionsTry.isSuccess mustBe true
  }


  "Permission set" must "be non-empty" in {
    Permissions.getAll must not be (empty)
  }


  "Version parsing" must "have worked correctly" in { 

    val versions =
      List(
        "1.0.0",
        "2.0.1",
        "1.0.0-SNAPSHOT",
        "1.0.0-RC",
      )

    versions.map(Version(_))

    succeed
  }


  it must "have failed" in { 

    val versions =
      List(
        "1.a.0",
        "a.b.c",
        "v1.0",
        "alpha",
      )

    all (versions.map(v => Try(Version(v)).isSuccess)) mustBe false

  }

}
