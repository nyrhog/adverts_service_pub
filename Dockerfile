# syntax=docker/dockerfile:1
#FROM gradle:7.1.0-jdk16 AS build
#COPY --chown=gradle:gradle . /home/gradle/src
#WORKDIR /home/gradle/src
#RUN gradle build --no-daemon

FROM openjdk:16-alpine3.13
#VOLUME /tmp
ENV TZ="Europe/Moscow"
COPY build/libs/adverts_service-0.0.1-SNAPSHOT.jar adverts_service-0.0.1-SNAPSHOT.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "adverts_service-0.0.1-SNAPSHOT.jar"]