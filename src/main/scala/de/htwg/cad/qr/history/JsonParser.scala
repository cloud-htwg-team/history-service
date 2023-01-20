package de.htwg.cad.qr.history

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

trait JsonParser  {
  implicit val additionRequestFormat: RootJsonFormat[CodeAdditionRequest] = jsonFormat3(CodeAdditionRequest)
  implicit val metadataShortFormat: RootJsonFormat[CodeMetadataShort] = jsonFormat3(CodeMetadataShort)
  implicit val metadataExtendedFormat: RootJsonFormat[CodeMetadataExtended] = jsonFormat4(CodeMetadataExtended)
  implicit val entryFormat: RootJsonFormat[CodeEntry] = jsonFormat5(CodeEntry)
}
