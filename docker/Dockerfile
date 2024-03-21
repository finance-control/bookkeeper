FROM gradle:jdk17 AS build
WORKDIR /app
COPY ../. /app
RUN ./gradlew bootJar --no-daemon

FROM openjdk:17-alpine
COPY --from=build /app/app/build/libs/*.jar bookkeeper.jar
COPY docker/scripts/startup.sh startup.sh
ENTRYPOINT ["java", "-jar", "bookkeeper.jar"]
