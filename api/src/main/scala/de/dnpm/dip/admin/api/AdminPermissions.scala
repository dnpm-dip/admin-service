package de.dnpm.dip.admin.api


import de.dnpm.dip.service.auth._



object AdminPermissions extends PermissionEnumeration
{
  val GetConnectionReport = Value("connection_report_read")

  override val descriptions =
    Map(
      GetConnectionReport -> "Status-Bericht Verbindungen zu anderen DNPM-Knoten abrufen/einsehen"
    )

}


object AdminRoles extends Roles
{

  import AdminPermissions._

  val Admin =
    Role(
      "Admin",
      permissions,
      Some("DNPM-Knoten-Admin")
    )

  override val roles: Set[Role] =
    Set(
      Admin
    )

}


class AdminPermissionsSPI extends PermissionsSPI
{
  override def getInstance: Permissions =
    AdminPermissions
}


class AdminRolesSPI extends RolesSPI
{
  override def getInstance: Roles =
    AdminRoles
}


