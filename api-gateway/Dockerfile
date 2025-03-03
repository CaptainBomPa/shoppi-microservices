FROM openjdk:17-jdk-slim AS builder

WORKDIR /build

COPY . .

WORKDIR /build/auth-library
RUN chmod +x gradlew
RUN ./gradlew clean build publishToMavenLocal

WORKDIR /build/api-gateway
RUN chmod +x gradlew
RUN ./gradlew clean build -x test --refresh-dependencies

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /build/api-gateway/build/libs/api-gateway-*.jar /app/api-gateway.jar

ENV SPRING_PROFILES_ACTIVE=docker

RUN chmod +rwx /app/api-gateway.jar

CMD ["java", "-jar", "/app/api-gateway.jar"]
