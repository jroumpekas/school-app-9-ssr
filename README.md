# School App 9 SSR

A server-side rendered school management application built with **Java 21**, **Spring Boot**, **Thymeleaf**, **Spring Security**, **Spring Data JPA**, **MySQL**, and **Flyway**.

The project demonstrates a traditional Spring MVC / SSR architecture where the backend handles the application logic, database access, authentication, authorization, validation, and rendering of HTML pages.

> **Status:** Educational project / work in progress

---

## Tech Stack

### Backend
- **Java 21**
- **Spring Boot 3.5.10**
- **Spring MVC**
- **Spring Data JPA / Hibernate**
- **Spring Security**
- **Jakarta Bean Validation**
- **Lombok**

### Frontend
- **Thymeleaf**
- **Thymeleaf Spring Security Extras**
- **HTML / CSS / JavaScript**
- Server-Side Rendering (**SSR**)

### Database
- **MySQL**
- **Flyway** database migrations

### Build & Testing
- **Gradle**
- **JUnit 5 / Spring Boot Test**

---

## Main Features

### Teacher Management
- View teachers in a paginated list
- Insert new teachers
- Edit existing teachers
- Delete teachers using a soft-delete approach
- Retrieve teachers by UUID
- Filter active teachers
- Associate teachers with regions
- Form validation with custom validators
- Success and error handling for CRUD operations

### User Management
- User registration
- DTO-based form handling
- Role selection during registration
- Password encoding with BCrypt
- User persistence through Spring Data JPA

### Authentication & Authorization
- Custom login page
- Spring Security form-based authentication
- Custom authentication success handler
- Custom authentication failure handler
- Custom `UserDetailsService`
- Role-based access control
- Capability / authority-based permissions
- Secure logout with session invalidation

### Database Management
- Version-controlled database schema with Flyway
- Initial database schema creation
- Region seed data
- Teacher soft-delete columns and indexes
- Users, roles, and capabilities
- Role / capability seed data

### Internationalization
The application contains message bundles for multiple languages:

```text
messages.properties
messages_el.properties
```

This allows validation and application messages to be maintained separately from the Java code.

---

## Application Architecture

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

DTOs are used between the web layer and service layer, while mappers handle conversion between DTOs and entities.

### Main package structure

```text
src/main/java/gr/aueb/cf/schoolapp
│
├── authentication
│   ├── CustomAuthenticationFailureHandler.java
│   ├── CustomAuthenticationSuccessHandler.java
│   ├── CustomUserDetailsService.java
│   └── SecurityConfig.java
│
├── controller
│   ├── LoginController.java
│   ├── TeacherController.java
│   └── UserController.java
│
├── core
│   └── exceptions
│
├── dto
│   ├── RegionReadOnlyDTO.java
│   ├── RoleReadOnlyDTO.java
│   ├── TeacherEditDTO.java
│   ├── TeacherInsertDTO.java
│   ├── TeacherReadOnlyDTO.java
│   ├── UserInsertDTO.java
│   └── UserReadOnlyDTO.java
│
├── mapper
│
├── model
│   ├── static_data
│   ├── AbstractEntity.java
│   ├── Capability.java
│   ├── Role.java
│   ├── Teacher.java
│   └── User.java
│
├── repository
│   ├── CapabilityRepository.java
│   ├── RegionRepository.java
│   ├── RoleRepository.java
│   ├── TeacherRepository.java
│   └── UserRepository.java
│
├── service
│   ├── IRegionService.java
│   ├── IRoleService.java
│   ├── ITeacherService.java
│   ├── IUserService.java
│   ├── RegionServiceImpl.java
│   ├── RoleServiceImpl.java
│   ├── TeacherService.java
│   └── UserService.java
│
├── validator
│   ├── TeacherEditValidator.java
│   └── TeacherInsertValidator.java
│
└── SchoolappApplication.java
```

---

## Thymeleaf Templates

The main server-rendered pages are located under:

```text
src/main/resources/templates
```

Current templates include:

```text
templates/
├── fragments/
├── delete-teacher-success.html
├── error.html
├── index.html
├── login.html
├── teacher-edit.html
├── teacher-insert.html
├── teacher-success.html
├── teachers.html
├── user-form.html
└── user-success.html
```

Reusable HTML fragments are kept separately inside the `fragments` directory.

---

## Teacher Workflow

The teacher functionality follows a standard Spring MVC flow.

### List Teachers

```http
GET /teachers
```

Displays a paginated list of active teachers.

### Insert Teacher

```http
GET  /teachers/insert
POST /teachers/insert
```

The form is validated before the teacher is persisted.

After a successful insert, the application follows the **Post / Redirect / Get (PRG)** pattern.

### Edit Teacher

```http
GET  /teachers/edit/{uuid}
POST /teachers/edit
```

Teachers are identified using UUIDs.

### Delete Teacher

```http
POST /teachers/delete/{uuid}
```

Teacher deletion is implemented using a soft-delete approach so that records can remain in the database while being excluded from normal application queries.

---

## User Registration

User registration is available through:

```http
GET  /users/register
POST /users/register
```

The registration process uses:

- `UserInsertDTO`
- Bean Validation
- `IUserService`
- `IRoleService`
- BCrypt password encoding

After successful registration, the application redirects to:

```text
/users/success
```

---

## Security

Security is handled with **Spring Security**.

The application uses server-side sessions and form-based login rather than JWT authentication.

### Public Pages

Examples of publicly accessible routes include:

```text
/
 /login
/users/register
/users/success
/css/**
/js/**
/img/**
/error
```

### Teacher Permissions

Teacher operations use both roles and granular authorities.

Examples include:

```text
INSERT_TEACHER
EDIT_TEACHER
DELETE_TEACHER
```

General teacher access is available to users with roles such as:

```text
ADMIN
EMPLOYEE
```

User administration routes are restricted to the `ADMIN` role.

### Password Security

Passwords are encoded with:

```text
BCryptPasswordEncoder
```

The application also invalidates the HTTP session and removes the `JSESSIONID` cookie during logout.

---

## Database Migrations

Flyway migration files are located in:

```text
src/main/resources/db/migration
```

Current migrations:

```text
V1__initial_schema.sql
V2__insert_regions.sql
V3__alter_teachers_add_soft_delete_columns.sql
V4__teachers_soft_delete_indexes.sql
V5__create_users_roles_capabilities_indexes.sql
V6__insert_roles_capabilites.sql
```

Flyway automatically validates and applies the required migrations when the application starts.

---

## Configuration Profiles

The project contains separate Spring configuration files:

```text
application.properties
application-dev.properties
application-staging.properties
application-prod.properties
```

The default active profile is:

```properties
spring.profiles.active=dev
```

For local development, database configuration can be supplied using environment variables such as:

```text
MYSQL_HOST
MYSQL_PORT
MYSQL_DB
MYSQL_USER
MYSQL_PASSWORD
```

Example:

```properties
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DB:school9ssr}
spring.datasource.username=${MYSQL_USER:your_username}
spring.datasource.password=${MYSQL_PASSWORD:your_password}
```

> Do not commit real database passwords or production credentials to the repository.

---

## Requirements

Before running the application, make sure you have installed:

- **Java 21**
- **MySQL**
- **Git**

Gradle does not need to be installed separately because the project includes the **Gradle Wrapper**.

---

## Local Development

### 1. Clone the repository

```bash
git clone https://github.com/jroumpekas/school-app-9-ssr.git
cd school-app-9-ssr
```

### 2. Create the MySQL database

Example:

```sql
CREATE DATABASE school9ssr;
```

### 3. Configure database credentials

Set the required environment variables.

Example:

```text
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DB=school9ssr
MYSQL_USER=your_username
MYSQL_PASSWORD=your_password
```

### 4. Run the application

#### Windows

```bash
gradlew.bat bootRun
```

#### Linux / macOS

```bash
./gradlew bootRun
```

The application runs by default at:

```text
http://localhost:8080
```

Flyway will apply the required database migrations automatically during startup.

---

## Build

To build the application:

### Windows

```bash
gradlew.bat clean build
```

### Linux / macOS

```bash
./gradlew clean build
```

The generated JAR will be available inside:

```text
build/libs/
```

---

## Running Tests

### Windows

```bash
gradlew.bat test
```

### Linux / macOS

```bash
./gradlew test
```

---

## Deployment

Create the executable Spring Boot JAR:

```bash
./gradlew clean bootJar
```

The packaged application will be generated under:

```text
build/libs/
```

It can then be started with:

```bash
java -jar build/libs/<application-name>.jar --spring.profiles.active=prod
```

For production deployment:

1. Use the `prod` Spring profile.
2. Configure the production MySQL connection through environment variables or external configuration.
3. Never commit production credentials.
4. Run Flyway migrations against the production database.
5. Deploy the generated Spring Boot JAR to a Java-compatible server or hosting environment.

---

## Key Concepts Demonstrated

This project demonstrates practical use of:

- Spring Boot application structure
- MVC architecture
- Server-Side Rendering
- Thymeleaf templates
- Layered architecture
- Spring Data repositories
- JPA / Hibernate entity relationships
- DTO pattern
- Mapper classes
- Dependency Injection
- Form validation
- Custom validation
- Exception handling
- Pagination
- Soft deletion
- UUID-based entity access
- Spring Security
- Roles and authorities
- BCrypt password hashing
- Flyway migrations
- Spring profiles
- Internationalization
- Gradle build management

---

## Possible Future Improvements

- Add more complete user administration pages
- Add teacher search and filtering
- Add automated service and controller tests
- Add integration tests for authentication and authorization
- Improve responsive styling
- Add more detailed validation feedback
- Expand staging and production configuration
- Add Docker support for MySQL and the Spring Boot application
- Add CI/CD with GitHub Actions

---

## Repository

```text
https://github.com/jroumpekas/school-app-9-ssr
```

---

## Author

**Dimitris Roumpekas**

Created as an educational Java / Spring Boot project while practicing server-side rendered web application development and layered application architecture.
