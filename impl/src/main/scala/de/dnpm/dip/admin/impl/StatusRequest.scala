package de.dnpm.dip.admin.impl


import de.dnpm.dip.coding.Coding
import de.dnpm.dip.model.Site
import de.dnpm.dip.service.PeerToPeerRequest
import play.api.libs.json.{
  Json,
  JsNull,
  OWrites
}

final case class StatusRequest
(
  origin: Coding[Site]
)
extends PeerToPeerRequest
{
  type ResultType = JsNull.type
}


object StatusRequest
{
  implicit val writes: OWrites[StatusRequest] =
    Json.writes[StatusRequest]
}
