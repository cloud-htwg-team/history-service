package de.htwg.cad.timetracker.tracking

final case class CodeAdditionRequest(createdAt: Long, qrCode: String)
final case class CodeMetadataShort(userId: String, entryId: String, createdAt: Long)
final case class CodeMetadataExtended(tenantId: String, userId: String, entryId: String, createdAt: Long) {
  def toEntry(qrCode: String): CodeEntry = CodeEntry(tenantId, userId, entryId, createdAt, qrCode)
}
final case class CodeEntry(tenantId: String, userId: String, entryId: String, createdAt: Long, qrCode: String)
