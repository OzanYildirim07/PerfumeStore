# 🌟 LuxPerfume E-Commerce System

LuxPerfume is a premium full-stack luxury e-commerce platform featuring a secure, scalable Spring Boot REST API backend and a modern, responsive, vanilla HTML5/CSS3/JavaScript frontend interface. The system delivers a complete shopping experience with real-time database persistence, secure JWT authentication, dynamic cart management, and seamless order dispatch flows.

---

## 🛠️ Tech Stack & Architecture

### 🖥️ Backend Architecture
* Core Language & Framework: Java 17 & Spring Boot 3.3.0
* Security & Auth: Spring Security Core with Stateless JWT (JSON Web Tokens)
* Data Layer (ORM): Spring Data JPA (Hibernate ORM 6.5.2)
* Database Engine: PostgreSQL 42.7.3
* API Documentation: Springdoc OpenAPI v2.5.0 (Swagger UI)
* Tooling & Helpers: Maven, Lombok, ModelMapper, Jakarta Validation API

### 🎨 Frontend Architecture
* Interface: Vanilla HTML5, Semantic CSS3 (Clean layout, custom modal systems, toast notification layouts)
* State & Logic: Native asynchronous JavaScript (Fetch API, dynamic DOM manipulation, LocalStorage-backed cart state management)
* Design Philosophy: 100% Responsive Design supporting desktop, tablet, and mobile displays without external UI frameworks.

---

## 📋 Prerequisites

Before running the application locally, ensure you have the following installed:
* Java Development Kit (JDK): Version 17 (e.g., Eclipse Adoptium OpenJDK 17)
* Database Server: PostgreSQL instance running locally (or via Docker Desktop)
* Build Tool: Apache Maven (built-in wrapper mvnw is included in the root directory)

---

## 🚀 Setup & Installation Instructions

Ensure you have a PostgreSQL database named luxperfume running. Update the database credentials inside the backend's source properties configuration file if your local credentials differ:

spring.datasource.url=jdbc:postgresql://localhost:5432/luxperfume
spring.datasource.username=postgres
spring.datasource.password=your_secure_password

### Docker alternative for PostgreSQL:
docker compose up -d

### Build and Package instructions:
To package the application, navigate to the root directory and run the Maven wrapper command matching your operating system:

# On Linux/macOS:
./mvnw clean package -DskipTests

# On Windows (CMD/PowerShell):
mvnw.cmd clean package -DskipTests

### Running the application:
java -jar target/LuxPerfume-1.0.0.jar

### Application Access Points:
* Web User Interface: http://localhost:8080
* Interactive Swagger Documentation: http://localhost:8080/swagger-ui/index.html
* Raw JSON API Metadata: http://localhost:8080/v3/api-docs

---

## 🗄️ Relational Database Schema Model

The persistence layer automatically generates, validates, and updates relational entities mapped out into a highly functional relational web of tables:

| Table Name | Description | Key Relationships |
| :--- | :--- | :--- |
| users | Customer credentials, profile fields, encrypted passwords, and RBAC authority levels. | One-to-Many with orders |
| perfumes | Core catalog items including brand attributes, descriptive metadata, dimensions, sales counters, and prices. | Many-to-One with categories, Many-to-Many with contents |
| categories | Dynamic separation of catalog entries. | One-to-Many with perfumes |
| contents | Fragrance composition ingredients, top/heart/base note categorizations. | Many-to-Many with perfumes |
| orders | High-level receipts logging transactions timestamps, global totals, and order fulfillment states. | Many-to-One with users, One-to-Many with order_items |
| order_items | Intermediate operational tables tracking exact prices, ordered quantities, and subtotal multipliers. | Many-to-One with orders, Many-to-One with perfumes |
| payments | Secure payment auditing logs mapping out transactional keys, methods (e.g., Credit Card), and approval metrics. | One-to-One with orders |

---

## 🔒 Enterprise-Grade Security Implementation

* Stateless JWT Authorization: Client authentication state is managed using securely generated Bearer tokens passed down via custom HTTP authorization request headers (Authorization: Bearer token).
* BCrypt Password Hashing: Credentials undergo high-entropy salting and cryptography structures through Spring Security configuration beans before persisting into PostgreSQL.
* Role-Based Access Control (RBAC): USER role is authorized for safe read operations (GET endpoints), while the ADMIN role retains complete administrative write capabilities (POST, PUT, DELETE operations).
* Data Safety Mechanisms: Built-in validation schema layers (jakarta.validation), automated parameter binding guarding against traditional SQL Injection vectors, and CORS filtration setups.

---

## 📡 Core API Endpoint Reference

### 🔐 Authentication Gateway
* POST /api/auth/register - Signs up a new customer account.
* POST /api/auth/login - Processes login credentials and yields an explicit JWT access token payload.

### 🛍️ Product Catalog Exploration & Advanced Filtering
* GET /api/perfumes - Extracts the full inventory index.
* GET /api/perfumes/{id} - Isolates a specific product record.
* GET /api/perfumes/search?query={query} - Dynamic search engine scanning brands, descriptions, and name fields.
* GET /api/perfumes/gender/{gender} - Filters out targeting products (MALE, FEMALE, UNISEX).
* GET /api/perfumes/filter?gender={gender}&contentId={contentId} - Multi-criteria specification query extracting products matching both targeted gender and explicit fragrance notes/contents concurrently.
* GET /api/perfumes/best-3 - Custom query sequence returning the top 3 best-selling products based on transaction history and popularity metrics to feed the frontend 'Best Sellers' showcase wrapper.

### 📦 Administrative Operations (ADMIN Authority Required)
* POST /api/perfumes | PUT /api/perfumes/{id} | DELETE /api/perfumes/{id} - Full CRUD operations over catalog items.
* GET /api/users - Extracts a full registry of user files across the platform database.
* GET /api/users/{id} - Get user by ID.
* POST /api/categories | POST /api/contents - Updates baseline filtering taxonomies.

### 🛒 Checkout Workflow
* GET /api/orders - Retrieves historical order records.
* GET /api/orders/{id} - Get order details by ID.
* POST /api/orders - Dispatches an explicit cart model object, generating structural row binds inside orders, order_items, and payments within atomic database transactions.

---

## 👥 Default Credentials

* Default Admin Account:
  * Username: admin
  * Password: admin123

---

## 🧪 Running Tests

./mvnw test

---

## 🎓 Project Origin
This comprehensive solution is engineered, deployed, and compiled as a University Computer Science Graduation Final Project, validating mastery over modern cloud-ready full-stack software development methodologies.