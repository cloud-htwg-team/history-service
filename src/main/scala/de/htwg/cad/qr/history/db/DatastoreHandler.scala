package de.htwg.cad.qr.history.db

import com.google.cloud.datastore.StructuredQuery.{CompositeFilter, PropertyFilter}
import com.google.cloud.datastore._
import de.htwg.cad.qr.history.{CodeMetadataExtended, CodeMetadataShort}

import scala.jdk.CollectionConverters.IteratorHasAsScala

private object DatastoreHandler {
  private val datastore = DatastoreOptions.getDefaultInstance.getService
  private val kind = "historyEntry"

  def saveMetadata(tenantId: String, userId: String, entryId: String, createdAt: Long): Unit = { // Instantiates a client
    val taskKey = datastore.newKeyFactory.setKind(kind).newKey(entryId)
    val metadata = Entity.newBuilder(taskKey)
      .set("tenantId", tenantId)
      .set("userId", userId)
      .set("entryId", entryId)
      .set("createdAt", createdAt)
      .build
    datastore.put(metadata)
  }

  def getEntry(entryId: String): CodeMetadataExtended = {
    val taskKey = datastore.newKeyFactory.setKind(kind).newKey(entryId)
    val retrieved: Entity = datastore.get(taskKey, Seq.empty[ReadOption]: _*)
    CodeMetadataExtended(
      retrieved.getString("tenantId"),
      retrieved.getString("userId"),
      retrieved.getString("entryId"),
      retrieved.getLong("createdAt"),
    )
  }

  def listTenantEntries(tenantId: String): List[CodeMetadataShort] = {
    runQuery(PropertyFilter.eq("tenantId", tenantId))
  }

  def listUserEntries(tenantId: String, userId: String): List[CodeMetadataShort] = {
    runQuery(CompositeFilter.and(
        PropertyFilter.eq("tenantId", tenantId),
        PropertyFilter.eq("userId", userId)))
  }

  private def runQuery(filter: StructuredQuery.Filter): List[CodeMetadataShort] = {
    val query = Query.newEntityQueryBuilder()
      .setKind(kind)
      .setFilter(filter)
      .build()
    collectEntries(query)
  }

  private def collectEntries(query: EntityQuery): List[CodeMetadataShort] =
    datastore.run(query, Seq.empty[ReadOption]: _*).asScala
      .map(entry => CodeMetadataShort(
        entry.getString("userId"),
        entry.getString("entryId"),
        entry.getLong("createdAt")))
      .toList
}
