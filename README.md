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




