# RandomNumber & SquareNumber Demo Project

A Spring Boot 3.5.x application demonstrating a reactive-ready architecture with caching, messaging, and security.

## Features

- **Random Number Generation**: Endpoint to generate a random number and publish the event to Kafka.
- **Square Calculation**: Endpoint to calculate the square of a number with **Redis Caching**.
- **Security**: Basic Auth protected endpoints with a JPA-backed UserDetailsService.
- **Messaging**: Kafka Producer and Consumer integration.
- **Persistence**: H2 In-Memory database for user management.
- **Developer Experience**: Automated Javadoc/Source downloads and comprehensive test coverage.

## Technology Stack

- **Java 25**
- **Spring Boot 3.5.10**
- **Spring WebFlux** (Reactive endpoints)
- **Spring Data JPA** (H2 Database)
- **Spring Data Redis** (Caching)
- **Spring Kafka** (Messaging)
- **Spring Security** (Authentication/Authorization)
- **Lombok** (Boilerplate reduction)
- **Podman/Docker** (Infrastructure)

## Prerequisites

- **Java 25** JDK
- **Maven 3.8+**
- **Podman** or **Docker** (with `podman-compose` or `docker-compose`)

## Getting Started

### 1. Set up Infrastructure
Navigate to the infrastructure directory and start the required services (Kafka, Redis):

```bash
cd /PATH/TO/PROJECT/infra
podman-compose up -d
```

*Note: Ensure Kafka is configured for a single-node cluster (see Troubleshooting).*

### 2. Build the Project
Download dependencies (including Javadocs) and build the application:

```bash
./mvnw clean install
```

### 3. Run the Application
Start the Spring Boot application:

```bash
./mvnw spring-boot:run
```
The server will start on `http://localhost:8081` (default).

## API Endpoints

| Method | Endpoint | Description | Auth Required |
|        |          |             |               |
| GET    | `/rest/randomNumber` | Returns a random number and sends a Kafka message. | Yes |
| GET    | `/rest/squareNumber?value=5` | Returns the square of the value (Cached). | Yes |
| GET    | `/h2-console` | Web interface for the H2 Database. | No |

## Testing
Run the test suite:
```bash
./mvnw test
```

## Troubleshooting

### Kafka Connection Issues
If you see `INVALID_REPLICATION_FACTOR` in the Kafka logs, ensure your `podman-compose.yml` environment variables include:
- `KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1`
- `KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1`
- `KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1`
