# Spotstracker Project

**Asen Slivarov**

---

## Overview

This project is a microservices-based backend system that simulates scheduled REST calls and asynchronous event publishing using **Spring Boot**, **Kafka (in KRaft mode)**, and **Docker**.

It consists of two main services:

- **event-service** – Periodically calls a stats API, publishes messages to Kafka, and exposes REST endpoints to update and track event statuses.
- **stats-service** – Simulates a third-party API returning event scores.

A third module, **stats-service-client**, provides a reactive client for `stats-service`, which the `event-service` uses for communication.

---

## Setup & Run Instructions

### Prerequisites
- Java 21+
- Gradle 8+
- Docker & Docker Compose
- Ports:
  - 8081 → event-service
  - 8082 → stats-service
  - 9092 → Kafka

### Build the project

```bash
./gradlew clean build
```

This builds all modules and runs the tests.

### Run with Docker

1. **Start Kafka, Event, and Stats Services**

   ```bash
   cd docker
   docker-compose up --build
   ```

   This starts:
   - Kafka in KRaft mode (no ZooKeeper)
   - event-service (depends on Kafka & stats-service)
   - stats-service

2. **Verify services are up**
   - Event Service: http://localhost:8081
   - Stats Service: http://localhost:8082

### Run Locally (without Docker)

You can run each service individually from its root folder:

```bash
./gradlew :stats-service:bootRun
./gradlew :event-service:bootRun
```

Make sure Kafka is running locally on `localhost:9092`.

---

## How to Run Tests

To run all tests (unit and integration):

```bash
./gradlew test
```

Each microservice includes:
- Unit tests for mappers, controllers, and schedulers.
- Kafka producer/consumer tests with mocked templates.
- Integration tests for REST endpoints with MockMvc.

Test coverage includes retry logic, message publishing, scheduling, and API validation.

---

## Design Decisions Summary

1. **Microservices Structure**
   - `event-service` handles event scheduling, publishing, and REST APIs.
   - `stats-service` mocks a third-party API for event statistics.
   - `stats-service-client` acts as a reusable HTTP client using Spring WebClient.

2. **Reactive Client (with blocking)**
   - `stats-service-client` uses `WebClient` to expose reactive calls but blocks at the end for simplicity.

3. **Kafka Messaging**
   - Event updates are sent asynchronously to Kafka (`sports.events` topic).
   - The consumer logs incoming messages.
   - Uses retry and recovery logic for message publishing.

4. **Scheduling**
   - A `SchedulerService` periodically polls stats for all live events and publishes updates every 10 seconds.

5. **Exception Handling**
   - Global exception handler in the `common` module ensures consistent REST error responses across all microservices.

6. **Swagger Integration**
   - Each service includes Swagger (OpenAPI 3) documentation for all endpoints at `/swagger-ui.html`.

7. **Dockerized Kafka**
   - Uses KRaft mode (no ZooKeeper).
   - Simplifies local setup while avoiding production overhead.

---

## Example API Calls

### 1. Update Event Status

**POST** `http://localhost:8081/events/status`

```json
{
  "eventId": "match1",
  "status": "LIVE"
}
```

Response: `200 OK`

### 2. Get Event Stats

**GET** `http://localhost:8082/stats/event?id=match1`

Response:

```json
{
  "eventId": "match1",
  "score": "2:1"
}
```

---

## Swagger Documentation

Once the services are running:

- Event Service: http://localhost:8081/swagger-ui.html
- Stats Service: http://localhost:8082/swagger-ui.html

---

## AI-Assisted Work Documentation

I used AI assistance in the following areas:

1. **Kafka configuration for KRaft mode**:
   - I used AI help to correctly configure Kafka to run without ZooKeeper by setting up proper listener and controller roles, generating a valid cluster ID, and ensuring that the configuration works smoothly in a local environment.
   - Adjustments were made to simplify setup while avoiding production overhead.

2. **Test creation and refinement**:
   - AI was used to generate initial test templates for producers, schedulers, and controllers.
   - I manually adjusted and expanded these tests to ensure they passed and to cover additional edge cases such as Kafka failures and retry handling.

---
