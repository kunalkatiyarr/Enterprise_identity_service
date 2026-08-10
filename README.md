# Enterprise Identity Service

A robust Spring Boot user identity and profile management service modernised with a production-ready DTO layer, validation rules, pagination, dynamic JPA specification search, structured exceptions, and OpenAPI Swagger documentation.

## Core Features
* **Authentication**: Spring Security Form Login with BCrypt password hashing.
* **DTO Separation**: Clean input and output wrappers (`UserRegisterRequest`, `UserUpdateRequest`, `UserResponseDto`, `ApiResponse`) protecting entity security.
* **Validation**: Request body validations checking formatting and duplicate usernames/emails.
* **Dynamic Search**: Paginated lookups filtered using Spring Data JPA specifications.
* **Audit Columns**: Automated `createdAt` and `updatedAt` audit logging on entities.
* **Global Error Handling**: Uniform JSON error formats with proper HTTP mapping.
* **API Documentation**: Automatic Swagger UI dashboard integrations.

---

## API Endpoints

### User REST API (`/api/users`)

| Method | Endpoint | Description | Query Parameters |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/users` | List all users with pagination and sorting | `page`, `size`, `sort` |
| **GET** | `/api/users/{id}` | Retrieve a user profile by database ID | None |
| **POST** | `/api/users` | Register a new user profile with validations | None |
| **PUT** | `/api/users/{id}` | Update details of an existing user profile | None |
| **DELETE** | `/api/users/{id}` | Remove a user profile from the database | None |
| **GET** | `/api/users/search` | Dynamic filtered user search with page/sort | `email`, `name`, `page`, `size`, `sort` |

---

## Running Locally

### Start Application
Run the service locally:
```bash
./mvnw.cmd spring-boot:run
```
The server will boot on port **8080** by default.

### Documentation Dashboards
* **Swagger UI Documentation**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **Raw OpenAPI Json Specs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
* **H2 Database Console**: [http://localhost:8080/sql](http://localhost:8080/sql)
  * *JDBC URL*: `jdbc:h2:file:./.data/h2database`
  * *Username / Password*: `sa` / `sa`

### Run Tests and Code Audits
Validate checkstyle formatters and verify mock unit tests run:
```bash
./mvnw.cmd clean test
```
