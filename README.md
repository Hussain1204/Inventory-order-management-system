# Inventory & Order Management System (Spring Boot)

Production-ready backend project for managing products, customers, and orders with secure JWT-based access control.

## Tech Stack
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Spring Security + JWT
- Swagger/OpenAPI (`/swagger-ui.html`)
- JUnit 5 + Mockito

## Layered Architecture
```
com.hussain.inventory
├── config
├── controller
├── dto
│   ├── auth
│   ├── customer
│   ├── order
│   └── product
├── entity
├── exception
├── repository
├── security
└── service
    └── impl
```

## Main Features
1. Product CRUD + low-stock APIs
2. Customer create/get APIs
3. Multi-item order creation with stock validation and automatic inventory deduction
4. Order status workflow: `PLACED -> SHIPPED -> DELIVERED`
5. Role-based access:
   - `ADMIN`: product management + order status update
   - `USER`: place/view orders

## Key API Endpoints (15+)
- `POST /auth/login`
- `GET /auth/me`
- `POST /products`
- `PUT /products/{id}`
- `DELETE /products/{id}`
- `GET /products/{id}`
- `GET /products`
- `GET /products/low-stock`
- `POST /customers`
- `GET /customers/{id}`
- `GET /customers`
- `POST /orders`
- `GET /orders/{id}`
- `GET /orders`
- `PATCH /orders/{id}/status`

## Security & Demo Credentials
`DataInitializer` seeds users on startup:
- `admin / admin123` (ROLE_ADMIN, ROLE_USER)
- `user / user123` (ROLE_USER)

Use returned JWT token from `/auth/login` in Swagger using Bearer auth.

**⚠️ Important:** Demo credentials are for local development only. Replace with secure credentials in production.

## Database Notes
- Entities are normalized: Product, Customer, OrderEntity, OrderItem, AppUser, Role.
- Indexes are defined at entity-level (`@Table(indexes = ...)`) for frequent lookup paths.
- `@EntityGraph` is used in order repository to avoid N+1 query issues when fetching order details.

## Run Locally

### Option A (Quick Start - H2 In-Memory Database)
No MySQL required. Perfect for development and testing.

1. Run with default profile (uses H2):
   ```bash
   mvn spring-boot:run
   ```
2. Open Swagger UI:
   - `http://localhost:8080/swagger-ui.html`
3. (Optional) H2 console:
   - `http://localhost:8080/h2-console`

### Option B (MySQL Profile)
For production-like testing with a real MySQL database.

1. Ensure MySQL is running locally (or update `DB_URL` accordingly).
2. Set environment variables:
   ```bash
   export DB_URL=jdbc:mysql://localhost:3306/inventory_db?createDatabaseIfNotExist=true&useSSL=false
   export DB_USERNAME=root
   export DB_PASSWORD=your_mysql_password
   export JWT_SECRET=your-super-secret-jwt-key-min-64-chars-long-replace-me-in-production
   ```
3. Run with MySQL profile:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=mysql
   ```
4. Open Swagger UI:
   - `http://localhost:8080/swagger-ui.html`

### Troubleshooting
- **"Access denied for user 'root'@'localhost'"**: Your MySQL password doesn't match `DB_PASSWORD` env var.
- **Port 8080 already in use**: Change port via `java -jar target/*.jar --server.port=8081`

## Environment Variables (Production)

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `JWT_SECRET` | ✅ Yes | N/A | Secret key for signing JWTs (min 64 chars) |
| `JWT_EXPIRATION_MS` | ❌ No | 86400000 | JWT expiration in milliseconds (24 hours) |
| `DB_URL` | For MySQL | H2 in-memory | JDBC URL for database connection |
| `DB_USERNAME` | For MySQL | N/A | Database username |
| `DB_PASSWORD` | For MySQL | N/A | Database password |

## Testing
Run unit tests:
```bash
mvn test
```
Includes 20+ service-layer tests with Mockito.

## JWT Flow (Step-by-step)
1. User calls `POST /auth/login` with username/password.
2. `AuthenticationManager` validates credentials using `AppUserDetailsService`.
3. On success, `JwtService` creates signed JWT with username + roles.
4. Client stores token and sends `Authorization: Bearer <token>` in API requests.
5. `JwtAuthenticationFilter` intercepts request, extracts token, validates signature/expiry.
6. If valid, Spring Security context is populated with authenticated user.
7. Role checks (`@PreAuthorize`) allow/deny endpoint execution.

## Interview Questions (Project Based)
1. Why should entities not be exposed directly in REST APIs?
2. How does JWT authentication differ from session-based auth?
3. How do you prevent placing an order when stock is insufficient?
4. How did you avoid N+1 problems in order listing APIs?
5. Why use `@Transactional` in service methods?
6. How do role-based restrictions work with `@PreAuthorize`?
7. Why are indexes added on category/status/username columns?
8. How is order status transition validated safely?
9. What is the responsibility of global exception handling?
10. How do unit tests with Mockito improve reliability?