FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN sh gradlew build --no-daemon
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/gameserver/build/libs/*.jar ./server.jar
EXPOSE 9000
CMD ["java", "-jar", "server.jar"]