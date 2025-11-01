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

### 1. **Microservices Structure**
- **`event-service`** – Handles event scheduling, polling, publishing, and exposes REST APIs.  
- **`stats-service`** – Mocks a third-party API for event statistics.  
- **`stats-service-client`** – A reusable HTTP client built with Spring **WebClient**.

---

### 2. **Reactive Client (with Controlled Blocking)**
- The `stats-service-client` uses Spring **WebClient** to perform reactive HTTP calls.  
- For simplicity, the client currently **blocks at the end** of each call.  
- This allows the system to maintain a straightforward synchronous flow while remaining flexible for a future shift to full reactivity.

---

### 3. **Kafka Messaging**
- Event updates are sent **asynchronously** to Kafka (`sports.events` topic).  
- The Kafka **consumer** logs incoming messages for debugging and observability.  
- The **producer** uses built-in **retry and recovery logic** to ensure reliable message delivery in case of transient failures.

---

### 4. **Scheduling (with Virtual Threads)**
- The `SchedulerService` periodically polls statistics for all **live events** and publishes updates every **10 seconds**.  
- To efficiently handle many concurrent polling tasks, the system uses **Java Virtual Threads (JEP 444)** instead of a limited fixed-size thread pool.

#### Why Virtual Threads
- **Massive Concurrency** – Allows thousands of concurrent polling tasks without exhausting system resources.  
- **Simplified Code** – Each polling task uses traditional **blocking I/O** without needing asynchronous or reactive complexity.  
- **Isolation & Stability** – Each event runs in its own lightweight thread, so one slow or stuck event doesn’t impact others.  
- **Efficiency** – Virtual threads automatically yield carrier threads when blocked on I/O, maximizing CPU utilization.

#### Artificial Event Distribution
- On startup, each event is assigned a **randomized initial delay (jitter)** before beginning its polling cycle.  
- This prevents all events from polling at the same moment (the **thundering herd problem**) and evenly distributes system load across time.

---

### 5. **Exception Handling**
- A **global exception handler** in the `common` module ensures consistent REST error responses across all microservices.  
- This guarantees uniform error structures and simplifies client-side error handling.

---

### 6. **Swagger Integration**
- Each service exposes **Swagger (OpenAPI 3)** documentation at:  
  ```
  /swagger-ui.html
  ```
- Enables quick API exploration, testing, and validation for developers and consumers.

---

### 7. **Dockerized Kafka**
- Kafka runs in **KRaft mode** (no ZooKeeper), simplifying local setup.  
- This modern configuration reduces maintenance overhead and improves startup reliability.

---

### Summary
By introducing **virtual threads**, the system combines the **simplicity of blocking code** with the **scalability of asynchronous architectures**.  
The **artificial distribution of event polling** ensures even workload distribution and predictable performance — enabling the platform to scale efficiently as the number of live events grows.

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
   - AI helped configure Kafka to run without ZooKeeper by setting up proper listener and controller roles, generating a valid cluster ID, and ensuring that the configuration works smoothly in a local environment.  
   - Adjustments were made to simplify setup while avoiding production overhead.

2. **Test creation and refinement**:  
   - AI was used to generate initial test templates for producers, schedulers, and controllers.  
   - I manually adjusted and expanded these tests to ensure they passed and to cover additional edge cases such as Kafka failures and retry handling.

---
