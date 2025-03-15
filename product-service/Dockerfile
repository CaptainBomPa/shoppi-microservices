FROM openjdk:17-jdk-slim AS builder

WORKDIR /build

COPY . .

WORKDIR /build/auth-library
RUN chmod +x gradlew
RUN ./gradlew clean build publishToMavenLocal

WORKDIR /build/product-service
RUN chmod +x gradlew
RUN ./gradlew clean build -x test --refresh-dependencies

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /build/product-service/build/libs/product-service-*.jar /app/product-service.jar

ENV SPRING_PROFILES_ACTIVE=docker

RUN chmod +rwx /app/product-service.jar

CMD ["java", "-jar", "/app/product-service.jar"]
