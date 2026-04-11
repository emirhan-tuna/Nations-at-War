FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN --mount=type=cache,target=/root/.gradle sh gradlew :gameserver:shadowJar --no-daemon
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/gameserver/build/libs/server.jar ./server.jar
EXPOSE 9000
CMD ["java", "-jar", "server.jar"]