FROM openjdk:17-jdk-slim AS builder

WORKDIR /build

COPY . .

WORKDIR /build/auth-library
RUN chmod +x gradlew
RUN ./gradlew clean build publishToMavenLocal

WORKDIR /build/contract-product
RUN chmod +x gradlew
RUN ./gradlew clean build publishToMavenLocal

WORKDIR /build/search-service
RUN chmod +x gradlew
RUN ./gradlew clean build -x test --refresh-dependencies

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /build/search-service/build/libs/search-service-*.jar /app/search-service.jar

ENV SPRING_PROFILES_ACTIVE=docker

RUN chmod +rwx /app/search-service.jar

CMD ["java", "-jar", "/app/search-service.jar"]
