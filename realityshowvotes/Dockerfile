FROM openjdk:14-jdk-alpine

ENV DOCKERIZE_VERSION v0.7.0

RUN apk update --no-cache \
    && apk add --no-cache wget openssl \
    && wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && apk del wget

ARG JAR_FILE=/target/reality-show-votes-consumer.jar
COPY $JAR_FILE app.jar
ENTRYPOINT ["dockerize", "-wait", "tcp://mongodb-service:27017", "-timeout=40s", "-wait", "tcp://rabbitmq-service:5672", "-timeout=40s", "java", "-jar", "/app.jar"]