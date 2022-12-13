package de.htwg.cad.timetracker.tracking

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

trait JsonParser  {
  implicit val entryFormat: RootJsonFormat[TimeTrackingElement] = jsonFormat3(TimeTrackingElement.apply)
}
