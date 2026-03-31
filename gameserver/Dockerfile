FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew :server:build
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/server/build/libs/server.jar ./server.jar
EXPOSE 9000
CMD ["java", "-jar", "server.jar"]