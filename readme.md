# Inventory & Order Management Application

This project is a sample Java Spring Boot application for managing Items, Inventories, and Orders using H2 Database.

## Specifications

- **Java Version:** 17
- **Spring Boot Version:** 3.5.4
- **Build Tool:** Maven (with Maven Wrapper)

## Prerequisites

- JDK 17 installed and `JAVA_HOME` set
- (Optional) Maven installed, otherwise use Maven Wrapper

## Running the Application

1. Open a terminal in the project root directory.
2. Start the application using Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The application will start on `http://localhost:8080`.
4. Access the H2 Console at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`).

## Project Structure

- `src/main/java` - Application source code

  - `com.example.demo.controller` - REST Controllers
  - `com.example.demo.service` - Service layer
  - `com.example.demo.model` - JPA Entities
  - `com.example.demo.repository` - Spring Data JPA Repositories
  - `com.example.demo.dto` - Request/Response DTOs
  - `com.example.demo.exception` - Custom Exceptions & Handler

- `src/test/java` - Unit tests

  - **Test Package:** `com.obs.test`

## Running Tests

To execute all unit tests, run:

```bash
./mvnw clean test -e
```

- Tests are located in the `com.obs.test` package.
- The `-e` flag enables full error output for debugging.

## Common Commands

| Command                  | Description                             |
| ------------------------ | --------------------------------------- |
| `./mvnw spring-boot:run` | Run the Spring Boot application         |
| `./mvnw clean test -e`   | Run all unit tests with detailed errors |

## Notes

- Use Java 17 and Spring Boot 3.5.4 to ensure compatibility.
- The application uses an in-memory H2 Database for development and testing.

