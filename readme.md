# History Service

## API
| Path                                                             | Method | Description                                                                                                                         |
|------------------------------------------------------------------|--------|-------------------------------------------------------------------------------------------------------------------------------------|
| history/tenants/{tenantId}/entries                               | GET    | Returns all QR-Code metadata of the tenant. (userId, entryId, createdAt (long))                                                     |
| history/tenants/{tenantId}/users/{userId}/entries                | GET    | Returns all QR-Code metadata of the user. (userId, entryId, createdAt (long))                                                       |
| history/tenants/{tenantId}/users/{userId}/entries                | POST   | Adds a new QR-Code for the specified user. Requires a createdAt data and the Base64 coded QR-image. Returns the new entryId.        |
| history/tenants/{tenantId}/users/{userId}/entries/{entryId}      | GET    | Returns all QR-Code information for the specified entry. (tenantId, userId, entryId, createdAt (long), qrCode (Base64 coded image)) |
| history/tenants/{tenantId}/users/{userId}/entries/{entryId}/code | GET    | Returns just the QR-Code image of the specified image as a byte array.                                                              |

All IDs are UUID strings.
