FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew :gameserver:build --no-daemon
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/gameserver/build/libs/*.jar ./server.jar
EXPOSE 9000
CMD ["java", "-jar", "server.jar"]