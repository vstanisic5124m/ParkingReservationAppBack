# Admin API Documentation

This document describes the admin endpoints for the Parking Reservation System.

## Authentication

All admin endpoints require JWT authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

**Note:** In the current implementation, all authenticated users can access admin endpoints. In a production environment, you should add role-based access control (RBAC) to restrict these endpoints to users with the ADMIN role.

## Base URL

```
http://localhost:8080/api/admin
```

## Endpoints

### 1. Get System Statistics

Get comprehensive system statistics for the admin dashboard.

**Endpoint:** `GET /api/admin/statistics`

**Response:**
```json
{
  "totalUsers": 150,
  "activeUsers": 142,
  "ownersCount": 25,
  "totalParkingSpaces": 150,
  "activeParkingSpaces": 148,
  "totalReservations": 450,
  "activeReservations": 125,
  "cancelledReservations": 325,
  "totalOwnerCancellations": 18
}
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/admin/statistics \
  -H "Authorization: Bearer <your-jwt-token>"
```

---

### 2. Get All Users

List all registered users in the system.

**Endpoint:** `GET /api/admin/users`

**Response:**
```json
[
  {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890",
    "isActive": true,
    "isOwner": false,
    "rating": 5,
    "createdAt": "2024-12-01T10:30:00",
    "updatedAt": "2024-12-10T15:45:00"
  }
]
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer <your-jwt-token>"
```

---

### 3. Get All Reservations

List all reservations in the system with optional status filtering.

**Endpoint:** `GET /api/admin/reservations`

**Query Parameters:**
- `status` (optional): Filter by reservation status (`ACTIVE` or `CANCELLED`)

**Response:**
```json
[
  {
    "id": 123,
    "userId": 5,
    "userEmail": "user@example.com",
    "userName": "John Doe",
    "parkingSpaceId": 42,
    "parkingType": "YARD",
    "spotNumber": 15,
    "reservationDate": "2024-12-15",
    "status": "ACTIVE",
    "createdAt": "2024-12-10T08:30:00",
    "updatedAt": "2024-12-10T08:30:00"
  }
]
```

**Examples:**
```bash
# Get all reservations
curl -X GET http://localhost:8080/api/admin/reservations \
  -H "Authorization: Bearer <your-jwt-token>"

# Get only active reservations
curl -X GET "http://localhost:8080/api/admin/reservations?status=ACTIVE" \
  -H "Authorization: Bearer <your-jwt-token>"

# Get only cancelled reservations
curl -X GET "http://localhost:8080/api/admin/reservations?status=CANCELLED" \
  -H "Authorization: Bearer <your-jwt-token>"
```

---

### 4. Get All Parking Spaces

List all parking spaces in the system.

**Endpoint:** `GET /api/admin/parking-spaces`

**Response:**
```json
[
  {
    "id": 1,
    "parkingType": "YARD",
    "spotNumber": 1,
    "isActive": true
  },
  {
    "id": 2,
    "parkingType": "GARAGE",
    "spotNumber": 1,
    "isActive": true
  }
]
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/admin/parking-spaces \
  -H "Authorization: Bearer <your-jwt-token>"
```

---

### 5. Get All Owner Cancellations

List all owner cancellations (dates when owners have made their parking spaces unavailable).

**Endpoint:** `GET /api/admin/owner-cancellations`

**Response:**
```json
[
  {
    "id": 1,
    "parkingSpaceId": 42,
    "parkingType": "YARD",
    "spotNumber": 15,
    "cancellationDate": "2024-12-15",
    "createdAt": "2024-12-10T14:20:00"
  }
]
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/admin/owner-cancellations \
  -H "Authorization: Bearer <your-jwt-token>"
```

---

### 6. Update User Owner Status

Update whether a user is designated as an owner.

**Endpoint:** `PUT /api/admin/users/{userId}/owner-status`

**Path Parameters:**
- `userId`: ID of the user to update

**Request Body:**
```json
{
  "isOwner": true
}
```

**Response:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890",
  "isActive": true,
  "isOwner": true,
  "rating": 5,
  "createdAt": "2024-12-01T10:30:00",
  "updatedAt": "2024-12-10T15:45:00"
}
```

**Example:**
```bash
curl -X PUT http://localhost:8080/api/admin/users/1/owner-status \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "isOwner": true
  }'
```

---

### 7. Update User Rating

Update a user's rating.

**Endpoint:** `PUT /api/admin/users/{userId}/rating`

**Path Parameters:**
- `userId`: ID of the user to update

**Request Body:**
```json
{
  "rating": 4
}
```

**Response:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890",
  "isActive": true,
  "isOwner": false,
  "rating": 4,
  "createdAt": "2024-12-01T10:30:00",
  "updatedAt": "2024-12-10T15:45:00"
}
```

**Example:**
```bash
curl -X PUT http://localhost:8080/api/admin/users/1/rating \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "rating": 4
  }'
```

---

## Error Responses

All endpoints may return error responses in the following format:

```json
{
  "message": "Error description"
}
```

### Common HTTP Status Codes

- `200 OK`: Request successful
- `400 Bad Request`: Invalid request parameters or validation error
- `401 Unauthorized`: Missing or invalid JWT token
- `404 Not Found`: Resource not found (e.g., user ID doesn't exist)
- `500 Internal Server Error`: Server error occurred

---

## Implementation Notes

### Security Considerations

1. **Authentication Required**: All endpoints require a valid JWT token
2. **Authorization**: Currently, any authenticated user can access these endpoints. For production:
   - Add role-based access control (RBAC)
   - Restrict these endpoints to users with `UserRole.ADMIN`
   - Update `SecurityConfig` to enforce role-based restrictions

### Performance Considerations

1. **Statistics Endpoint**: Optimized to fetch data in bulk rather than multiple individual queries
2. **Reservations Filtering**: Currently filters in-memory; for large datasets, consider adding database-level filtering methods
3. **Pagination**: For large datasets, consider adding pagination support to list endpoints

### Future Enhancements

1. **Pagination**: Add offset/limit parameters for large result sets
2. **Sorting**: Add sort parameters for list endpoints
3. **Advanced Filtering**: Add more filter options (date ranges, user search, etc.)
4. **Bulk Operations**: Add endpoints for bulk user/reservation management
5. **Audit Logging**: Track admin actions for compliance
6. **Export Functionality**: Add CSV/Excel export for reports

---

## Testing

### Test the Statistics Endpoint

```bash
# First, register and login to get a JWT token
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "password123"
  }' | jq -r '.token')

# Then use the token to access admin endpoints
curl -X GET http://localhost:8080/api/admin/statistics \
  -H "Authorization: Bearer $TOKEN"
```

### Verify Response

The response should contain current system statistics:
- User counts (total, active, owners)
- Parking space counts (total, active)
- Reservation counts (total, active, cancelled)
- Owner cancellation count

---

### 8. Create New Admin

Create a new admin account.

**Endpoint:** `POST /api/admin/admins`

**Request Body:**
```json
{
  "email": "newadmin@example.com",
  "password": "securepassword123",
  "firstName": "Jane",
  "lastName": "Admin",
  "phoneNumber": "+1234567890"
}
```

**Response:**
```json
{
  "id": 1,
  "email": "newadmin@example.com",
  "firstName": "Jane",
  "lastName": "Admin",
  "phoneNumber": "+1234567890",
  "isActive": true,
  "createdAt": "2024-12-13T10:30:00",
  "updatedAt": "2024-12-13T10:30:00"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/admin/admins \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newadmin@example.com",
    "password": "securepassword123",
    "firstName": "Jane",
    "lastName": "Admin",
    "phoneNumber": "+1234567890"
  }'
```

---

### 9. Get All Admins

List all admin accounts in the system.

**Endpoint:** `GET /api/admin/admins`

**Response:**
```json
[
  {
    "id": 1,
    "email": "admin@example.com",
    "firstName": "Jane",
    "lastName": "Admin",
    "phoneNumber": "+1234567890",
    "isActive": true,
    "createdAt": "2024-12-13T10:30:00",
    "updatedAt": "2024-12-13T10:30:00"
  }
]
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/admin/admins \
  -H "Authorization: Bearer <your-jwt-token>"
```

---

### 10. Get Admin by ID

Retrieve a specific admin account by ID.

**Endpoint:** `GET /api/admin/admins/{id}`

**Path Parameters:**
- `id`: ID of the admin to retrieve

**Response:**
```json
{
  "id": 1,
  "email": "admin@example.com",
  "firstName": "Jane",
  "lastName": "Admin",
  "phoneNumber": "+1234567890",
  "isActive": true,
  "createdAt": "2024-12-13T10:30:00",
  "updatedAt": "2024-12-13T10:30:00"
}
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/admin/admins/1 \
  -H "Authorization: Bearer <your-jwt-token>"
```

---

### 11. Update Admin

Update an existing admin account.

**Endpoint:** `PUT /api/admin/admins/{id}`

**Path Parameters:**
- `id`: ID of the admin to update

**Request Body:**
```json
{
  "firstName": "Jane",
  "lastName": "Administrator",
  "phoneNumber": "+0987654321",
  "isActive": true
}
```

**Note:** All fields are optional. Only provided fields will be updated.

**Response:**
```json
{
  "id": 1,
  "email": "admin@example.com",
  "firstName": "Jane",
  "lastName": "Administrator",
  "phoneNumber": "+0987654321",
  "isActive": true,
  "createdAt": "2024-12-13T10:30:00",
  "updatedAt": "2024-12-13T14:15:00"
}
```

**Example:**
```bash
curl -X PUT http://localhost:8080/api/admin/admins/1 \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Administrator",
    "phoneNumber": "+0987654321"
  }'
```

---

### 12. Delete Admin

Delete an admin account.

**Endpoint:** `DELETE /api/admin/admins/{id}`

**Path Parameters:**
- `id`: ID of the admin to delete

**Response:**
```json
{
  "message": "Admin deleted successfully"
}
```

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/admin/admins/1 \
  -H "Authorization: Bearer <your-jwt-token>"
```

---

## Support

For issues or questions about the admin API, please refer to the main README.md or open an issue on GitHub.
