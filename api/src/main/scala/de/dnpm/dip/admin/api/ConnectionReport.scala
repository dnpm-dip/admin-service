package de.dnpm.dip.admin.api


import java.time.LocalDateTime
import de.dnpm.dip.service.ConnectionStatus
import play.api.libs.json.{
  Json,
  OWrites
}



final case class ConnectionReport
(
  peers: List[ConnectionStatus],
  createdAt: LocalDateTime = LocalDateTime.now
)


object ConnectionReport
{

  implicit val writesConnectionReport: OWrites[ConnectionReport] =
    Json.writes[ConnectionReport]

}
