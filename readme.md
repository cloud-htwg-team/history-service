# History Service

## API
| Path                                                         | Method | Description                                                                                                                         |
|--------------------------------------------------------------|--------|-------------------------------------------------------------------------------------------------------------------------------------|
| history/tenant/{tenantId}                                    | GET    | Returns all QR-Code metadata of the tenant. (userId, entryId, createdAt (long))                                                     |
| history/tenant/{tenantId}/user/{userId}                      | GET    | Returns all QR-Code metadata of the user. (userId, entryId, createdAt (long))                                                       |
| history/tenant/{tenantId}/user/{userId}                      | POST   | Adds a new QR-Code for the specified user. Requires a createdAt data and the Base64 coded QR-image. Returns the new entryId.        |
| history/tenant/{tenantId}/user/{userId}/entry/{entryId}      | GET    | Returns all QR-Code information for the specified entry. (tenantId, userId, entryId, createdAt (long), qrCode (Base64 coded image)) |
| history/tenant/{tenantId}/user/{userId}/entry/{entryId}/code | GET    | Returns just the QR-Code image of the specified image as a byte array.                                                              |

All IDs are UUID strings.
