FROM gradle:jdk21 AS build
WORKDIR /app
COPY ./ /app
RUN echo ls -la
RUN ./gradlew :app:bootJar --no-daemon

FROM openjdk:21
COPY --from=build /app/app/build/libs/*.jar bookkeeper.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bookkeeper.jar", "--spring.profiles.active=", "${APP_PROFILE}"]
