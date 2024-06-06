package de.dnpm.dip.admin.api


import de.dnpm.dip.service.auth._



object AdminPermissions extends PermissionEnumeration
{
  val GetConnectionReport = Value("connection_report_read")


  override val display =
    Map(
      GetConnectionReport -> "DNPM-Verbindungs-Status abrufen"
    )

  override val description =
    Map(
      GetConnectionReport -> "Status-Bericht der Verbindungen zu anderen DNPM-Knoten abrufen/einsehen"
    )

}

class AdminPermissionsSPI extends PermissionsSPI
{
  override def getInstance: Permissions =
    AdminPermissions
}


