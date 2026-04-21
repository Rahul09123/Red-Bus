# RedBus

RedBus is a **role-aware** (**Customer / Company / Admin**) intercity bus ticketing platform for India that models **bus transportation workflows** using a **microservice topology** built on `Spring Boot` and `Eureka` service discovery.

This repository is intentionally structured as an architectural showcase:
- an **edge orchestrator** (`api-gateway`) is the single client-facing backend entry-point,
- core business capabilities live inside isolated services,
- service-to-service communication is performed via synchronous HTTP with discovery through `eureka-server`.

Frontend is implemented with **React + Vite**, while the backend consists of **Spring Boot microservices**.

### Supported Cities (India)
RedBus operates across India's top 10 cities:
- **Mumbai** • **Delhi** • **Bangalore** • **Hyderabad** • **Chennai**
- **Kolkata** • **Pune** • **Ahmedabad** • **Jaipur** • **Surat**

---

## Functional Capabilities (High-Level)

### Customer
- Registration / Login / Logout (session-based)
- Bus route search across India (10 major cities)
- Seat availability lookup
- Bus ticket purchase
- Purchased tickets listing & booking history

### Profile & Account Management
- Profile mutation (name, surname, gender, email, password)
- Favorite bus operator association
- Payment card add / deactivate flows (via payment-service)

### Bus Operator (Company)
- Bus route creation & management
- Operator-scoped route listing

### Admin
- Bus operator verification workflow
- Admin verification workflow

---

## System Topology (Concrete)

```text
Browser
  │
  ▼
frontend (React + Vite)
  │  (Vite dev proxy: /api -> api-gateway)
  ▼
api-gateway (edge orchestrator, @LoadBalanced RestTemplate)
  │
  ├─► member-service     (registration, profiles, verification, resources)
  ├─► security-service   (session lifecycle + session validation)
  ├─► expedition-service (expeditions, seats, reservation + ticketing)
  └─► payment-service    (card registry + pseudo payment processing)
         ▲
         └──── consumed by expedition-service and member-service

eureka-server (service discovery)
postgres (expeditionDB + memberDB + securityDB + paymentDB)
pgadmin (DB UI)
```

### Discovery & Client-Side Load Balancing

- Backend services register themselves to `eureka-server`.
- The edge layer calls services using service identifiers (e.g. `http://member-service/...`) through a `@LoadBalanced` `RestTemplate`.

This keeps service addressing **indirection-friendly** (host/port changes are absorbed by discovery) while preserving a simple synchronous integration model.

---

## Global Layering Model (Conceptual)

The system can be reasoned about in four coarse layers:

1. **Presentation Layer** → `frontend/` (UI + routing, no backend access except via the gateway)
2. **Edge Layer** → `backend/api-gateway/` (API boundary + orchestration + DTO mapping)
3. **Domain/Application Layer** → `backend/*-service/` (business logic + persistence)
4. **Infrastructure Layer** → PostgreSQL, Docker, Docker Compose, pgAdmin

Inside each microservice, code is further organized in a typical layered structure:

```text
Controller → Service → Repository → Model/Entity
```

---

## Session Model (CookieDTO + Server-Side Session)

ShuBilet uses a **session-oriented** authentication model.

- The `api-gateway` keeps an `HttpSession` and serializes it into an internal `CookieDTO` carrier.
- Authentication/session lifecycle is delegated to `security-service` (`/api/auth/*`), which persists session records (admin/company/customer sessions) in PostgreSQL.
- A scheduled maintenance component (`SessionSweeper`, enabled via `@EnableScheduling`) periodically removes expired sessions.

To improve traceability across hops, the gateway commonly emits an `X-Request-Id` header which is forwarded to downstream services.

---

## Persistence Model (PostgreSQL)

The Docker runtime provisions a single PostgreSQL instance and creates four databases via `db/init/` scripts:

- `expeditionDB`: used by `expedition-service` (bus routes & schedules)
- `memberDB`: used by `member-service` (users & companies)
- `securityDB`: used by `security-service` (sessions)
- `paymentDB`: used by `payment-service` (payment cards)

Schema evolution in the local environment is handled by JPA with:
- `spring.jpa.hibernate.ddl-auto=update`

---

## Technology Stack (As Used In This Repo)

### Frontend
- React 19
- Vite
- React Router DOM

### Backend
- Java 21
- Spring Boot 3.3.4
- Spring Cloud Netflix Eureka (client/server) 2023.0.3
- Spring Web (REST)
- Spring Data JPA (Hibernate)
- RestTemplate + Spring Cloud LoadBalancer (`@LoadBalanced`)
- Spring Scheduling (security-service)
- MapStruct (api-gateway DTO mapping)

### Infrastructure / DevOps
- PostgreSQL 15
- Docker & Docker Compose
- pgAdmin 4 (Docker image)
- Maven (wrapper in each backend service)

---

## Quick Start (Docker Compose)

### Prerequisites
- Docker Engine / Docker Desktop

### Run

```bash
docker compose up --build
```

### Access Points (Host)

- Frontend: `http://localhost:3000` (admin user: `redbus@example.com`, password `SecurePassword123!`)
- API Gateway: `http://localhost:8080`
- Eureka Dashboard: `http://localhost:8761`
- pgAdmin: `http://localhost:5051` (user: `admin@example.com`, password: `admin`)
- PostgreSQL: `localhost:5432` (user: `postgres`, password: `123`)

### Stop / Reset

```bash
docker compose down
```

```bash
docker compose down -v
```

> Security note: Default credentials in `docker-compose.yml` are for local development only.

---

## API Surface (Gateway)

All external backend access is funneled through `api-gateway` (`http://localhost:8080`).

Primary endpoint groups exposed by the edge layer:

- `POST /api/auth/*` (customer/operator/admin registration, login/logout)
- `POST /api/expedition/*` (search bus routes, check seat availability, create route, list operator routes)
- `POST /api/ticket/*` (purchase bus ticket, list purchased tickets)
- `POST /api/profile/*` (profile updates, favorite operators, payment cards)
- `POST /api/auth/verify/*` (operator/admin verification workflows)

---

## Repository Layout

```text
.
├── backend/
│   ├── api-gateway
│   ├── eureka-server
│   ├── member-service
│   ├── security-service
│   ├── expedition-service
│   └── payment-service
├── frontend/
├── db/
│   └── init.sql
└── docker-compose.yml
```

---

## Authors

- Rahul Raman

