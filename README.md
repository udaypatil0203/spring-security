# Spring Security Hospital Management API

A Spring Boot REST API for managing hospital data such as patients, doctors, appointments and insurance, with authentication and authorization implemented using Spring Security, JWT and OAuth 2.0 login.

## Overview

This project demonstrates how Spring Security can be integrated into a real-world style hospital management application. It includes:

- User signup and login
- Stateless JWT-based authentication
- Custom `UserDetailsService`
- Password hashing with `PasswordEncoder`
- JWT authentication filter
- Role-based method-level authorization
- OAuth 2.0 login with Google and GitHub
- Patient management
- Doctor management
- Appointment management
- Insurance assignment
- JPA/Hibernate persistence with MySQL
- DTO-based API responses
- Global exception handling
- Pagination for patient listing

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 4.1.0 | Application framework |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Database access |
| Hibernate | ORM |
| MySQL | Relational database |
| JWT (JJWT 0.13.0) | Token-based authentication |
| OAuth 2.0 | Google and GitHub authentication |
| ModelMapper | Entity-to-DTO mapping |
| Lombok | Boilerplate reduction |
| Maven | Dependency management and build |

## Security

### JWT Authentication

The application uses stateless JWT authentication.

The login flow is:

```text
Client
  |
  | username + password
  v
AuthController
  |
  v
AuthService
  |
  v
AuthenticationManager
  |
  v
CustomUserDetailService
  |
  v
UserRepository / Database
  |
  v
PasswordEncoder
  |
  v
Authenticated User
  |
  v
JWT generated
  |
  v
Client receives access token
```

For protected requests, the JWT is sent in the HTTP `Authorization` header:

```text
Authorization: Bearer <JWT>
```

`JwtAuthFilter` validates the token and establishes the authenticated user in Spring Security's `SecurityContext`.

The application is configured as stateless, so it does not maintain server-side HTTP sessions for authentication.

### OAuth 2.0 Login

OAuth 2.0 login is configured for:

- Google
- GitHub

After successful OAuth authentication, `OAuth2SuccessHandler` delegates to the authentication service. The application either finds an existing user or creates one and then generates a JWT access token.

## Authorization

The main security configuration:

- Disables CSRF because the API is stateless.
- Uses `SessionCreationPolicy.STATELESS`.
- Allows `/public/**` and `/auth/**` without authentication.
- Requires authentication for other requests.
- Adds the JWT filter before `UsernamePasswordAuthenticationFilter`.

Method-level authorization is also demonstrated. For example, appointment creation is restricted with:

```java
@Secured("ROLE_PATIENT")
```

## Authentication Endpoints

Base application context:

```text
/api/v1
```

### Signup

```http
POST /api/v1/auth/signup
```

Example request:

```json
{
  "username": "user@example.com",
  "password": "your-password"
}
```

### Login

```http
POST /api/v1/auth/login
```

Example request:

```json
{
  "username": "user@example.com",
  "password": "your-password"
}
```

A successful login returns a JWT access token and the authenticated user's ID.

## Main Domain Model

The project contains the following entities:

```text
User
Patient
Doctor
Appointment
Insurance
Department
```

Supporting enums include:

```text
AuthProviderType
BloodGroupType
```

The application models relationships between patients, doctors, appointments and insurance using JPA/Hibernate.

## Project Structure

```text
src/main/java/com/codingShuttle/com/SpringSecurity/
│
├── Controller/
│   ├── AdminController.java
│   ├── AuthController.java
│   ├── DoctorController.java
│   ├── HospitalController.java
│   └── PatientController.java
│
├── config/
│   └── AppConfig.java
│
├── dto/
│   ├── AppointmentResponseDto.java
│   ├── BloodGroupCountResponseEntity.java
│   ├── CreateAppointmentRequestDto.java
│   ├── DoctorResponseDto.java
│   ├── LoginRequestDto.java
│   ├── LoginResponseDto.java
│   ├── OnboardDoctorRequestDto.java
│   ├── PatientResponseDto.java
│   └── SignupResponseDto.java
│
├── entity/
│   ├── Appointment.java
│   ├── Department.java
│   ├── Doctor.java
│   ├── Insurance.java
│   ├── Patient.java
│   ├── User.java
│   └── type/
│       ├── AuthProviderType.java
│       └── BloodGroupType.java
│
├── error/
│   ├── ApiError.java
│   └── GlobalExceptionHandler.java
│
├── repository/
│   ├── AppointmentRepository.java
│   ├── DepartmentRepository.java
│   ├── DoctorRepository.java
│   ├── InsuranceRepository.java
│   ├── PatientRepository.java
│   └── UserRepository.java
│
├── security/
│   ├── AuthService.java
│   ├── AuthUtil.java
│   ├── CustomUserDetailService.java
│   ├── JwtAuthFilter.java
│   ├── OAuth2SuccessHandler.java
│   └── WebSecurityConfig.java
│
└── service/
    ├── AppointmentService.java
    ├── DoctorService.java
    ├── InsuranceService.java
    └── PatientService.java
```

## Database

The application uses MySQL.

The schema is managed through JPA/Hibernate, while `data.sql` contains sample hospital data for patients, doctors and appointments.

The repository does **not** contain an actual MySQL database. A local MySQL server/database must be configured before running the application.

## Configuration and Environment Variables

Sensitive values are intentionally read from environment variables.

`application.properties` uses:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secretKey=${JWT_TOKEN}
```

OAuth 2.0 credentials are configured in `application.yml`:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
          github:
            client-id: ${GITHUB_CLIENT_ID}
            client-secret: ${GITHUB_CLIENT_SECRET}
```

### Required environment variables

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_TOKEN
GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET
GITHUB_CLIENT_ID
GITHUB_CLIENT_SECRET
```

**Never commit the actual values of these variables to GitHub.**

For local Eclipse development, they can be configured under:

```text
Run Configurations
→ Spring Boot App
→ Environment
```

## Getting Started

### Prerequisites

Install:

- Java 21
- Maven (optional because the project includes Maven Wrapper)
- MySQL
- Git

### 1. Clone the repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd SpringSecurity
```

### 2. Create the MySQL database

Create a database named:

```sql
CREATE DATABASE hospitalDB;
```

### 3. Configure environment variables

Set the required variables listed above.

For a local MySQL setup, for example:

```text
DB_URL=jdbc:mysql://localhost:3306/hospitalDB
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
JWT_TOKEN=your_jwt_secret
```

Add Google and GitHub OAuth credentials only if you want to use OAuth login.

### 4. Run the application

Using Maven Wrapper on Windows:

```bash
mvnw.cmd spring-boot:run
```

Or on Linux/macOS:

```bash
./mvnw spring-boot:run
```

You can also run the `SpringSecurityApplication` class from Eclipse.

The application context path is:

```text
/api/v1
```

## Sample Data

The repository includes `src/main/resources/data.sql` with sample patients, doctors and appointments.

The application is currently configured with:

```properties
spring.jpa.hibernate.ddl-auto=create
```

This is suitable for a learning/demo project, but it should be changed for production because the database schema is recreated when the application starts.

## API Usage

For protected APIs, first authenticate using `/auth/login` and copy the returned JWT.

Then send it with subsequent requests:

```http
Authorization: Bearer <your-jwt-token>
```

Public authentication endpoints:

```text
POST /api/v1/auth/signup
POST /api/v1/auth/login
```

OAuth login entry points are provided by Spring Security for the configured Google and GitHub registrations.

## Error Handling

The project contains:

```text
ApiError
GlobalExceptionHandler
```

to provide centralized API error handling for application exceptions.

## Notes

- The project is intended primarily as a learning/demo project for Spring Security and backend development.
- OAuth credentials and database passwords should be supplied through environment variables.
- Do not commit `.env` files or real credentials.
- `target/` and IDE-specific files should not be committed to GitHub.
- For a production application, consider using database migrations such as Flyway or Liquibase and a safer schema management strategy.

## Author

**Uday Patil**

GitHub: `<YOUR_GITHUB_PROFILE_URL>`

---

## License

This project is currently provided for learning and portfolio purposes.
