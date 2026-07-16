# syntax=docker/dockerfile:1

# ---- Build stage: compile the Compose Multiplatform web app (JS + Wasm) ----
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Kotlin/Wasm's tooling setup downloads its own Node.js, which needs libatomic1
# (not present in this base image) or it fails with "libatomic.so.1: cannot open
# shared object file".
RUN apt-get update && apt-get install -y --no-install-recommends libatomic1 \
    && rm -rf /var/lib/apt/lists/*

# Cache Gradle dependencies separately from source changes
COPY gradle ./gradle
COPY settings.gradle.kts build.gradle.kts ./
COPY shared/build.gradle.kts shared/build.gradle.kts
COPY webApp/build.gradle.kts webApp/build.gradle.kts

COPY . .
# Repo is checked out on Windows, so gradlew may have CRLF line endings, which
# breaks its #!/bin/sh shebang under Linux ("./gradlew: not found"). COPY . .
# above re-copies gradlew from the build context, so this must run after it.
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
# Produces webApp/build/dist/composeWebCompatibility/productionExecutable
# (wasmJs build with automatic fallback to JS on browsers without WasmGC support)
RUN ./gradlew --no-daemon :webApp:composeCompatibilityBrowserDistribution

# ---- Runtime stage: serve the static bundle and proxy /api to the Go backend ----
FROM nginx:1.27-alpine
COPY deploy/nginx.conf.template /etc/nginx/templates/default.conf.template
COPY --from=build /app/webApp/build/dist/composeWebCompatibility/productionExecutable /usr/share/nginx/html

ENV BACKEND_HOST=host.docker.internal:8081
EXPOSE 80
