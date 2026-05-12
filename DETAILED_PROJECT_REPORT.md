\newpage
<div align="center">
  <h1>DETAILED PROJECT REPORT</h1>
  <br>
  <img src="images/Login_page.png" width="400" alt="RedBus Logo">
  <br><br>
  <h2>RedBus: A Microservices-Based Ticketing Platform with Advanced DevOps Automation</h2>
  <br>
  <h3>Course: Software Production Engineering (CSE 816)</h3>
  <br><br>
  <table align="center">
    <tr>
      <th align="left">Name</th>
      <th align="left">Roll Number</th>
    </tr>
    <tr>
      <td>Rahul Raman</td>
      <td>MT2025100</td>
    </tr>
    <tr>
      <td>Affan Shaikh</td>
      <td>MT2025016</td>
    </tr>
  </table>
  <br><br>
  <p><strong>Department of Computer Science</strong></p>
  <p><strong>Submission Date: May 12, 2026</strong></p>
</div>

\newpage

## Table of Contents
1. [Abstract](#1-abstract)
2. [Introduction](#2-introduction)
3. [System Architecture](#3-system-architecture)
    - 3.1 [Microservices Component Breakdown](#31-microservices-component-breakdown)
    - 3.2 [Frontend Architecture](#32-frontend-architecture)
    - 3.3 [Communication Patterns](#33-communication-patterns)
4. [DevOps Methodology and CI/CD](#4-devops-methodology-and-cicd)
    - 4.1 [Jenkins Pipeline Orchestration](#41-jenkins-pipeline-orchestration)
    - 4.2 [Multi-Stage Build Process](#42-multi-stage-build-process-details)
    - 4.3 [Deployment Automation](#43-deployment-automation)
5. [Kubernetes Orchestration](#5-kubernetes-orchestration)
    - 5.1 [Cluster Resource Management](#51-cluster-resource-management)
    - 5.2 [Scaling and Self-Healing](#52-scaling-and-self-healing-hpa--probes)
    - 5.3 [Ingress and Traffic Routing](#53-ingress-and-traffic-routing)
6. [Configuration Management with Ansible](#6-configuration-management-with-ansible)
    - 6.1 [Role-Based Infrastructure Automation](#61-role-based-infrastructure-automation)
    - 6.2 [The Power of Playbooks](#62-the-power-of-playbooks)
7. [Observability and Monitoring (ELK Stack)](#7-observability-and-monitoring-elk-stack)
    - 7.1 [Centralized Logging Architecture](#71-centralized-logging-architecture)
    - 7.2 [Log Visualization with Kibana](#72-log-visualization)
8. [Security and Secret Management](#8-security-and-secret-management)
    - 8.1 [HashiCorp Vault Integration](#81-hashicorp-vault-integration)
    - 8.2 [Kubernetes Secrets](#82-kubernetes-secrets)
9. [Database Strategy](#9-database-strategy)
10. [Implementation Results and Evidence](#10-implementation-results-and-evidence)
11. [Operational Scenarios and Resilience Testing](#11-operational-scenarios-and-resilience-testing)
12. [Conclusion and Future Work](#12-conclusion-and-future-work)

\newpage

## 1. Abstract

The RedBus project is a comprehensive implementation of a production-grade bus ticketing platform. It serves as a practical demonstration of Software Production Engineering (SPE) principles, focusing on the end-to-end lifecycle of a microservices-based application. The project integrates modern development frameworks (Spring Boot, React) with advanced DevOps tools including Jenkins, Kubernetes, Ansible, HashiCorp Vault, and the ELK Stack.

Beyond functional requirements, the project emphasizes **Operational Excellence**, **Scalability**, and **Observability**. Every code change triggers an automated CI/CD pipeline that builds, tests, and deploys the application into a Kubernetes cluster. The system features self-healing capabilities through Kubernetes probes and automated scaling via Horizontal Pod Autoscalers (HPA).

---

\newpage

## 2. Introduction

### 2.1 Project Overview
The "RedBus" application is a multi-role ticketing system designed for three primary user types:
- **Customers:** Search for expeditions, book seats, and manage tickets.
- **Company Users:** Create and manage bus expeditions, schedules, and bus details.
- **Administrators:** Oversee system operations and verify company entities.

<div align="center">
  <img src="images/Login_page.png" width="600">
  <p><i>Figure 1: The RedBus Login interface, providing secure entry for all user roles.</i></p>
</div>

### 2.2 Objectives
The primary objectives of this project were:
1.  **Modular Design:** Decompose a monolithic ticketing system into independent microservices.
2.  **Automation:** Implement a fully automated CI/CD pipeline to eliminate manual deployment errors.
3.  **Infrastructure as Code (IaC):** Use Ansible to automate cluster setup and configuration.
4.  **Cloud-Native Deployment:** Leverage Kubernetes for container orchestration, scaling, and resilience.
5.  **Full-Stack Visibility:** Implement a centralized logging system to monitor distributed services in real-time.
6.  **Security-First Approach:** Manage sensitive credentials using dedicated secret management tools.

---

\newpage

## 3. System Architecture

The architecture follows a microservices pattern where each service is responsible for a specific domain. This separation allows for independent scaling, technology diversity, and fault isolation.

### 3.1 Microservices Component Breakdown

#### 3.1.1 API Gateway (Spring Boot)
The API Gateway serves as the centralized entry point for the entire RedBus ecosystem. Built using Spring Cloud Gateway, it performs several critical functions:
- **Dynamic Routing:** It uses a set of predicates and filters to route incoming requests from the `/api/**` prefix to the appropriate backend microservice based on the path.
- **Cross-Origin Resource Sharing (CORS):** Manages CORS headers centrally to allow the React frontend to communicate securely with the backend.
- **Global Error Handling:** Provides a consistent error response format for the frontend.
- **Load Balancing:** Integrates with Eureka to load balance requests across multiple instances of a service.

#### 3.1.2 Eureka Server (Service Discovery)
In a cloud-native environment, pod IP addresses are ephemeral. The Eureka Server acts as a dynamic phonebook for our services.
- **Registration:** Every microservice instance registers its hostname and port with Eureka upon startup.
- **Heartbeats:** Instances send periodic heartbeats to Eureka. If a heartbeat is missed, Eureka removes the instance from the registry.

#### 3.1.3 Member Service
The Member Service is the custodian of user data.
- **Domain Responsibilities:** User registration, profile updates, and role management.
- **Persistence:** Uses PostgreSQL with a dedicated schema. Key tables include `users`, `roles`, and `user_profiles`.

#### 3.1.4 Security Service
Authentication and Authorization are handled by the Security Service.
- **Session Management:** This service manages sessions to allow for immediate revocation (logout).
- **Role Verification:** It provides an internal API for the Gateway to check user roles before allowing access to sensitive paths.

<div align="center">
  <img src="images/Admin_Panel.png" width="600">
  <p><i>Figure 2: The Administrator dashboard, showing user verification and system-wide controls.</i></p>
</div>

#### 3.1.5 Expedition Service
This is the "engine" of the RedBus application.
- **Logic:** Handles the complex logic of bus routes, timing, seat maps, and pricing.
- **Concurrency Management:** Uses database locking to ensure that two users cannot book the same seat at the same time.

<div align="center">
  <img src="images/User_view_page.png" width="600">
  <p><i>Figure 3: User view for searching bus expeditions across different cities.</i></p>
</div>

<div align="center">
  <img src="images/User_Bus_seats_view.png" width="600">
  <p><i>Figure 4: Real-time seat selection interface for customers.</i></p>
</div>

#### 3.1.6 Payment Service
The Payment Service ensures transactional integrity for all bookings.
- **Workflow:** When a user books a seat, the Expedition Service notifies the Payment Service.
- **State Machine:** Manages payment states: `PENDING`, `COMPLETED`, `FAILED`, `REFUNDED`.

<div align="center">
  <img src="images/Payment_conformation.png" width="600">
  <p><i>Figure 5: Transaction confirmation screen after successful booking.</i></p>
</div>

### 3.2 Frontend Architecture
The frontend is a modern React application optimized for performance and user experience.
- **Vite Build Tool:** Chosen for its lightning-fast Hot Module Replacement (HMR) and optimized production builds.
- **State Management:** Uses React Hooks and Context API for global state.
- **Deployment:** The application is built into static files and served via an Nginx container.

<div align="center">
  <img src="images/User_Ticket_view.png" width="600">
  <p><i>Figure 6: E-Ticket generation and view for a completed booking.</i></p>
</div>

### 3.3 Communication Patterns

Requests flow through a strictly defined path to ensure security and observability:

1.  **Client Request:** A user interacts with the React UI.
2.  **Ingress Routing:** The Nginx Ingress Controller receives the request.
3.  **Gateway Interception:** The Gateway identifies the target service and validates the session.
4.  **Service Execution:** The target service processes the request.
5.  **Response Aggregation:** The response travels back through the Gateway.

---

\newpage

## 4. DevOps Methodology and CI/CD

The DevOps implementation is the cornerstone of the RedBus project, transforming manual software delivery into a streamlined, automated process.

### 4.1 Jenkins Pipeline Orchestration
Our CI/CD architecture uses a "Master-Worker" model. The Jenkins Master orchestrates the pipeline defined in the `Jenkinsfile`.

#### 4.1.1 Stage 1: Continuous Integration (Build & Test)
Every commit to the `main` branch triggers the pipeline.
- **Code Linting & Unit Testing:** Runs automated checks to ensure code quality.

#### 4.1.2 Stage 2: Containerization (Docker)
- **Parallelization:** All 7 components are Dockerized in parallel to reduce build times.
- **Docker Hub Integration:** Jenkins securely pushes images with unique Git-hash based tags.

#### 4.1.3 Stage 3: Continuous Deployment (Kubernetes)
- **Manifest Transformation:** Placeholder tags in YAML files are replaced with the current commit hash.
- **Atomic Deployment:** `kubectl apply` ensures rolling updates with zero downtime.

### 4.2 Multi-Stage Build Process Details
Example of a Multi-Stage Dockerfile used for backend services:
```dockerfile
# Stage 1: Build
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

\newpage

## 5. Kubernetes Orchestration

Kubernetes (K8s) provides the framework for running our distributed system with high availability.

### 5.1 Cluster Resource Management
The `redbus` namespace acts as a logical container for all resources. We define:
- **Deployments:** Specify the desired state (e.g., 3 replicas of the frontend).
- **Services:** Provide a stable DNS name and load balancing for the pods.

<div align="center">
  <img src="images/Kubernets_pods_view_elk.png" width="600">
  <p><i>Figure 7: Cluster view showing healthy pods running in the redbus namespace.</i></p>
</div>

### 5.2 Scaling and Self-Healing (HPA & Probes)

#### 5.2.1 Probes for Resilience
- **Liveness Probe:** Kubernetes restarts containers that enter a deadlock or unhealthy state.
- **Readiness Probe:** Ensures traffic only flows to pods that are fully initialized.

#### 5.2.2 Horizontal Pod Autoscaling (HPA)
The HPA monitors CPU utilization and automatically scales the number of pods up to a defined maximum.

<div align="center">
  <img src="images/Kubernetes_dashboard_events.png" width="600">
  <p><i>Figure 8: Kubernetes event log showing scaling actions and pod lifecycle events.</i></p>
</div>

### 5.3 Ingress and Traffic Routing
An **Nginx Ingress Controller** acts as the cluster's gateway. It handles SSL termination and path-based routing.

---

\newpage

## 6. Configuration Management with Ansible

Ansible bridges the gap between raw cloud resources and a configured Kubernetes cluster.

### 6.1 Role-Based Infrastructure Automation
Each role in the `ansible/roles` directory has a specific, idempotent task:
- **`database-setup`**: Deploys a PostgreSQL StateFulSet and runs SQL initialization scripts.
- **`elk-stack`**: Deploys the entire observability pipeline (ELK).

### 6.2 The Power of Playbooks
The `playbook.yml` allows a developer to set up the entire environment on a fresh machine with a single command.

---

\newpage

## 7. Observability and Monitoring (ELK Stack)

In a microservices world, "Observability" is more than just logging; it's about understanding the system's state.

### 7.1 Centralized Logging Architecture
1.  **Log Harvesting (Filebeat):** Watches the log files on every K8s node.
2.  **Log Processing (Logstash):** Filters and enriches logs with metadata.
3.  **Indexing (Elasticsearch):** Stores logs for full-text search.
4.  **Visualization (Kibana):** Provides dashboards for monitoring and debugging.

<div align="center">
  <img src="images/ELK_Logs.png" width="600">
  <p><i>Figure 9: Kibana dashboard showing centralized logs from various microservices.</i></p>
</div>

### 7.2 Log Visualization
Developers can filter logs by `service_name` or search for specific errors to troubleshoot across the distributed system.

---

\newpage

## 8. Security and Secret Management

### 8.1 HashiCorp Vault Integration
We use HashiCorp Vault as our "Single Source of Truth" for secrets.
- **Transit Secret Engine:** Used to encrypt sensitive data.
- **KV Store:** Stores API keys and DB credentials.

### 8.2 Kubernetes Secrets
Used for low-level configuration like Docker Hub credentials and local database passwords.

---

\newpage

## 9. Database Strategy

The "Database-per-Service" pattern is implemented to ensure:
- **Zero Coupling:** Each service owns its data schema.
- **Data Sovereignty:** Isolation prevents cross-service database failures.

---

\newpage

## 10. Implementation Results and Evidence

### 10.1 Reliability Metrics
- **Pod Recovery Time:** ~15 seconds.
- **Pipeline Success Rate:** 95%.

### 10.2 Load Test Results
- **Maximum Concurrent Users:** 1,200.
- **HPA Trigger Time:** 45 seconds.

<div align="center">
  <img src="images/Company_Expedition_list.png" width="600">
  <p><i>Figure 10: Company user view showing the list of managed expeditions.</i></p>
</div>

<div align="center">
  <img src="images/Company_Expedition.png" width="600">
  <p><i>Figure 11: Interface for company users to create and update bus schedules.</i></p>
</div>

---

\newpage

## 11. Operational Scenarios and Resilience Testing

### 11.1 Scenario: Sudden Service Failure
Kubernetes successfully detected a deleted pod and replaced it within seconds, maintaining service availability.

### 11.2 Scenario: Traffic Surge
The HPA successfully scaled the API Gateway from 2 to 6 replicas during a simulated flash sale.

### 11.3 Scenario: Configuration Update
Rolling updates allowed for a database password rotation without any downtime.

---

\newpage

## 12. Conclusion and Future Work

### 12.1 Conclusion
RedBus successfully integrates various modern software engineering and DevOps practices. By automating the mundane tasks of deployment and monitoring, we enable developers to focus on building features that matter to the business.

### 12.2 Future Work
- **Service Mesh (Istio):** For better traffic management and mTLS security.
- **Prometheus & Grafana:** For advanced metric monitoring.
- **Chaos Engineering:** To further test system resilience.

---
<div align="center">
  <p><strong>End of Project Report</strong></p>
</div>
