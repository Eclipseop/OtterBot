FROM maven:3.5-jdk-8-alpine AS build
WORKDIR /app
COPY OtterBot/pom.xml /app
RUN mvn dependency:go-offline

COPY OtterBot/src /app/src
RUN mvn package

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=build /app/target/ /app
ENTRYPOINT ["java","-jar","/app/OtterBot-1.1.3-jar-with-dependencies.jar","google key", "discord key"]
