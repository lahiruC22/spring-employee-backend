# Employee Management API

## Project Description

This project is a robust and secure RESTful API built with Spring Boot for managing employee data. It provides standard Create, Read, Update, and Delete (CRUD) operations for employees. The API is designed with scalability, maintainability, and security as core principles, making it suitable for integration into larger enterprise systems.

**Key Features:**

* **CRUD Operations:** Full support for managing employee records (Create, Read, Update, Delete).
* **Data Transfer Objects (DTOs):** Uses DTOs for clear data representation and to protect internal domain models.
* **Role-Based Access Control (RBAC):** Implements fine-grained access control using Spring Security's `@PreAuthorize` annotation based on user roles (e.g., USER, ADMIN).
* **JWT Authentication:** Secures API endpoints using JSON Web Tokens (JWT) for stateless authentication.
* **Authentication Endpoints:** Provides dedicated endpoints for user registration and login to obtain JWTs.
* **Error Handling:** Implements robust error handling and validation for API requests.
* **API Documentation:** Generates interactive API documentation using Springdoc OpenAPI (Swagger UI).
* **Testing:** Includes (Only add Employee Service and Controller class unit tests) unit and integration tests to ensure code quality and reliability.

## Setup Instructions

### Prerequisites

* Java 21 or higher
* Maven 3.6 or higher
* An IDE like IntelliJ IDEA or Eclipse
* A database (Neon serverless PostgreSQL db)

### Running the Application

1.  **Clone the repository:**
    ```bash
    git clone <repository_url>
    cd employee-management-api
    ```
2.  **Configure the Database:**
    * Update the database connection properties in `src/main/resources/application.properties` (or `application.yml`).
    * Example for PostgreSQL:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/employeedb
        spring.datasource.username=your_username
        spring.datasource.password=your_password
        spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
        spring.jpa.hibernate.ddl-auto=update
        ```
3.  **Configure JWT:**
    * Add a JWT secret key in `application.properties`:
        ```properties
        jwt.secret=<your_secret_key_here> # Use a strong, unique key
        jwt.expiration.ms=86400000 # Token validity in milliseconds (e.g., 24 hours)
        ```
4.  **Build the project:**
    ```bash
    mvn clean install
    ```
5.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    The application will start on port 8080 by default (or the port configured in `application.properties`).

## Architecture Overview

The application follows a standard layered architecture:

* **Controller Layer:** Handles incoming HTTP requests, maps them to appropriate service methods, and returns HTTP responses. Includes validation and utilizes DTOs. (`EmployeeController`, `AuthController`)
* **Service Layer:** Contains the business logic, orchestrates operations, and interacts with the data access layer. (`EmployeeService`, `UserService`)
* **Data Access Layer (Repository Layer):** Interacts directly with the database to perform CRUD operations on entities. (e.g., `EmployeeRepository`, `UserRepository`, `RoleRepository`)
* **Security Layer:** Implemented using Spring Security for authentication and authorization. Includes JWT handling, user details management, and role-based access control. (`SecurityConfig`, `JwtUtils`, `UserDetailsImpl`, `AuthEntryPointJwt`, `AuthTokenFilter`)
* **Model Layer:** Represents the application's data structure (JPA Entities like `Employee`, `User`, `Role`).
* **DTO Layer:** Data Transfer Objects used for communication between the client and the controller layer. (`EmployeeDTO`, `CreateEmployeeDTO`, `UpdateEmployeeDTO`, `LoginRequest`, `LoginResponse`, `RegisterRequest`)
* **Mapper Layer:** Converts between Model entities and DTOs. (`EmployeeMapper`)

## Key Components

### Data Model

* **`Employee`**: Entity representing an employee with relevant fields (e.g., id, name, position, etc.).
* **`User`**: Entity representing an application user, linked to roles. Contains security-related fields like username, email, and password hash. **(Requires Implementation)**
* **`Role`**: Entity representing user roles (e.g., ADMIN, USER). **(Requires Implementation)**
* **`ERole`**: Enum defining the available roles in the system (`ROLE_USER`, `ROLE_ADMIN`). **(Requires Implementation)**

### Security Components

* **`SecurityConfig`**: Configures Spring Security, disabling CSRF, setting session management to stateless, defining public and protected endpoints, and integrating the JWT filter.
* **`UserService` / `UserDetailsService`**: Service responsible for loading user details (username, password, roles) from the database during the authentication process. You will likely have an implementation of Spring Security's `UserDetailsService`. **(Requires Implementation)**
* **`UserDetailsImpl`**: A custom implementation of Spring Security's `UserDetails` interface. It wraps your `User` entity and provides the necessary methods for Spring Security to manage user details (username, password, authorities/roles). **(Requires Implementation)**
* **`JwtUtils`**: A utility class responsible for generating JWT tokens during login and validating incoming JWT tokens from client requests. **(Requires Implementation)**
* **`PasswordEncoder`**: An interface used for encoding user passwords before storing them in the database and verifying passwords during login. You should configure a bean for a strong password encoder like `BCryptPasswordEncoder`. **(Requires Configuration/Implementation of Bean)**
* **`AuthenticationManager`**: A core Spring Security component used in the `AuthController` to perform the actual authentication process (verifying username/password). **(Requires Configuration)**
* **`AuthEntryPointJwt`**: Handles authentication errors for unauthorized access to protected resources. **(Requires Implementation)**
* **`AuthTokenFilter`**: A custom filter that intercepts incoming requests, extracts the JWT from the `Authorization` header, validates it, and sets the authenticated user in Spring Security's `SecurityContext`. **(Requires Implementation)**

### Authentication Endpoints (`AuthController`)

* **`POST /api/auth/register`**: Allows new users to register. Requires a request body containing username, email, password, and optionally roles. **(Requires Implementation of Logic)**
* **`POST /api/auth/login`**: Allows registered users to log in. Requires a request body containing username/email and password. Returns a JWT upon successful authentication. **(Requires Implementation of Logic)**

### API Endpoints (`EmployeeController`)

* **`GET /api/employees`**: Get all employees. Requires authentication (`ROLE_USER` or `ROLE_ADMIN`).
* **`GET /api/employees/{id}`**: Get employee by ID. Requires authentication (`ROLE_USER` or `ROLE_ADMIN`).
* **`POST /api/employees`**: Create a new employee. Requires authentication (`ROLE_ADMIN`).
* **`PUT /api/employees/{id}`**: Update an existing employee. Requires authentication (`ROLE_ADMIN`).
* **`DELETE /api/employees/{id}`**: Delete an employee. Requires authentication (`ROLE_ADMIN`).

### API Documentation (Swagger UI)

* API documentation is generated using Springdoc OpenAPI and is accessible via Swagger UI.
* Access the Swagger UI at: `http://localhost:8080/swagger-ui.html`

## Technologies Used

* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* JWT (e.g., `jjwt`)
* Springdoc OpenAPI (Swagger UI)
* Databasea (PostgreSQL)
* Maven

## License

MIT License
