# Company Driver Portal — Frontend

React single-page application (SPA) for the **Company & Driver Management System**. It provides the login and registration UI, company and driver list/search/create/edit/delete screens with pagination, and user management (list, role update, delete for SUPER_ADMIN). All visibility and actions are driven by the user’s role (USER, ADMIN, SUPER_ADMIN) and JWT-based authentication.

---

## Tech Stack

| Technology     | Purpose |
|----------------|---------|
| **React 18**   | UI library |
| **TypeScript 5.2** | Typing and tooling |
| **Vite 5**     | Build tool and dev server |
| **Tailwind CSS 3** | Styling |
| **Lucide React** | Icons |

---

## Prerequisites

- **Node.js 18+** (20+ recommended).
- **npm** or **yarn**.
- **Backend** running at **http://localhost:8080** (see [backend/README.md](backend/README.md); ensure MySQL username and password are set in the backend YML file).

---

## Setup

1. **Navigate to the frontend folder:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm run dev
   ```

   The app is served at **http://localhost:3000** (port is set in `vite.config.ts`). The backend must be running at **http://localhost:8080** so that login, companies, drivers, and users APIs work.

---

## NPM Scripts (Detail)

| Command | Description |
|---------|-------------|
| `npm run dev` | Starts the Vite dev server with HMR. Default port: 3000. |
| `npm run build` | Runs TypeScript compiler (`tsc`) then Vite production build. Output: `dist/`. |
| `npm run preview` | Serves the `dist/` build locally so you can test the production bundle. |
| `npm run lint` | Runs ESLint on `.ts` and `.tsx` files with project rules. |

---

## Project Structure (Detail)

```
frontend/
├── index.html                 # HTML entry; title: "Company & Driver Management System"
├── package.json               # Dependencies and scripts
├── vite.config.ts             # Vite config (React plugin, dev server port 3000)
├── tailwind.config.js         # Tailwind configuration
├── postcss.config.js          # PostCSS (Tailwind)
├── tsconfig.json, tsconfig.node.json
├── src/
│   ├── main.tsx               # React entry; mounts <App /> into #root
│   ├── App.tsx                # Root component: AuthProvider, login/register vs main app, tab routing
│   ├── App.css                # Global styles (if any)
│   ├── index.css              # Tailwind directives / base styles
│   ├── context/
│   │   └── AuthContext.tsx    # Auth state (isAuthenticated, role), login(), logout(), token in localStorage
│   ├── components/
│   │   ├── Header.tsx         # Top bar with logout
│   │   ├── Navigation.tsx     # Tabs: Companies, Drivers, Users
│   │   ├── CompanyCard.tsx    # Company list item
│   │   ├── CompanyForm.tsx    # Create/edit company form
│   │   ├── DriverCard.tsx     # Driver list item
│   │   ├── DriverForm.tsx     # Create/edit driver form
│   │   └── Pagination.tsx     # Reusable pagination controls
│   ├── pages/
│   │   ├── LoginPage.tsx      # Login form; switch to register
│   │   ├── RegisterPage.tsx   # Register form; switch to login
│   │   ├── CompaniesPage.tsx  # List/search/pagination + CRUD for companies
│   │   ├── DriversPage.tsx    # List/search/pagination + CRUD for drivers
│   │   └── UsersPage.tsx      # User list; role update and delete (SUPER_ADMIN)
│   ├── utils/
│   │   ├── authApi.ts         # register() → POST /api/auth/register
│   │   ├── companyApi.ts      # getAll, getById, create, update, remove, search (POST /api/companies/search)
│   │   ├── driverApi.ts       # Same pattern for drivers
│   │   ├── userApi.ts         # User list, role update, delete
│   │   ├── jwt.ts             # Decode JWT, get role from token
│   │   ├── rbac.ts            # Helpers for role-based UI (e.g. can edit, can delete)
│   │   └── constants.ts       # e.g. ITEMS_PER_PAGE
│   └── types/
│       └── index.ts          # Company, Driver, User, TabType, ViewType, UserRole
└── public/                    # Static assets (e.g. vite.svg)
```

---

## Features (Detail)

- **Authentication**
  - **Login:** Username + password → POST `/api/auth/login` → JWT and role stored in `localStorage`.
  - **Register:** Username, password, email, role → POST `/api/auth/register`.
  - Unauthenticated users see only login/register; after login, the main app (tabs) is shown.
  - Token is sent as `Authorization: Bearer <token>` on all API calls from company, driver, and user utils.

- **Companies**
  - List all companies; search via backend `POST /api/companies/search` with pagination.
  - Create and edit company (ADMIN, SUPER_ADMIN); delete company (SUPER_ADMIN only).
  - Pagination component for search results.

- **Drivers**
  - Same pattern: list, search (backend search API), pagination, create, edit, delete by role.

- **Users**
  - List users (all authenticated roles); SUPER_ADMIN can update a user’s role and delete users.

- **Role-based UI (RBAC)**
  - Tabs and buttons (e.g. Add, Edit, Delete) are shown or hidden based on `UserRole` (USER, ADMIN, SUPER_ADMIN), aligned with backend permissions.

---

## API Base URL and Changing It

All API calls currently use **http://localhost:8080**. This is set in:

- `src/context/AuthContext.tsx` (login URL)
- `src/utils/authApi.ts`
- `src/utils/companyApi.ts`
- `src/utils/driverApi.ts`
- `src/utils/userApi.ts`

To point to another backend (e.g. staging or production):

- Either replace `BASE_URL` / login URL in each of these files, or
- Introduce a single config (e.g. `import.meta.env.VITE_API_URL`) and use it everywhere.

---

## Roles in the UI

| Role | Companies | Drivers | Users |
|------|-----------|---------|--------|
| **USER** | View list and details only | View list and details only | View list |
| **ADMIN** | View + Create + Edit (no Delete) | View + Create + Edit (no Delete) | View list |
| **SUPER_ADMIN** | Full CRUD including Delete | Full CRUD including Delete | View, update role, delete user |

---

## Build for Production

```bash
npm run build
```

Output is in **`dist/`**. Serve with any static file server or configure your backend to serve the `dist/` folder. Ensure the backend URL used in production is correct (see “API Base URL” above).

---

## Backend Dependency and MySQL Config

The frontend depends on the backend for auth and data. Before using the app:

1. **Backend** must be running (see [backend/README.md](backend/README.md)).
2. **Add your MySQL username and password** in the backend YML file: `backend/src/main/resources/application.yml` under `spring.datasource.username` and `spring.datasource.password`. Without this, the backend cannot start and the frontend will get connection or 5xx errors when calling the API.
