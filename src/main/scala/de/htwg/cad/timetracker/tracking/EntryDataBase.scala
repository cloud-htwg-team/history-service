package de.htwg.cad.timetracker.tracking

import scala.concurrent.Future

trait EntryDataBase {
  def postEntry(tenantId: String, toAdd: TimeTrackingElement): Future[String]
  def getEntry(tenantId: String, entryId: String): Future[Option[TimeTrackingElement]]
  def getAllEntries(tenantId: String): Future[List[TimeTrackingElement]]
}
