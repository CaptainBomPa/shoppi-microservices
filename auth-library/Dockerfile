FROM openjdk:17-jdk-slim AS auth-builder

WORKDIR /app
COPY . .
RUN ./gradlew clean build publishToMavenLocal
