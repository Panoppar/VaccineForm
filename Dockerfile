# syntax=docker/dockerfile:1

# ---- Build stage: compile the Compose Multiplatform web app (JS + Wasm) ----
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends libatomic1 \
    && rm -rf /var/lib/apt/lists/*

COPY gradle ./gradle
COPY settings.gradle.kts build.gradle.kts ./
COPY shared/build.gradle.kts shared/build.gradle.kts
COPY webApp/build.gradle.kts webApp/build.gradle.kts

COPY . .

RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# -------------------------------------------------------------------------
# [จุดที่ต้องแก้] เพิ่ม --mount เพื่อเมานต์ไฟล์ local.properties เข้าไปชั่วคราว
# Docker จะดึงไฟล์ความลับมาวางไว้ที่เป้าหมาย (target=/app/local.properties)
# พอคำสั่งรันเสร็จ ไฟล์นี้จะหายไปจาก Image ทันที ปลอดภัย 100%
# -------------------------------------------------------------------------
RUN --mount=type=secret,id=local_props,target=/app/local.properties \
    ./gradlew --no-daemon :webApp:composeCompatibilityBrowserDistribution

# ---- Runtime stage: serve the static bundle and proxy /api to the Go backend ----
FROM nginx:1.27-alpine
COPY deploy/nginx.conf.template /etc/nginx/templates/default.conf.template
COPY --from=build /app/webApp/build/dist/composeWebCompatibility/productionExecutable /usr/share/nginx/html

ENV BACKEND_HOST=host.docker.internal:8081
EXPOSE 80