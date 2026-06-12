# ecommerce-api

REST API for e-commerce built with Java 21, Quarkus 3, PostgreSQL and OpenAPI documentation.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Quarkus 3.36.2 |
| ORM | Hibernate ORM + Panache |
| Database | PostgreSQL 16 |
| Documentation | Swagger UI / OpenAPI 3 |
| Build | Maven |
| Tests | QuarkusTest + REST Assured |
| Containers | Docker + Docker Compose |

## Project Structure

```text
ecommerce-api/
├── src/
│   ├── main/
│   │   ├── java/com/michael/ecommerce/
│   │   │   ├── entity/       # JPA models
│   │   │   ├── dto/          # Request/Response objects
│   │   │   ├── service/      # Business logic
│   │   │   ├── resource/     # REST endpoints
│   │   │   └── exception/    # Error handling
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── docker-compose.yml
└── README.md
```

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- Docker + Docker Compose

### Run in development mode

```bash
docker-compose up -d
mvn quarkus:dev
```

API available at: `http://localhost:8080`

Swagger UI available at: `http://localhost:8080/q/swagger-ui`

## API Endpoints

| Resource | Endpoint |
|---|---|
| Categories | `/api/categories` |
| Products | `/api/products` |
| Customers | `/api/clients` |
| Orders | `/api/orders` |

## Development Phases

- [x] Phase 1 — Project setup, GitHub, Docker, first entity (Category)
- [ ] Phase 2 — Products with category relationship
- [ ] Phase 3 — Customers and Orders with business logic
- [ ] Phase 4 — Tests, error handling, final documentation
