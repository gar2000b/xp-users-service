# xp-users-service

A lightweight **experimental** Spring Boot microservice for managing user accounts across various research and prototype systems.  
This service is part of the broader **XP (eXPerimental Platform)** ecosystem.

> **Part of the XP Project:** This service is part of the [xp-project](https://github.com/gar2000b/xp-project) workspace, which coordinates multiple XP microservices and infrastructure.

---

## 🚀 Features
- Spring Boot 4.x (modern stack)
- Java 21 runtime
- Self-contained executable **fat JAR**
- REST API with full CRUD operations for user management
- **OpenAPI 3.0 / Swagger UI** documentation
- Docker image build + push support
- Actuator endpoints enabled for monitoring

---

## 🧰 Tech Stack

| Component | Choice |
|----------|--------|
| Language | Java 21 |
| Framework | Spring Boot 4.0 |
| Build Tool | Maven |
| Packaging | Fat JAR |
| Deployment | Docker |
| API Documentation | SpringDoc OpenAPI 3.0 / Swagger UI |
| Database | MariaDB |

---

## 🛠️ Building & Running

Build:
```bash
mvn clean package
```

Run:
```bash
java -jar target/xp-users-service-*.jar
```

Service runs at:
```
http://localhost:8081
```

Health endpoint:
```
http://localhost:8081/actuator/health
```

## 📚 API Documentation (Swagger/OpenAPI)

This service includes comprehensive API documentation powered by **SpringDoc OpenAPI 3.0**.

### Access Swagger UI

Interactive API documentation and testing interface:
```
http://localhost:8081/swagger-ui.html
```

The Swagger UI provides:
- Complete API endpoint documentation
- Interactive "Try it out" functionality
- Request/response schema definitions
- Example values for all parameters

### OpenAPI Specification

Raw OpenAPI specification endpoints:
- **JSON format**: `http://localhost:8081/v3/api-docs`
- **YAML format**: `http://localhost:8081/v3/api-docs.yaml`

These endpoints can be used to:
- Generate client SDKs
- Import into API testing tools (Postman, Insomnia, etc.)
- Integrate with API gateways
- Generate documentation in other formats

### API Endpoints

The service provides the following REST endpoints:

**User Management** (`/users`):
- `GET /users` - List all users (with optional firstName/lastName filters)
- `GET /users/{id}` - Get user by ID
- `POST /users` - Create a new user
- `PUT /users/{id}` - Full update of a user
- `PATCH /users/{id}` - Partial update of a user
- `DELETE /users/{id}` - Delete a user

**Service Info**:
- `GET /test` - Health check endpoint
- `GET /info` - Service information (name and version)

---

## 🐳 Docker Support

Included Dockerfile allows image creation directly from the fat JAR.

Manual build:
```bash
docker build -t gar2000b/xp-users-service:latest .
```

Run container:
```bash
docker run -p 8081:8081 gar2000b/xp-users-service:latest
```

---

## 🔁 Release Workflow (Automated Scripts)

This repo includes helper scripts to automate release + Docker publishing.

| Order | Script                  | Purpose |
|------|-------------------------|---------|
| 1️⃣ | `release-mvn-to-git.sh` | Executes Maven release to bump version, tag repo, and build JAR — then triggers Docker build + push |
| 2️⃣ | `build-docker-image.sh` | Builds Docker image using the latest fat JAR |
| 3️⃣ | `push-docker-image.sh`  | Pushes tagged Docker image(s) to Docker Hub |

All run automatically by:
```bash
./release.sh
```

---

## ⚠️ full-reset.sh — Emergency Use Only

Use this script when Maven release fails mid-process and the repo becomes messy.

It:
- Resets repo to `origin/main`
- Deletes untracked files + release leftovers
- Removes `target/`
- Restores clean working state

Run carefully — it **wipes local changes**:
```bash
./full-reset.sh
```

---

## 📌 Status

This is a **research prototype** — design, API, and data model may change rapidly.
