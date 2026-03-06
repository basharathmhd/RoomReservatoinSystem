# 🏨 Ocean View Resort — Room Reservation System

A full-stack hotel management and room reservation system built with **Java Servlets** (backend) and **React + Vite** (frontend). Designed to manage guests, rooms, reservations, billing, payments, and user accounts for a resort environment.

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Database Setup](#-database-setup)
- [Backend Setup](#-backend-setup)
- [Frontend Setup](#-frontend-setup)
- [API Endpoints](#-api-endpoints)
- [Security](#-security)
- [Testing](#-testing)
- [Screenshots](#-screenshots)
- [License](#-license)

---

## ✨ Features

| Module                 | Capabilities                                                                              |
| ---------------------- | ----------------------------------------------------------------------------------------- |
| **Authentication**     | Session-based login, password hashing, role-based access (Admin / Staff)                  |
| **Dashboard**          | Quick-access overview with key metrics and navigation                                     |
| **Guest Management**   | Full CRUD — add, edit, view, search, and delete guest profiles                            |
| **Room Types**         | Define room categories with base rates, capacities, and amenities                         |
| **Room Management**    | Track room inventory, floor assignments, and availability status                          |
| **Reservations**       | Book rooms with date validation (no backdating), availability checks, and status tracking |
| **Billing**            | Auto-calculate totals from room charges, service charges, tax (10%), and discounts        |
| **Payments**           | Record payments against bills with multiple methods (Cash, Card, Bank Transfer)           |
| **Reports**            | Aggregated dashboard with total guests, reservations, rooms, revenue, and occupancy rate  |
| **User Management**    | Admin panel to create, edit, and deactivate staff accounts                                |
| **Settings**           | Application preferences and system configuration                                          |
| **Dark / Light Theme** | Toggle between dark and light UI modes                                                    |

---

## 🛠 Tech Stack

### Backend

| Technology        | Version | Purpose                       |
| ----------------- | ------- | ----------------------------- |
| Java              | 8+      | Core language                 |
| Java Servlets     | 3.1     | REST API layer                |
| Maven             | 3.x     | Build & dependency management |
| MySQL             | 8.0     | Relational database           |
| MySQL Connector/J | 8.0.33  | JDBC driver                   |
| JUnit 5           | 5.9.3   | Unit testing                  |

### Frontend

| Technology       | Version | Purpose               |
| ---------------- | ------- | --------------------- |
| React            | 19.x    | UI framework          |
| Vite             | 7.x     | Dev server & bundler  |
| React Router DOM | 7.x     | Client-side routing   |
| Tailwind CSS     | 3.4     | Utility-first styling |
| Lucide React     | 0.576   | Icon library          |
| React Hot Toast  | 2.6     | Toast notifications   |

---

## 📁 Project Structure

```
RoomReservationSystem/
├── pom.xml                          # Maven build config
├── schema.sql                       # MySQL database schema
├── README.md
│
├── src/main/java/com/oceanview/
│   ├── dao/                         # Data Access Objects (JDBC)
│   │   ├── BaseDAO.java             # Generic base DAO with helper methods
│   │   ├── GuestDAO.java
│   │   ├── RoomDAO.java
│   │   ├── RoomTypeDAO.java
│   │   ├── ReservationDAO.java
│   │   ├── BillDAO.java
│   │   ├── PaymentDAO.java
│   │   ├── UserDAO.java
│   │   └── ReportDAO.java
│   ├── modal/                       # Entity / Model classes
│   │   ├── Guest.java
│   │   ├── Room.java
│   │   ├── RoomType.java
│   │   ├── Reservation.java
│   │   ├── Bill.java
│   │   ├── Payment.java
│   │   ├── User.java
│   │   └── ReportSummary.java
│   ├── servlet/                     # REST API Servlets
│   │   ├── AuthServlet.java
│   │   ├── GuestServlet.java
│   │   ├── RoomServlet.java
│   │   ├── RoomTypeServlet.java
│   │   ├── ReservationServlet.java
│   │   ├── BillServlet.java
│   │   ├── PaymentServlet.java
│   │   ├── UserServlet.java
│   │   └── ReportServlet.java
│   ├── filter/                      # Servlet Filters
│   │   └── CORSFilter.java
│   ├── service/                     # Business logic services
│   │   └── DatabaseService.java
│   └── util/                        # Utility classes
│       ├── JSONUtil.java
│       ├── PasswordUtil.java
│       └── ValidationUtil.java
│
├── src/test/java/com/oceanview/     # JUnit 5 test classes
│   └── util/
│       ├── ValidationUtilTest.java
│       └── JSONUtilTest.java
│
└── frontend/                        # React + Vite SPA
    ├── package.json
    ├── vite.config.js
    ├── tailwind.config.js
    └── src/
        ├── App.jsx                  # Root component with routing
        ├── main.jsx                 # Entry point
        ├── context/
        │   ├── AuthContext.jsx       # Authentication state
        │   └── ThemeContext.jsx      # Dark/Light mode state
        ├── components/
        │   ├── DashboardLayout.jsx   # Sidebar + top nav layout
        │   └── ui/                   # Reusable UI components
        │       ├── Button.jsx
        │       ├── Input.jsx
        │       ├── Card.jsx
        │       ├── Table.jsx
        │       ├── Badge.jsx
        │       ├── Dialog.jsx
        │       └── ConfirmDialog.jsx
        ├── pages/
        │   ├── LoginPage.jsx
        │   ├── DashboardPage.jsx
        │   ├── GuestsPage.jsx
        │   ├── RoomsPage.jsx
        │   ├── RoomTypesPage.jsx
        │   ├── ReservationsPage.jsx
        │   ├── BillsPage.jsx
        │   ├── PaymentsPage.jsx
        │   ├── UsersPage.jsx
        │   ├── ReportsPage.jsx
        │   └── SettingsPage.jsx
        └── services/
            └── api.js               # Centralized API service
```

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java JDK** 8 or higher
- **Apache Maven** 3.x
- **Apache Tomcat** 9.x (or any Servlet 3.1+ container)
- **MySQL** 8.0+
- **Node.js** 18+ and **npm** 9+

---

## 🗄 Database Setup

1. Start your MySQL server.
2. Execute the schema file to create the database and all tables:

```bash
mysql -u root -p < schema.sql
```

This creates the `oceanview_resort` database with 8 tables:

- `room_types`, `rooms`, `guests`, `users`, `reservations`, `bills`, `payments`, `system_logs`

3. Update the database connection settings in `DatabaseService.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/oceanview_resort";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

---

## ⚙ Backend Setup

1. **Build the project** using Maven:

```bash
mvn clean package
```

2. **Deploy** the generated WAR file (`target/oceanview-resort.war`) to your Tomcat `webapps/` directory.

3. **Start Tomcat** — the API will be available at:

```
http://localhost:8085/oceanview-resort/api/
```

Alternatively, run directly from your IDE (e.g., IntelliJ IDEA) with a Tomcat run configuration.

---

## 🖥 Frontend Setup

1. Navigate to the frontend directory:

```bash
cd frontend
```

2. Install dependencies:

```bash
npm install
```

3. Start the development server:

```bash
npm run dev
```

4. Open your browser at:

```
http://localhost:3000
```

> **Note:** The Vite dev server proxies API requests to the backend. Ensure the backend is running before using the frontend.

---

## 🔌 API Endpoints

All endpoints are prefixed with `/api/`.

### Authentication

| Method | Endpoint           | Description                    |
| ------ | ------------------ | ------------------------------ |
| `POST` | `/api/auth/login`  | Login with username & password |
| `POST` | `/api/auth/logout` | End user session               |

### Guests

| Method   | Endpoint           | Description      |
| -------- | ------------------ | ---------------- |
| `GET`    | `/api/guests`      | List all guests  |
| `GET`    | `/api/guests/{id}` | Get guest by ID  |
| `POST`   | `/api/guests`      | Create new guest |
| `PUT`    | `/api/guests/{id}` | Update guest     |
| `DELETE` | `/api/guests/{id}` | Delete guest     |

### Rooms

| Method   | Endpoint          | Description     |
| -------- | ----------------- | --------------- |
| `GET`    | `/api/rooms`      | List all rooms  |
| `POST`   | `/api/rooms`      | Create new room |
| `PUT`    | `/api/rooms/{id}` | Update room     |
| `DELETE` | `/api/rooms/{id}` | Delete room     |

### Room Types

| Method   | Endpoint               | Description         |
| -------- | ---------------------- | ------------------- |
| `GET`    | `/api/room-types`      | List all room types |
| `POST`   | `/api/room-types`      | Create room type    |
| `PUT`    | `/api/room-types/{id}` | Update room type    |
| `DELETE` | `/api/room-types/{id}` | Delete room type    |

### Reservations

| Method   | Endpoint                               | Description             |
| -------- | -------------------------------------- | ----------------------- |
| `GET`    | `/api/reservations`                    | List all reservations   |
| `GET`    | `/api/reservations/check-availability` | Check room availability |
| `POST`   | `/api/reservations`                    | Create reservation      |
| `PUT`    | `/api/reservations/{resNumber}`        | Update reservation      |
| `DELETE` | `/api/reservations/{resNumber}`        | Cancel reservation      |

### Bills

| Method   | Endpoint          | Description       |
| -------- | ----------------- | ----------------- |
| `GET`    | `/api/bills`      | List all bills    |
| `GET`    | `/api/bills/{id}` | Get bill by ID    |
| `POST`   | `/api/bills`      | Generate new bill |
| `PUT`    | `/api/bills/{id}` | Update bill       |
| `DELETE` | `/api/bills/{id}` | Delete bill       |

### Payments

| Method | Endpoint        | Description       |
| ------ | --------------- | ----------------- |
| `GET`  | `/api/payments` | List all payments |
| `POST` | `/api/payments` | Record payment    |

### Users

| Method   | Endpoint          | Description    |
| -------- | ----------------- | -------------- |
| `GET`    | `/api/users`      | List all users |
| `POST`   | `/api/users`      | Create user    |
| `PUT`    | `/api/users/{id}` | Update user    |
| `DELETE` | `/api/users/{id}` | Delete user    |

### Reports

| Method | Endpoint       | Description                   |
| ------ | -------------- | ----------------------------- |
| `GET`  | `/api/reports` | Get aggregated report metrics |

---

## 🔐 Security

| Feature                 | Implementation                                                    |
| ----------------------- | ----------------------------------------------------------------- |
| **Password Hashing**    | SHA-256 hashing via `PasswordUtil.java`                           |
| **Session Management**  | `HttpSession`-based authentication                                |
| **Input Validation**    | `ValidationUtil.java` — email, phone, alphanumeric, length checks |
| **Input Sanitization**  | Strips dangerous characters (`'`, `"`, `\`, `*`, `;`)             |
| **Username Validation** | Login rejects special characters (alphanumeric only)              |
| **Date Validation**     | Reservation check-in dates cannot be in the past                  |
| **CORS**                | `CORSFilter.java` enables cross-origin requests for the frontend  |
| **Protected Routes**    | Frontend guards dashboard routes behind authentication            |

---

## 🧪 Testing

### Running Unit Tests

```bash
mvn test
```

### Test Coverage

| Test Class           | Methods Tested                                                                                                                                             |
| -------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `ValidationUtilTest` | `isEmpty`, `isValidEmail`, `isValidPhone`, `isAlphanumeric`, `isValidLength`, `isPositiveNumber`, `isInRange`, `sanitizeInput`, `isValidReservationNumber` |
| `JSONUtilTest`       | JSON serialization and deserialization utilities                                                                                                           |

### Test Cases

A comprehensive set of **45 manual test cases** covering all modules (Login, Dashboard, Guests, Rooms, Reservations, Bills, Payments, Users, Reports) is documented separately for QA purposes.

---

## 📸 Screenshots

> Screenshots can be added here to showcase the application UI.

| Page         | Description                             |
| ------------ | --------------------------------------- |
| Login        | Clean login form with validation        |
| Dashboard    | Overview with quick-access navigation   |
| Guests       | Searchable guest management table       |
| Rooms        | Room grid with status indicators        |
| Reservations | Date-validated booking system           |
| Billing      | Auto-calculated invoices with tax       |
| Payments     | Transaction history with details dialog |
| Reports      | Aggregated metrics dashboard            |

---

## 📄 License

This project is developed as part of a BSc assessment. All rights reserved.

---

> **Ocean View Resort** — _Where comfort meets the coast_ 🌊
