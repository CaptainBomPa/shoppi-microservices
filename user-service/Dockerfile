FROM openjdk:17-jdk-slim AS builder

WORKDIR /build

COPY . .

WORKDIR /build/auth-library
RUN chmod +x gradlew
RUN ./gradlew clean build publishToMavenLocal

WORKDIR /build/user-service
RUN chmod +x gradlew
RUN ./gradlew clean build -x test --refresh-dependencies

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /build/user-service/build/libs/user-service-*.jar /app/user-service.jar

ENV SPRING_PROFILES_ACTIVE=docker

RUN chmod +rwx /app/user-service.jar

CMD ["java", "-jar", "/app/user-service.jar"]
