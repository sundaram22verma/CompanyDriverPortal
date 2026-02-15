# Company Driver Portal

A **Company and Driver Management System** with role-based access control. Manage companies, drivers, and users through a React single-page application (frontend) and a Spring Boot REST API (backend). Users sign in with JWT authentication and see different features based on their role (USER, ADMIN, or SUPER_ADMIN).

---

## What This Project Does

- **Authentication:** Register and log in; receive a JWT token used for all protected API calls.
- **Companies:** Create, read, update, and delete company records; search and paginate.
- **Drivers:** Create, read, update, and delete driver records; search and paginate.
- **Users:** List users; SUPER_ADMIN can update roles and delete users.
- **Roles:** USER (view only), ADMIN (view + create/update), SUPER_ADMIN (full access including delete and user management).

---

## Repository Layout

| Folder        | Description |
|---------------|-------------|
| **backend/**  | Spring Boot 3 (Java 21) REST API. Handles JWT authentication, MySQL persistence with JPA, and REST endpoints for auth, companies, drivers, and users. **See [backend/README.md](backend/README.md) for full details.** |
| **frontend/** | React 18 + TypeScript + Vite SPA. Login/register UI, companies and drivers list/search/CRUD, user management, and role-based visibility. **See [frontend/README.md](frontend/README.md) for full details.** |

---

## Before You Run: Configure MySQL

The backend connects to MySQL using settings in a YML file. **You must add your MySQL username and password** before the application will start successfully.

1. Open the backend configuration file:
   - **Path:** `backend/src/main/resources/application.yml`
2. Find the `spring.datasource` section and set:
   - **`username`** — your MySQL username (e.g. `root` or your DB user).
   - **`password`** — your MySQL password for that user.

Example (replace with your actual values):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/CompanyDriverPortal?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: root          # <-- Add your MySQL username here
    password: yourpassword  # <-- Add your MySQL password here
    driver-class-name: com.mysql.cj.jdbc.Driver
```

Without these values, the backend will fail to connect to the database. The database `CompanyDriverPortal` will be created automatically if it does not exist (because of `createDatabaseIfNotExist=true` in the URL).

---

## Quick Start

### 1. Backend

- Ensure **MySQL** is installed and running.
- **Add your MySQL username and password** in `backend/src/main/resources/application.yml` as described above.
- From the project root:

  ```bash
  cd backend
  ./mvnw spring-boot:run
  ```

  On Windows:

  ```bash
  cd backend
  mvnw.cmd spring-boot:run
  ```

- API base URL: **http://localhost:8080**

### 2. Frontend

- From the project root:

  ```bash
  cd frontend
  npm install
  npm run dev
  ```

- App URL: **http://localhost:3000**

### 3. Use the Application

- Open **http://localhost:3000** in your browser.
- Use **Register** to create an account or **Login** with existing credentials.
- After login, use the tabs to open **Companies**, **Drivers**, or **Users**. What you can do (view, add, edit, delete) depends on your role.

---

## Documentation

- **[Backend README](backend/README.md)** — Detailed backend setup, API reference, roles, and configuration (including MySQL in the YML file).
- **[Frontend README](frontend/README.md)** — Detailed frontend setup, scripts, project structure, and features.
