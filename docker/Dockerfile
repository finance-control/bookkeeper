FROM gradle:jdk21 AS build
WORKDIR /app
COPY ../. /app
RUN ./gradlew bootJar --no-daemon

FROM openjdk:21
COPY --from=build /app/app/build/libs/*.jar bookkeeper.jar
COPY docker/scripts/startup.sh startup.sh
ENTRYPOINT ["java", "-jar", "bookkeeper.jar"]
