# Company Driver Portal — Backend

Spring Boot REST API for the **Company & Driver Management System**. It handles user registration and login (JWT), CRUD and search for companies and drivers, and user management. All protected endpoints use role-based access control (USER, ADMIN, SUPER_ADMIN).

---

## Tech Stack

| Technology        | Purpose |
|-------------------|---------|
| **Java 21**       | Language |
| **Spring Boot 3.2** | Framework |
| **Spring Security** | Authentication & authorization |
| **JWT (jjwt 0.12.3)** | Stateless auth tokens |
| **Spring Data JPA** | Data access |
| **Hibernate**     | ORM |
| **MySQL 8**       | Database |
| **Lombok**        | Boilerplate reduction |
| **Bean Validation** | Request validation |

---

## Prerequisites

- **JDK 21** or higher (to run and build).
- **Maven 3.6+** (or use the included wrapper: `./mvnw` / `mvnw.cmd`).
- **MySQL 8** installed and running (local or remote). You will need a valid **username** and **password** to connect.

---

## Configuration: Add Your MySQL Username and Password in the YML File

The application reads database settings from a single YML file. **You must set your MySQL username and password there** before the backend will start.

1. **Open the configuration file:**
   - Path: **`backend/src/main/resources/application.yml`**
   - (From the repo root: `backend/src/main/resources/application.yml`.)

2. **Locate the `spring.datasource` block.** It looks like this:

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/CompanyDriverPortal?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
       username: <Username for MySQL>
       password: <Your MySQL Password>
       driver-class-name: com.mysql.cj.jdbc.Driver
   ```

3. **Replace the placeholders with your real MySQL credentials:**
   - **`username`** — Your MySQL login name (e.g. `root` or a dedicated user).
   - **`password`** — The password for that user.

   **Example** (use your own values):

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/CompanyDriverPortal?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
       username: root
       password: MySecurePassword123
       driver-class-name: com.mysql.cj.jdbc.Driver
   ```

4. **Save the file.** Do not commit real passwords to version control. For production, use environment variables or a secret manager and reference them in the YML or via Spring profiles.

**Notes:**

- The URL includes `createDatabaseIfNotExist=true`, so the database `CompanyDriverPortal` will be created automatically if it does not exist.
- If MySQL is on another host or port, change the `url` accordingly (e.g. `localhost:3306` → `your-host:3306`).
- Without correct `username` and `password`, the application will fail at startup with a datasource connection error.

---

## Setup and Run

1. **Ensure MySQL is running** and you have the username and password.

2. **Add your MySQL username and password** in `src/main/resources/application.yml` as described above.

3. **From the backend directory**, run the application:

   **Linux / macOS:**
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

   **Windows:**
   ```bash
   cd backend
   mvnw.cmd spring-boot:run
   ```

4. When the log shows something like “Started I11LabsTestApplication”, the API is ready at **http://localhost:8080**.

---

## Project Structure (Detail)

```
backend/
├── pom.xml                                    # Maven dependencies and build
├── mvnw, mvnw.cmd                             # Maven wrapper
├── src/main/
│   ├── java/com/test/CompanyDriverPortal/
│   │   ├── I11LabsTestApplication.java        # Spring Boot main class
│   │   ├── config/                            # Security and infrastructure
│   │   │   ├── SecurityConfig.java            # HTTP security, CORS, JWT filter
│   │   │   ├── JwtAuthenticationFilter.java  # Validates JWT on each request
│   │   │   ├── JwtUtil.java                   # JWT create/parse
│   │   │   └── CustomUserDetailsService.java  # Load user for Spring Security
│   │   ├── controller/                        # REST endpoints
│   │   │   ├── AuthController.java            # /api/auth (register, login)
│   │   │   ├── CompanyController.java         # /api/companies
│   │   │   ├── DriverController.java          # /api/drivers
│   │   │   └── UserController.java           # /api/users
│   │   ├── dto/                               # Data transfer objects
│   │   │   ├── auth/                          # RegisterRequest, LoginRequest, LoginResponse
│   │   │   ├── company/                       # CompanyRequestDto, CompanyResponseDto, CompanySearchDto
│   │   │   ├── driver/                        # DriverRequestDto, DriverResponseDto, DriverSearchDto
│   │   │   └── user/                          # UserResponseDto
│   │   ├── globalException/
│   │   │   ├── GlobalExceptionHandler.java    # Centralized exception handling
│   │   │   └── ResourceNotFoundException.java
│   │   ├── model/                             # JPA entities
│   │   │   ├── User.java                      # Users and roles
│   │   │   ├── Company.java, CompanyDetails.java
│   │   │   └── Driver.java, DriverDetails.java
│   │   ├── repository/                        # Spring Data JPA repositories
│   │   ├── service/                           # Business logic interfaces
│   │   │   └── impl/                          # Implementations
│   │   └── util/
│   │       └── SecurityUtil.java              # Current user from security context
│   └── resources/
│       └── application.yml                    # Server, DB, JWT, logging (ADD USERNAME/PASSWORD HERE)
└── src/test/                                  # Unit/integration tests
```

---

## API Reference (Detail)

Base URL: **http://localhost:8080**

All endpoints except `/api/auth/*` require the header: **`Authorization: Bearer <your-jwt-token>`**.

### Authentication (no token required)

| Method | Endpoint | Request body | Description |
|--------|----------|---------------|-------------|
| POST   | `/api/auth/register` | `{ "username", "password", "email", "role" }` | Register a new user. Returns token + role on success. |
| POST   | `/api/auth/login`    | `{ "username", "password" }` | Log in. Returns JWT and role. |

### Companies

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET    | `/api/companies` | USER, ADMIN, SUPER_ADMIN | List all companies. |
| GET    | `/api/companies/{id}` | USER, ADMIN, SUPER_ADMIN | Get one company by ID. |
| POST   | `/api/companies` | ADMIN, SUPER_ADMIN | Create company (JSON body per CompanyRequestDto). |
| PUT    | `/api/companies/{id}` | ADMIN, SUPER_ADMIN | Update company. |
| DELETE | `/api/companies/{id}` | SUPER_ADMIN only | Delete company. |
| POST   | `/api/companies/search` | USER, ADMIN, SUPER_ADMIN | Search with filters and pagination (page, size, filters). Returns Spring Page (content, totalPages, totalElements). |

### Drivers

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET    | `/api/drivers` | USER, ADMIN, SUPER_ADMIN | List all drivers. |
| GET    | `/api/drivers/{id}` | USER, ADMIN, SUPER_ADMIN | Get one driver by ID. |
| POST   | `/api/drivers` | ADMIN, SUPER_ADMIN | Create driver. |
| PUT    | `/api/drivers/{id}` | ADMIN, SUPER_ADMIN | Update driver. |
| DELETE | `/api/drivers/{id}` | SUPER_ADMIN only | Delete driver. |
| POST   | `/api/drivers/search` | USER, ADMIN, SUPER_ADMIN | Search with pagination. |

### Users

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET    | `/api/users` | USER, ADMIN, SUPER_ADMIN | List users (filtered by current user’s role). |
| PUT    | `/api/users/{id}/role?role=ADMIN` | SUPER_ADMIN only | Update a user’s role. |
| DELETE | `/api/users/{id}` | SUPER_ADMIN only | Delete a user. |

---

## Roles (Detail)

| Role | Companies | Drivers | Users |
|------|-----------|---------|--------|
| **USER** | View list, view by ID, search | View list, view by ID, search | View list |
| **ADMIN** | View + create + update (no delete) | View + create + update (no delete) | View list |
| **SUPER_ADMIN** | Full CRUD + delete | Full CRUD + delete | View, update role, delete user |

---

## Configuration Reference (application.yml)

| Property | Meaning |
|----------|---------|
| `server.port` | HTTP port (default `8080`). |
| `spring.datasource.url` | JDBC URL for MySQL. Default creates DB if not present. |
| **`spring.datasource.username`** | **Your MySQL username — must be set in the YML file.** |
| **`spring.datasource.password`** | **Your MySQL password — must be set in the YML file.** |
| `spring.jpa.hibernate.ddl-auto` | Schema strategy (`update` for dev: tables updated automatically). |
| `jwt.secret` | Secret key for signing JWTs. Use a strong value in production. |
| `jwt.expiration` | Token lifetime in milliseconds (e.g. `86400000` = 24 hours). |
| `logging.level.*` | Log levels (e.g. Security, application packages). |

**Again: add your MySQL username and password in `src/main/resources/application.yml` under `spring.datasource` before running the application.**

---

## CORS

Allowed origins for browser requests: `http://localhost:3000`, `http://localhost:5173`. Methods and headers are configured for typical SPA usage.

---

## Running Tests

```bash
./mvnw test
```

(Use `mvnw.cmd` on Windows.)

---

## Production Notes

- Do not commit real MySQL passwords or JWT secrets. Use environment variables or a secrets manager and inject them (e.g. via `application-prod.yml` or env placeholders).
- Consider setting `spring.jpa.hibernate.ddl-auto` to `validate` (or use Flyway/Liquibase) and using a dedicated DB user with limited privileges.
