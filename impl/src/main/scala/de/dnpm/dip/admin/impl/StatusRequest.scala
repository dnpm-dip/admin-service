package de.dnpm.dip.admin.impl


import de.dnpm.dip.coding.Coding
import de.dnpm.dip.model.Site
import de.dnpm.dip.service.{
  PeerToPeerRequest,
  ConnectionStatus
}
import play.api.libs.json.{
  Json,
  OWrites
}

final case class StatusRequest
(
  origin: Coding[Site]
)
extends PeerToPeerRequest
{
//  type ResultType = Status
  type ResultType = ConnectionStatus
}


object StatusRequest
{
  implicit val writes: OWrites[StatusRequest] =
    Json.writes[StatusRequest]
}
