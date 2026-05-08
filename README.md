# OpsMind AI

<p align="center">

  <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExYjF6aXc5a2h4NXg1eWx4Y3Q2N2c2a2s2bTV2eTF0N2Z1M2N1emh2aiZlcD12MV9naWZzX3NlYXJjaCZjdD1n/l0HlNaQ6gWfllcjDO/giphy.gif" width="900"/>

</p>

<p align="center">

Enterprise AI SaaS Platform built with Microservices, Event-Driven Architecture and Cloud-Native principles.

</p>

---

## Tech Stack

### Backend

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

---

### Architecture

![Microservices](https://img.shields.io/badge/Microservices-Architecture-blue?style=for-the-badge)
![DDD](https://img.shields.io/badge/DDD-Domain_Driven_Design-orange?style=for-the-badge)
![Clean Architecture](https://img.shields.io/badge/Clean_Architecture-000000?style=for-the-badge)
![Hexagonal](https://img.shields.io/badge/Hexagonal_Architecture-8A2BE2?style=for-the-badge)
![Event Driven](https://img.shields.io/badge/Event_Driven-Kafka-red?style=for-the-badge)

---

### Infrastructure

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-000000?style=for-the-badge&logo=apachekafka&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white)

---

### Observability

![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white)
![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white)
![Loki](https://img.shields.io/badge/Loki-000000?style=for-the-badge&logo=grafana&logoColor=white)
![OpenTelemetry](https://img.shields.io/badge/OpenTelemetry-000000?style=for-the-badge)

---

### AI Providers

![OpenAI](https://img.shields.io/badge/OpenAI-412991?style=for-the-badge&logo=openai&logoColor=white)
![Gemini](https://img.shields.io/badge/Google_Gemini-4285F4?style=for-the-badge&logo=google&logoColor=white)
![Ollama](https://img.shields.io/badge/Ollama-000000?style=for-the-badge)

---

### Status

![Status](https://img.shields.io/badge/Status-In_Development-yellow?style=for-the-badge)
![Architecture](https://img.shields.io/badge/Architecture-Enterprise-success?style=for-the-badge)
![Cloud Ready](https://img.shields.io/badge/Cloud_Ready-AWS-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

---

# Overview

OpsMind AI is a scalable multi-tenant SaaS ecosystem designed to deliver intelligent enterprise solutions for traditional industries such as:

- Retail
- Construction
- Mining
- Manufacturing
- Logistics
- Agribusiness
- Corporate Offices

The platform is designed from the ground up to support modular AI products, distributed systems, and enterprise-scale workloads.

---

# Vision

The goal of OpsMind AI is to become a fully modular AI ecosystem capable of powering:

- Intelligent dashboards
- Predictive analytics
- AI corporate assistants
- Automated workflows
- OCR and document analysis
- Enterprise chatbots
- Operational monitoring
- Financial intelligence systems
- AI observability
- Ticket management
- Intelligent recommendation systems
- Anomaly detection
- Autonomous AI agents

---

# Architecture Goals

The platform is designed to be:

- Multi-tenant
- Cloud-ready
- Kubernetes-ready
- AWS-ready
- Event-driven
- Highly scalable
- Stateless
- Fault tolerant
- Modular
- Distributed
- Enterprise-grade

The project initially runs entirely locally using only free and open-source technologies.

---

# Tech Stack

## Backend

- Java 21
- Spring Boot 3
- Spring Cloud
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring WebFlux
- Lombok
- MapStruct
- Maven

---

## Architecture

- Microservices
- Clean Architecture
- Hexagonal Architecture
- DDD (Domain-Driven Design)
- Event-Driven Architecture
- CQRS (where applicable)
- API Gateway
- Service Discovery
- Distributed Resilience

---

## Infrastructure

- PostgreSQL
- Redis
- Apache Kafka
- Docker
- Docker Compose
- Kubernetes
- NGINX
- Prometheus
- Grafana
- Loki
- OpenTelemetry

---

# Current Monorepo Structure

```text
/opsmind
 ├── /docker
 ├── /docs
 ├── /infrastructure
 ├── /kubernetes
 ├── /services
 │    ├── /opsmind-api-gateway
 │    ├── /opsmind-auth-service
 │    ├── /opsmind-company-service
 │    ├── /opsmind-ai-orchestrator
 │    ├── /opsmind-ai-worker
 │    ├── /opsmind-config-server
 │    ├── /opsmind-discovery-service
 │    ├── /opsmind-notification-service
 │    └── /opsmind-observability-service
 └── pom.xml
```

---

# Implemented So Far

## Foundation

- Monorepo architecture
- Parent Maven POM
- Centralized dependency management
- Enterprise project structure
- Docker infrastructure foundation

---

## Infrastructure

Docker Compose environment running:

- PostgreSQL
- Redis
- Kafka
- Zookeeper

---

## Spring Cloud Foundation

### Config Server

Implemented:
- Spring Cloud Config Server
- Native profile support
- Centralized configuration structure
- Actuator health endpoints

### Discovery Service

Implemented:
- Eureka Server
- Service discovery foundation
- Health monitoring

---

## Auth Service Foundation

Implemented:
- Clean Architecture structure
- Domain layer
- Infrastructure layer
- PostgreSQL integration
- Flyway migrations
- Multi-tenant foundation
- RBAC foundation
- Repository abstraction
- JPA adapters
- Domain entities

---

# Database

Current database strategy:

- PostgreSQL
- Database-per-service architecture
- Flyway migrations
- Multi-tenant ready structure

Current tables:
- users
- roles
- user_roles
- flyway_schema_history

---

# Event-Driven Architecture

Kafka topics planned:

- auth-events
- ai-requests
- ai-results
- notifications
- audit-events
- company-events
- billing-events

---

# Security Goals

Planned features:

- JWT Authentication
- Refresh Tokens
- RBAC
- Multi-tenant isolation
- Rate limiting
- API Gateway security
- Audit logging
- Brute force protection

---

# Observability Goals

Planned stack:

- Prometheus
- Grafana
- Loki
- OpenTelemetry
- Distributed tracing
- Centralized logging
- Kafka metrics
- AI metrics

---

# AI Platform Goals

The AI ecosystem will support:

- OpenAI integration
- Gemini integration
- Ollama local models
- AI orchestration
- Multi-provider strategy
- AI fallback mechanisms
- Retry and circuit breaker patterns
- Async AI processing

---

# Current Development Stage

The project is currently in the foundational architecture phase.

Focus areas:
- infrastructure
- service discovery
- configuration management
- authentication foundation
- domain modeling
- clean architecture implementation

---

# Next Steps

Planned upcoming implementations:

1. Application Layer (Use Cases)
2. Authentication Flow
3. JWT + Refresh Tokens
4. API Gateway
5. Company Service
6. Kafka Event Foundation
7. AI Orchestrator
8. AI Workers
9. Notification Service
10. Observability Stack
11. Kubernetes Deployment
12. CI/CD Pipelines

---

# Engineering Principles

This project follows:

- SOLID
- Clean Code
- Repository Pattern
- DTO Pattern
- Use Cases
- Global Exception Handling
- Correlation ID
- Distributed Tracing
- Enterprise modularization

---

# Status

OpsMind AI is currently under active architecture and backend foundation development.
