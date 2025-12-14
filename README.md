🍬 Sweet Shop Management System – Backend

A production-ready backend for managing a sweet shop, built using Spring Boot, JWT authentication, role-based authorization, and MySQL, with cloud storage support via AWS S3.
The Sweet Shop Management System Backend is a secure, production-ready REST API built with Spring Boot that handles user authentication, sweet inventory management, and dashboard
analytics. It uses JWT-based authentication with refresh tokens, role-based authorization, MySQL for data persistence, and AWS S3 for image storage, and is designed to integrate 
seamlessly with a React frontend deployed in production.

This backend exposes secure REST APIs consumed by a React (Vite) frontend deployed on Netlify.

🚀 Tech Stack

Java 17

Spring Boot

Spring Web MVC

Spring Security (JWT-based)

Spring Data JPA (Hibernate)

MySQL (AWS RDS compatible)

AWS S3 (Image upload)

JWT Authentication + Refresh Tokens

Maven

JUnit 5 & Mockito (Testing)

Docker (Deployment)

Render (Production hosting)


📐 Architecture Overview

Controller Layer – Handles HTTP requests

Service Layer – Business logic

Repository Layer – Data persistence (JPA)

Security Layer

JWT Filter

Role-based access control

Cloud Integration

AWS S3 for image storage

🔐 Authentication & Authorization

JWT Access Token

Refresh Token flow

Roles

ROLE_ADMIN

ROLE_USER

Secured Endpoints
| Endpoint               | Access       |
| ---------------------- | ------------ |
| `/api/auth/**`         | Public       |
| `/api/sweets/add`      | ADMIN        |
| `/api/sweets/update`   | ADMIN        |
| `/api/sweets/delete`   | ADMIN        |
| `/api/sweets/purchase` | USER / ADMIN |
| `/api/dashboard/**`    | ADMIN        |


📦 Core Features

User Registration & Login

JWT + Refresh Token Authentication

Sweet CRUD Operations

Inventory Management (Purchase & Restock)

Dashboard Analytics

Image Upload to AWS S3

Production-grade CORS configuration

Dockerized deployment


🧪 Testing Strategy

Unit Tests

Service layer fully tested

Mockito used for dependency isolation

SecurityContext mocked where required

Coverage

AuthService

SweetService

InventoryService

RefreshTokenService

DashboardService

ImageService

Tests follow Red → Green → Refactor methodology.


⚙️ Environment Configuration

All sensitive values are injected via environment variables.

```
DB_URL=jdbc:mysql://<host>:3306/sweetshop
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password

JWT_SECRET_KEY=your_jwt_secret
JWT_EXPIRATION=7200000
JWT_REFRESH_EXPIRATION=604800000

AWS_ACCESS_KEY=your_access_key
AWS_SECRET_KEY=your_secret_key
AWS_REGION=ap-south-1
AWS_BUCKET=your_bucket_name
```

🌐 CORS Configuration

CORS is configured at the Spring Security layer to allow requests only from the deployed frontend, ensuring:

Secure cross-origin access

JWT Authorization headers allowed

Preflight requests handled correctly

🐳 Docker Support

The application is fully Dockerized and ready for deployment on Render.

```
docker build -t sweet-shop-backend .
docker run -p 8080:8080 sweet-shop-backend
```

📁 Project Structure
```
src/main/java
├── controller
├── service
├── repository
├── entity
├── security
├── config
└── exception
```

🤖 My AI Usage
AI Tools Used

ChatGPT
GitHub Copilot

How I Used AI

I used ChatGPT as a development assistant, not as an auto-code generator.
Specifically, I used it to:

Clarify Spring Security & JWT flow

Design refresh token logic

Improve test coverage using Mockito

Debug CORS and deployment issues

Refine commit messages to follow professional standards

Validate Docker + Render deployment steps

Review code for production best practices

All AI-generated suggestions were manually reviewed, adapted, and integrated into the codebase.

Reflection on AI Impact

AI significantly improved my development speed and code quality, especially during:

Writing unit tests for security-dependent services

Debugging production-only issues (Linux case sensitivity, CORS)

Structuring clean commit history and documentation

However, all architectural decisions, security considerations, and final implementations were made by me.
AI acted as a pair-programming and learning tool, not a replacement for understanding.








