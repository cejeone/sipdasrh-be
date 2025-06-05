# Contents of /microservices-project/microservices-project/rm-service/README.md

# RM Service

This is the RM microservice of the microservices project. It is built using Spring Boot 3.3 and is designed to handle CRUD operations for the RM entity.

## Features

- CRUD API for managing RM entities
- Swagger documentation for API endpoints
- PostgreSQL database integration
- Docker support for easy deployment

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Docker (for containerization)

### Installation

1. Clone the repository:
   ```
   git clone <repository-url>
   ```

2. Navigate to the `rm-service` directory:
   ```
   cd microservices-project/rm-service
   ```

3. Build the project using Maven:
   ```
   mvn clean install
   ```

### Running the Service

You can run the RM service using Docker Compose. Make sure you have Docker installed and running, then execute the following command from the root of the project:

```
docker-compose up
```

### API Documentation

The API documentation is available through Swagger. Once the service is running, you can access it at:

```
http://localhost:8080/swagger-ui.html
```

## License

This project is licensed under the MIT License. See the LICENSE file for details.