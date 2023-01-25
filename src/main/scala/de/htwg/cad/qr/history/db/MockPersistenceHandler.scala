package de.htwg.cad.qr.history.db

import de.htwg.cad.qr.history.{CodeAdditionRequest, CodeEntry, CodeMetadataShort}

import java.util.UUID
import scala.collection.mutable
import scala.concurrent.Future

private class MockPersistenceHandler extends HistoryPersistenceHandler {
  private val entries = mutable.Map.empty[String, mutable.Map[String, mutable.Map[String, CodeEntry]]]

  override def postEntry(tenantId: String, userId: String, toAdd: CodeAdditionRequest): Future[String] = {
    val id = UUID.randomUUID().toString
    val entry = CodeEntry(tenantId, userId, id, toAdd.createdAt, toAdd.qrCode)
    entries.get(tenantId) match {
      case Some(tenantEntries) =>
        tenantEntries.get(userId) match {
          case Some(userEntries) => userEntries += id -> entry
          case None => tenantEntries += userId -> mutable.Map(id -> entry)
        }
      case None => entries += tenantId -> mutable.Map(userId -> mutable.Map(id -> entry))
    }
    Future.successful(id)
  }

  override def getEntry(tenantId: String, userId: String, entryId: String): Future[CodeEntry] =
    Future.successful(entries(tenantId)(userId)(entryId))

  override def getQrCode(tenantId: String, userId: String, entryId: String): Future[String] =
    Future.successful(entries(tenantId)(userId)(entryId).qrCode)

  override def getTenantEntries(tenantId: String): Future[List[CodeMetadataShort]] =
    Future.successful(entries.get(tenantId).toList.flatMap(_.values.flatMap(_.values)).map(e => CodeMetadataShort(e.userId, e.entryId, e.createdAt)))

  override def getUserEntries(tenantId: String, userId: String): Future[List[CodeMetadataShort]] =
    Future.successful(entries.get(tenantId).toList.flatMap(_.get(userId).toList.flatMap(_.values)).map(e => CodeMetadataShort(e.userId, e.entryId, e.createdAt)))
}
