package de.dnpm.dip.admin.api


import java.time.LocalDateTime
import de.dnpm.dip.coding.Coding
import de.dnpm.dip.model.Site
import play.api.libs.json.{
  Json,
  Format,
  OWrites
}



final case class ConnectionReport
(
  peers: List[ConnectionReport.Entry],
  createdAt: LocalDateTime = LocalDateTime.now
)


object ConnectionReport
{

  object Status extends Enumeration
  {
    val Online = Value("online")
    val Offline = Value("offline")
    
    implicit val format: Format[Value] =
      Json.formatEnum(this)
  }


  final case class Entry
  (
    site: Coding[Site],
    status: Status.Value,
    details: String
  )

  implicit val writesEntry: OWrites[Entry] =
    Json.writes[Entry]


  implicit val writesConnectionReport: OWrites[ConnectionReport] =
    Json.writes[ConnectionReport]

}
