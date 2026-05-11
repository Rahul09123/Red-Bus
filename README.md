# RedBus: Production-Grade Microservices Ticketing System

RedBus is a **high-availability, role-aware** (Customer / Company / Admin) intercity bus ticketing platform. It features a distributed microservice topology built on **Java Spring Boot**, orchestrated with **Kubernetes**, and monitored with the **ELK Stack**.

---

## 🌟 Key Features
- **One-Click Booking**: Frictionless ticket reservation system with bypassable payment for high-speed checkout.
- **Auto-Scaling**: Horizontal Pod Autoscaler (HPA) configured to handle traffic spikes.
- **Centralized Observability**: Full ELK stack integration (Elasticsearch, Logstash, Kibana, Filebeat) with Kubernetes metadata enrichment.
- **Automated CI/CD**: Jenkins "Master Orchestrator" pipeline for parallel building and deployment of all services.
- **Service Discovery**: Netflix Eureka integration for dynamic service registration and lookup.

---

## 🏗️ System Architecture

```text
Browser (React Frontend)
  │
  ▼
Ingress (Nginx) / API Gateway (Spring Cloud Gateway)
  │
  ├─► Security Service   (Authentication & Session Lifecycle)
  ├─► Member Service     (User Profiles & Verification)
  ├─► Expedition Service (Routes, Seats, Reservation & Ticketing)
  └─► Payment Service    (Card Registry & Mock Processing)
         │
         ▼
    PostgreSQL (Centralized Persistence)
```

### 🔭 Observability Stack (ELK)
- **Filebeat**: DaemonSet collecting logs from every node.
- **Logstash**: Processes and enriches logs with Kubernetes metadata (Namespace, Pod, Container).
- **Elasticsearch**: Distributed search engine for high-performance log storage.
- **Kibana**: Advanced dashboard for log visualization and service health monitoring.

---

## 🛠️ Technology Stack

### Frontend & Backend
- **Frontend**: React 19, Vite, TailwindCSS (Premium UI/UX)
- **Backend**: Java 21, Spring Boot 3.3.4, Spring Cloud, Hibernate
- **Database**: PostgreSQL 15

### DevOps & Infrastructure
- **Containerization**: Docker & Docker Hub
- **Orchestration**: Kubernetes (Minikube)
- **CI/CD**: Jenkins (Declarative Pipelines)
- **Monitoring**: ELK Stack (Elasticsearch 8.10.2)
- **Configuration**: Kubernetes ConfigMaps & Secrets

---

## 🚀 Deployment Guide (Minikube)

### 1. Prerequisites
- Minikube & Kubectl installed
- Docker installed

### 2. Startup Sequence
```bash
# Start Minikube
minikube start --memory=8192 --cpus=4

# Enable Ingress and Metrics Server
minikube addons enable ingress
minikube addons enable metrics-server

# Deploy Infrastructure (Postgres, Eureka, ELK)
kubectl apply -f k8s/elk-stack.yaml
kubectl apply -f k8s/postgres.yaml

# Deploy Microservices
kubectl apply -f k8s/api-gateway.yaml
# ... apply other k8s files
```

### 3. Access Points
- **Application**: `http://localhost` (via `minikube tunnel`)
- **Kibana Dashboard**: `http://localhost/kibana`
- **Eureka Dashboard**: `http://localhost:8761`
- **Jenkins**: `http://localhost:8080`

---

## 📊 Evaluation Metrics (Viva Highlights)
- **Fault Tolerance**: Liveness and Readiness probes ensure self-healing pods.
- **Scalability**: HPA scales pods based on real-time CPU metrics.
- **Security**: Kubernetes Secrets for DB credentials and Session-based Auth.
- **Automation**: One-click "Build & Deploy" via Jenkins Orchestrator.

---

## 👨‍💻 Authors
- **Rahul Raman  Affan Shaikh**

---
> **Note**: This project was developed as a Final Project showcase for production-grade microservice orchestration.
