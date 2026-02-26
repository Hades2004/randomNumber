# RandomNumber & SquareNumber Demo Project

A production-ready Spring Boot 3.5.x application demonstrating a microservice architecture with persistence, caching, messaging, and automated CI/CD.

## Architecture

- **Random Number Generation**: Endpoint to generate a random number and publish the event to Kafka.
- **Square Calculation**: Endpoint to calculate the square of a number with **Redis Caching**.
- **Security**: Basic Auth protected endpoints with a JPA-backed UserDetailsService and dynamic user initialization.
- **Messaging**: Kafka Producer and Consumer integration.
- **Persistence**: **PostgreSQL** (16-alpine) for persistent user and application data.
- **Health Checks**: Specialized **Kubernetes Liveness and Readiness Probes** via Spring Boot Actuator.

## Technology Stack

- **Java 25** (Eclipse Temurin)
- **Spring Boot 3.5.10**
- **Spring WebFlux** (Reactive endpoints)
- **Spring Data JPA** (PostgreSQL)
- **Spring Data Redis** (Caching)
- **Spring Kafka** (Messaging)
- **Spring Security** (Basic Auth)
- **Jenkins** (Automated CI/CD)
- **Kubernetes (kind)** (Orchestration)

## Prerequisites

- **Java 25** JDK
- **Maven 3.9+**
- **Podman** or **Docker**
- **kubectl** (Kubernetes CLI)
- **Jenkins** (with Docker & Pipeline support)

## Getting Started (Local Development)

### 1. Set up Infrastructure
Start the required local services (PostgreSQL, Kafka, Redis):

```bash
cd infra
podman-compose up -d
```

### 2. Run the Application
Start the Spring Boot application locally:

```bash
./mvnw clean spring-boot:run
```
The server will start on `http://localhost:8081`. Default local credentials: `user` / `password`.

## CI/CD Pipeline (Jenkins)

The project includes a sophisticated `Jenkinsfile` with the following stages:

1.  **Maven Install**: Compiles the app and runs tests using a Java 25 Maven container.
2.  **Docker Build (Optimized)**: Calculates hashes of the `Dockerfile` and `demo.jar`. It **skips** the build if no changes are detected since the last successful run.
3.  **Docker Push**: Pushes the image to `docker.io/hades2004/randomnumber`.
4.  **Deploy App**: 
    - Dynamically creates/updates Kubernetes Secrets (`db-secrets`) from Jenkins credentials.
    - Provisions **PostgreSQL**, **Kafka**, and **Redis** in the cluster.
    - Deploys the application with proper readiness checks.

### Required Jenkins Credentials:
- `docker-credentials`: Docker Hub username/password.
- `k8s-deployer-token`: Secret text (Kubernetes ServiceAccount token).
- `db-password`: Secret text for the PostgreSQL user.
- `admin-password`: Secret text for the initial application admin.

## Kubernetes Deployment

### Accessing the App in the Cluster
Since the app uses a NodePort and specialized health probes, use **Port-Forwarding** to access it reliably:

```bash
kubectl port-forward service/randomnumber-service 8081:8081
```

- **Health Status**: [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)
- **Liveness**: [http://localhost:8081/actuator/health/liveness](http://localhost:8081/actuator/health/liveness)
- **Readiness**: [http://localhost:8081/actuator/health/readiness](http://localhost:8081/actuator/health/readiness)

### Ingress Configuration
An `ingress.yaml` is provided for host-based routing. Add `127.0.0.1 randomnumber.local` to your `/etc/hosts` and access via:
`http://randomnumber.local:9090` (assuming port 80 is mapped to 9090).

## Security Note
Passwords and sensitive tokens are **not stored in Git**. They are managed via:
1.  **Jenkins Credentials** (Secret Text)
2.  **Kubernetes Secrets** (`db-secrets`) injected as environment variables.
