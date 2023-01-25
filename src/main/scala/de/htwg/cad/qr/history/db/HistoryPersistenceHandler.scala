package de.htwg.cad.qr.history.db

import de.htwg.cad.qr.history.{CodeAdditionRequest, CodeEntry, CodeMetadataShort}

import scala.concurrent.{ExecutionContext, Future}

trait HistoryPersistenceHandler {
  def postEntry(tenantId: String, userId: String, toAdd: CodeAdditionRequest): Future[String]
  def getEntry(tenantId: String, userId: String, entryId: String): Future[CodeEntry]
  def getQrCode(tenantId: String, userId: String, entryId: String): Future[String]
  def getTenantEntries(tenantId: String): Future[List[CodeMetadataShort]]
  def getUserEntries(tenantId: String, userId: String): Future[List[CodeMetadataShort]]
}

object HistoryPersistenceHandler {
  def mock: HistoryPersistenceHandler = new MockPersistenceHandler
  def cloud(implicit executionContext: ExecutionContext): HistoryPersistenceHandler = new CloudPersistenceHandler
}
