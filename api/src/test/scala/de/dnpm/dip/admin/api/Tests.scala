package de.dnpm.dip.admin.api


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


  "RolesSPI" must "have worked" in {
    rolesTry.isSuccess mustBe true
  }


  "Role set" must "be non-empty" in {
    Roles.getAll must not be (empty)
  }

}
