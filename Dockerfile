FROM gradle:8.12.1-jdk23-alpine AS build
WORKDIR /home/gradle/project
COPY . .
RUN gradle clean build --no-daemon

FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]