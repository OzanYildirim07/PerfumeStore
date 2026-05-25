# LuxPerfume

LuxPerfume is a modern luxury perfume store backend and frontend system built with Spring Boot. It provides a complete e-commerce platform for perfume sales with JWT authentication, role-based access control, and a responsive web interface.

## Technologies Used

### Backend
- Java 17
- Spring Boot 3.3.0
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- ModelMapper
- Swagger/OpenAPI

### Frontend
- HTML5, CSS3, JavaScript
- Responsive Design
- REST API Integration

## Prerequisites

- Java 17 or higher
- Docker Desktop
- Maven (or use included wrapper)

## Setup Instructions

### 1. Start PostgreSQL with Docker

```bash
docker compose up -d
```

### 2. Build the Project

```bash
./mvnw clean package -DskipTests
```

On Windows:
```cmd
mvnw.cmd clean package -DskipTests
```

### 3. Run the Application

```bash
java -jar target/LuxPerfume-1.0.0.jar
```

### 4. Access the Application

- Web UI: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

## API Endpoints

### Authentication
- POST /api/auth/register - Register new user
- POST /api/auth/login - Login and get JWT token

### Perfumes
- GET /api/perfumes - List all perfumes
- GET /api/perfumes/{id} - Get perfume by ID
- GET /api/perfumes/search?query={query} - Search perfumes
- GET /api/perfumes/gender/{gender} - Filter by gender
- POST /api/perfumes - Create perfume (ADMIN only)
- PUT /api/perfumes/{id} - Update perfume (ADMIN only)
- DELETE /api/perfumes/{id} - Delete perfume (ADMIN only)

### Users
- GET /api/users - List all users (ADMIN only)
- GET /api/users/{id} - Get user by ID

### Orders
- GET /api/orders - List orders
- POST /api/orders - Create order
- GET /api/orders/{id} - Get order by ID

### Categories
- GET /api/categories - List all categories
- POST /api/categories - Create category (ADMIN only)

### Contents
- GET /api/contents - List all contents
- POST /api/contents - Create content (ADMIN only)

## Role Structure

- **ADMIN**: Full access to all endpoints (POST, PUT, DELETE)
- **USER**: Read-only access (GET endpoints)

## JWT Usage

1. Register or login to receive a JWT token
2. Include the token in the Authorization header:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

## Default Admin Account

- Username: admin
- Password: admin123

## Database Schema

- **users**: User accounts with roles
- **perfumes**: Perfume products with categories and contents
- **categories**: Product categories
- **contents**: Fragrance notes/content types
- **orders**: Customer orders
- **order_items**: Order line items
- **payments**: Payment records

## Running Tests

```bash
./mvnw test
```

## Security Features

- JWT-based authentication
- Password encryption with BCrypt
- Role-based access control (RBAC)
- CSRF protection disabled for API endpoints
- SQL injection prevention via parameterized queries
- XSS protection through input validation

## License

This project is developed as a university computer science final project.
