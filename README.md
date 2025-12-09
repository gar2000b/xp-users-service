# xp-users-service

A lightweight **experimental** Spring Boot microservice for managing user accounts across various research and prototype systems.  
This service is part of the broader **XP (eXPerimental Platform)** ecosystem.

---

## �� Features
- Spring Boot 4.x (modern stack)
- Java 21 runtime
- Self-contained executable **fat JAR**
- REST-ready for future endpoints
- Docker image build + push support
- Actuator endpoints enabled for monitoring

---

## �� Tech Stack

| Component | Choice |
|----------|--------|
| Language | Java 21 |
| Framework | Spring Boot |
| Build Tool | Maven |
| Packaging | Fat JAR |
| Deployment | Docker |

---

## �� Building & Running

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
http://localhost:8080
```

Health endpoint:
```
http://localhost:8080/actuator/health
```

---

## �� Docker Support

Included Dockerfile allows image creation directly from the fat JAR.

Example manual build:
```bash
docker build -t gar2000b/xp-users-service:latest .
```

Run container:
```bash
docker run -p 8080:8080 gar2000b/xp-users-service:latest
```

---

## �� Release Lifecycle Scripts

This repo includes helper scripts to automate release + Docker image publishing.  
**Order matters** — always execute them in this sequence:

| Order | Script | Purpose |
|------|--------|---------|
| 1️⃣ | `release.sh` | Runs Maven release plugin to bump version, tag repo, and build JAR |
| 2️⃣ | `build-docker-image.sh` | Builds Docker image using latest built JAR |
| 3️⃣ | `push-docker-image.sh` | Pushes tagged image(s) to Docker Hub |

---

## ⚠️ full-reset.sh

Script:
- Resets repo to `origin/main`
- Removes untracked files (+ release debris)
- Deletes `target/`
- Ensures clean state for future releases

Use when:
- The maven release process fails mid-way
- You get unremovable `target/checkout/.git/...` files
- Git becomes inconsistent with remote

Run carefully — it wipes local changes:
```bash
./full-reset.sh
```

---

## �� Status

This is a **research service** — design, APIs, and data model will evolve rapidly.

