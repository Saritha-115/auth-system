# Auth System

A full-stack authentication system built with Spring Boot microservices, Spring Cloud Gateway, and React. Fully containerized with Docker.

---

## Architecture

```
                        ┌─────────────────┐
                        │   React Frontend │
                        │  localhost:3000  │
                        └────────┬────────┘
                                 │
                        ┌────────▼────────┐
                        │   API Gateway   │  ← JWT Auth Filter
                        │  localhost:8080  │  ← Redis Rate Limiter
                        └────────┬────────┘
                 ┌───────────────┼───────────────┐
        ┌────────▼────────┐              ┌────────▼────────┐
        │  Auth Service   │              │  User Service   │
        │  localhost:8081  │              │  localhost:8082  │
        └────────┬────────┘              └────────┬────────┘
                 └───────────────┬───────────────┘
                        ┌────────▼────────┐
                        │   PostgreSQL    │
                        │  localhost:5432  │
                        └─────────────────┘
```

### Services

| Service | Port | Description |
|---|---|---|
| React Frontend | 3000 | Login, Register, Dashboard UI |
| API Gateway | 8080 | Routes requests, JWT validation, rate limiting |
| Auth Service | 8081 | Login, token generation and validation |
| User Service | 8082 | User registration and management |
| PostgreSQL | 5432 | Persistent database |
| Redis | 6379 | Rate limiter storage |

---

## Features

- JWT-based authentication
- Redis rate limiting on auth endpoints (5 requests/second per IP)
- Password hashing with BCrypt (strength 12)
- Request routing via Spring Cloud Gateway
- CORS configured for frontend origin
- Health checks on all services
- Fully dockerized with proper startup ordering

---

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running
- Git

---

## Getting Started

**1. Clone the repository**

```bash
git clone <your-repo-url>
cd auth-system
```

**2. Make sure Docker Desktop is running**

Look for the whale icon in your taskbar. If it's not running, open Docker Desktop and wait for it to show "Engine running".

**3. Start all services**

```bash
docker-compose up --build
```

This will build all images and start every service in the correct order. First run takes 3-5 minutes.

**4. Open the app**

```
http://localhost:3000
```

**5. To stop everything**

```bash
docker-compose down
```

**To stop and delete all data (fresh start)**

```bash
docker-compose down -v
```

---

## API Endpoints

### Auth Service — `/auth/**`

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| POST | `/auth/login` | No | Login with username and password |
| POST | `/auth/validate` | No | Validate a JWT token |
| GET | `/auth/health` | No | Health check |

**Login request body:**
```json
{
  "username": "your_username",
  "password": "your_password"
}
```

**Login response:**
```json
{
  "token": "eyJhbGci...",
  "tokenType": "Bearer",
  "userId": 1,
  "username": "your_username",
  "email": "your@email.com",
  "role": "ROLE_USER"
}
```

---

### User Service — `/users/**`

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| POST | `/users/register` | No | Register a new user |
| GET | `/users/{id}` | Yes | Get user by ID |
| PUT | `/users/{id}` | Yes | Update user |
| DELETE | `/users/{id}` | Yes | Delete user |
| GET | `/users/health` | No | Health check |

**Register request body:**
```json
{
  "username": "your_username",
  "email": "your@email.com",
  "password": "yourpassword",
  "firstName": "First",
  "lastName": "Last"
}
```

---

## Project Structure

```
auth-system/
├── api-gateway/
│   ├── src/main/java/com/example/api_gateway/
│   │   ├── config/
│   │   │   └── RateLimiterConfig.java
│   │   └── filter/
│   │       └── JwtAuthFilter.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── Dockerfile
│
├── auth-service/
│   ├── src/main/java/com/example/auth_service/
│   │   ├── config/SecurityConfig.java
│   │   ├── controller/AuthController.java
│   │   ├── dto/AuthDtos.java
│   │   ├── model/User.java
│   │   ├── repository/UserRepository.java
│   │   ├── security/JwtUtil.java
│   │   └── service/AuthService.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── Dockerfile
│
├── user-service/
│   ├── src/main/java/com/example/user_service/
│   │   ├── config/SecurityConfig.java
│   │   ├── controller/UserController.java
│   │   ├── dto/UserDtos.java
│   │   ├── model/User.java
│   │   ├── repository/UserRepository.java
│   │   └── service/UserService.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── Dockerfile
│
├── react-frontend/
│   ├── src/
│   │   ├── components/ProtectedRoute.jsx
│   │   ├── context/
│   │   │   ├── AuthContext.js
│   │   │   ├── AuthProvider.jsx
│   │   │   └── useAuth.js
│   │   ├── pages/
│   │   │   ├── LoginPage.jsx
│   │   │   ├── RegisterPage.jsx
│   │   │   └── DashboardPage.jsx
│   │   └── services/api.js
│   ├── nginx.conf
│   └── Dockerfile
│
├── init.sql
├── docker-compose.yml
└── README.md
```

---

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.5
- Spring Cloud Gateway 4.3
- Spring Security 6.5
- Spring Data JPA
- Hibernate 6.6
- jjwt 0.11.5
- PostgreSQL 15
- Redis 7
- Lombok

### Frontend
- React 19
- React Router 7
- Axios
- Vite 8

### Infrastructure
- Docker
- Docker Compose
- Nginx (serving React build)

---

## Environment Variables

| Variable | Service | Default | Description |
|---|---|---|---|
| `JWT_SECRET` | gateway, auth | `mySecretKey123456789012345678901234567890` | Secret key for signing JWT tokens |
| `JWT_EXPIRATION` | auth | `86400000` | Token expiry in milliseconds (24 hours) |
| `SPRING_DATASOURCE_URL` | auth, user | `jdbc:postgresql://postgres-db:5432/authdb` | Database connection URL |
| `SPRING_DATASOURCE_USERNAME` | auth, user | `authuser` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | auth, user | `authpass` | Database password |
| `SPRING_REDIS_HOST` | gateway | `redis` | Redis hostname |
| `SPRING_REDIS_PORT` | gateway | `6379` | Redis port |

To override any default, create a `.env` file in the project root:

```env
JWT_SECRET=your_strong_secret_key_here
```

---

## Database Schema

```sql
CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(50)  UNIQUE NOT NULL,
    email       VARCHAR(100) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    first_name  VARCHAR(50),
    last_name   VARCHAR(50),
    role        VARCHAR(20)  NOT NULL DEFAULT 'ROLE_USER',
    enabled     BOOLEAN      NOT NULL DEFAULT true,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);
```

---

## Troubleshooting

**Docker daemon not running**
```
failed to connect to the docker API
```
Open Docker Desktop and wait for it to fully start before running docker-compose commands.

**Services failing to connect to database**
```
Connection refused to postgres-db
```
Run `docker-compose down -v` then `docker-compose up --build` again. The `-v` flag clears old volumes for a clean start.

**Port already in use**
```
Bind for 0.0.0.0:8080 failed: port is already allocated
```
Something else is using that port. Run `docker-compose down` first, or change the port mapping in `docker-compose.yml`.

**Network Error in frontend despite backend working**
Check the browser DevTools Network tab. If you see duplicate `Access-Control-Allow-Origin` headers in the response, the gateway CORS deduplication filter needs to be applied to that route.

---

## License

MIT