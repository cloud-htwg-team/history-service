package de.htwg.cad.timetracker.tracking

import java.util.UUID
import scala.collection.mutable
import scala.concurrent.Future

class MockDatabase extends EntryDataBase {
  private val entries = mutable.Map.empty[String, mutable.Map[String, TimeTrackingElement]]

  override def postEntry(tenantId: String, toAdd: TimeTrackingElement): Future[String] = {
    val id = UUID.randomUUID().toString
    entries.get(tenantId) match {
      case Some(tenantEntries) => tenantEntries += id -> toAdd
      case None => entries += tenantId -> mutable.Map(id -> toAdd)
    }
    Future.successful(id)
  }

  override def getEntry(tenantId: String, entryId: String): Future[Option[TimeTrackingElement]] = Future.successful(entries.get(tenantId).flatMap(_.get(entryId)))

  override def getAllEntries(tenantId: String): Future[List[TimeTrackingElement]] = Future.successful(entries.get(tenantId).toList.flatMap(_.values))
}
