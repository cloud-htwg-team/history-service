package de.htwg.cad.timetracker.tracking.db

import com.google.cloud.storage.{BlobId, BlobInfo, StorageOptions}

private object GoogleBucketHandler {
  private val bucketName = "cloud-computing-fotos"
  private val projectId = "cloud-computing-pre"
  private val storage = StorageOptions.newBuilder.setProjectId(projectId).build.getService

  def uploadObject(objectName: String, data: Array[Byte]): Unit = {
    val blobId = BlobId.of(bucketName, objectName)
    val blobInfo = BlobInfo.newBuilder(blobId).build
    storage.create(blobInfo, data)
  }

  def getObject(objectName: String): Array[Byte] = {
    val blobId = BlobId.of(bucketName, objectName)
    storage.get(blobId).getContent()
  }
}
