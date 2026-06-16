# ecommerce-api

A RESTful API for e-commerce management built with Java 21 and Quarkus 3, featuring full CRUD operations, business logic validation, OpenAPI documentation, and integration testing.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Quarkus 3.36.2 |
| ORM | Hibernate ORM + Panache |
| Database | PostgreSQL 16 |
| Documentation | Swagger UI / OpenAPI 3.1 |
| Build | Maven 3.9 |
| Tests | QuarkusTest + REST Assured |
| Containers | Docker + Docker Compose |

## Project Structure

```
ecommerce-api/
├── src/
│   ├── main/
│   │   ├── java/com/michael/ecommerce/
│   │   │   ├── entity/         # JPA models (Category, Product, Customer, Order, OrderItem)
│   │   │   ├── dto/            # Request and Response objects
│   │   │   ├── service/        # Business logic layer
│   │   │   ├── resource/       # REST endpoints
│   │   │   └── exception/      # Global error handling
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/michael/ecommerce/
│           └── OrderFlowTest.java
├── docs/
│   ├── learning-journal.md     # Java + Quarkus learning notes (ES)
│   └── definition-of-done.md  # Project DoD
├── docker-compose.yml
└── README.md
```

## Domain Model

```
Category (1) ──────── (∞) Product
Customer (1) ──────── (∞) Order (1) ──────── (∞) OrderItem (∞) ──────── (1) Product
```

## API Endpoints

### Categories
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/categories` | List all categories |
| GET | `/api/categories/{id}` | Get category by ID |
| POST | `/api/categories` | Create category |
| PUT | `/api/categories/{id}` | Update category |
| DELETE | `/api/categories/{id}` | Soft delete category |

### Products
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/products` | List all products (optional `?categoryId=`) |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Soft delete product |

### Customers
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/customers` | List all customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| POST | `/api/customers` | Create customer |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Soft delete customer |

### Orders
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/orders` | List all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| POST | `/api/orders` | Create order |
| PATCH | `/api/orders/{id}/status` | Update order status |
| DELETE | `/api/orders/{id}` | Soft delete order |

## Business Rules

- Category names must be unique (case-insensitive)
- Customer emails must be unique (case-insensitive)
- Product prices must be greater than 0
- Order items must have a quantity of at least 1
- Stock is validated before creating an order — insufficient stock returns `409 Conflict`
- Unit price is captured from the product at order time, not from the request
- Order total is calculated server-side: `sum(quantity × unitPrice)` per item
- Order status transitions: `PENDING → PAID → SHIPPED → DELIVERED`
- All deletes are soft deletes (`active = false`) — data is never permanently removed

## Error Responses

All errors follow a consistent structure:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Product with id 99 not found",
  "timestamp": "2026-06-15T21:00:00"
}
```

| Status | Meaning |
|---|---|
| `400` | Validation error (missing required field, invalid format) |
| `404` | Resource not found |
| `409` | Business rule violation (duplicate name, insufficient stock) |
| `500` | Unexpected server error |

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- Docker + Docker Compose

### Run in development mode

```bash
# Start PostgreSQL
docker-compose up -d

# Start the API with live reload
./mvnw quarkus:dev
```

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/q/swagger-ui`
- Dev UI: `http://localhost:8080/q/dev`

### Run tests

```bash
./mvnw test
```

### Configuration profiles

| Property | Dev | Prod |
|---|---|---|
| SQL logging | enabled | disabled |
| Swagger UI | enabled | disabled |
| Log level | DEBUG | WARNING |
| Schema generation | update | none |

## Author

**Michael Espinosa**
- GitHub: [@Michael-2405](https://github.com/Michael-2405)
- LinkedIn: [michael-a-espinosa-batista](https://linkedin.com/in/michael-a-espinosa-batista)
