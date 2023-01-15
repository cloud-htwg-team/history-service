package de.htwg.cad.qr.history.db

import de.htwg.cad.qr.history.{CodeAdditionRequest, CodeEntry, CodeMetadataShort}

import java.util.{Base64, UUID}
import scala.concurrent.{ExecutionContext, Future}

private class CloudPersistenceHandler(implicit executionContext: ExecutionContext) extends HistoryPersistenceHandler {
  override def postEntry(tenantId: String, userId: String, toAdd: CodeAdditionRequest): Future[String] = {
    val entryId = UUID.randomUUID().toString
    val qrData = Base64.getDecoder.decode(toAdd.qrCode)
    val imageUpload = Future(GoogleBucketHandler.uploadObject(entryId, qrData))
    val metadataSave = Future(DatastoreHandler.saveMetadata(tenantId, userId, entryId, toAdd.createdAt))
    imageUpload.flatMap(_ => metadataSave).map(_ => entryId)
  }

  override def getEntry(tenantId: String, userId: String, entryId: String): Future[CodeEntry] = {
    val metadataRequest = Future(DatastoreHandler.getEntry(entryId))
    val qrDataRequest = Future(GoogleBucketHandler.getObject(entryId))
    metadataRequest.filter(metadata => metadata.tenantId == tenantId && metadata.userId == userId)
      .flatMap(metadata => qrDataRequest.map(qr => metadata.toEntry(Base64.getEncoder.encodeToString(qr))))
  }

  override def getQrCode(tenantId: String, userId: String, entryId: String): Future[Array[Byte]] = {
    val metadataRequest = Future(DatastoreHandler.getEntry(entryId))
    val qrDataRequest = Future(GoogleBucketHandler.getObject(entryId))
    metadataRequest.filter(metadata => metadata.tenantId == tenantId && metadata.userId == userId)
      .flatMap(_ => qrDataRequest)
  }

  override def getTenantEntries(tenantId: String): Future[List[CodeMetadataShort]] = {
    Future(DatastoreHandler.listTenantEntries(tenantId))
  }

  override def getUserEntries(tenantId: String, userId: String): Future[List[CodeMetadataShort]] = {
    Future(DatastoreHandler.listUserEntries(tenantId, userId))
  }
}
