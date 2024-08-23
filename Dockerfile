### build stage ###
FROM gradle:8.8-jdk21 AS builder
RUN mkdir /usr/src/app
COPY --chown=gradle:gradle . /usr/src/app
WORKDIR /usr/src/app
RUN gradle build --no-daemon -x test

FROM openjdk:21-alpine
ARG JAR_FILE=/usr/src/app/build/libs/*.jar
ENV JAVA_OPTS=""
COPY --from=builder ${JAR_FILE} app.jar
ENTRYPOINT java ${JAVA_OPTS} -jar /app.jar
