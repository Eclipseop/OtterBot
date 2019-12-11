FROM alpine/git AS clone
WORKDIR /app
RUN git clone https://github.com/Eclipseop/OtterBot.git

FROM maven:3.5-jdk-8-alpine AS build
WORKDIR /app
COPY --from=clone /app/OtterBot /app
RUN mvn clean package

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=build /app/target/ /app
ENTRYPOINT ["java","-jar","/app/OtterBot-1.1.3-jar-with-dependencies.jar","google key", "discord key"]
