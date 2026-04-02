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
com.example.inventory
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

## Database Notes
- Entities are normalized: Product, Customer, OrderEntity, OrderItem, AppUser, Role.
- Indexes are defined at entity-level (`@Table(indexes = ...)`) for frequent lookup paths.
- `@EntityGraph` is used in order repository to avoid N+1 query issues when fetching order details.

## Run Locally
### Option A (quick start, no MySQL required)
1. Run with default profile (H2 in-memory DB):
   ```bash
   mvn spring-boot:run
   ```
2. Open Swagger UI:
   - `http://localhost:8080/swagger-ui.html`
3. (Optional) H2 console:
   - `http://localhost:8080/h2-console`

### Option B (MySQL profile)
1. Create MySQL database (or let URL create it automatically).
2. Set DB credentials as environment variables or use defaults:
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
3. Run with MySQL profile:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=mysql
   ```
4. If you see `Access denied for user 'root'@'localhost'`, your local MySQL password does not match `DB_PASSWORD`.
1. Create MySQL database or let URL create automatically.
2. Update `src/main/resources/application.yml` credentials if needed.
3. Run:
   ```bash
   mvn spring-boot:run
   ```
4. Open Swagger UI:
   - `http://localhost:8080/swagger-ui.html`

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
