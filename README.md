# microservices-project/README.md

# Microservices Project

This project consists of two microservices built with Spring Boot 3.3, named RH and RM. Both services are containerized using Docker and are configured to work with a PostgreSQL database. The project also includes Swagger for API documentation and provides CRUD operations for both microservices.

## Project Structure

```
microservices-project
├── docker-compose.yml
├── rh-service
│   ├── Dockerfile
│   ├── pom.xml
│   └── src
│       └── main
│           └── java
│               └── com
│                   └── example
│                       └── rh
│                           ├── RhApplication.java
│                           ├── controller
│                           │   └── RhController.java
│                           ├── model
│                           │   └── RhEntity.java
│                           ├── repository
│                           │   └── RhRepository.java
│                           └── service
│                               └── RhService.java
│           └── resources
│               └── application.yml
└── rm-service
    ├── Dockerfile
    ├── pom.xml
    └── src
        └── main
            └── java
                └── com
                    └── example
                        └── rm
                            ├── RmApplication.java
                            ├── controller  
                            │   └── RmController.java
                            ├── model
                            │   └── RmEntity.java
                            ├── repository
                            │   └── RmRepository.java
                            └── service
                                └── RmService.java
            └── resources
                └── application.yml
```

## Getting Started

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd microservices-project
   ```

2. **Build and run the services**:
   ```bash
   docker-compose up --build
   ```

3. **Access the APIs**:
   - RH Service: `http://localhost:8081/api/rh`
   - RM Service: `http://localhost:8082/api/rm`

4. **Swagger Documentation**:
   - RH Service: `http://localhost:8081/swagger-ui.html`
   - RM Service: `http://localhost:8082/swagger-ui.html`

## Technologies Used

- Spring Boot 3.3
- Docker
- PostgreSQL
- Swagger

## License

This project is licensed under the MIT License.