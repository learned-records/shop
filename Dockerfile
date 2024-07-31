FROM openjdk:21-jdk-slim AS JAR_BUILDER

WORKDIR /app
COPY . /app

RUN ./gradlew bootJar

FROM amazoncorretto:21-alpine AS JRE_BUILDER

RUN apk add --no-cache binutils
RUN jlink \
      --verbose \
      --add-modules java.base,java.compiler,java.desktop,java.instrument,java.management,java.naming,java.prefs,java.rmi,java.scripting,java.security.jgss,java.sql,jdk.httpserver,jdk.jfr,jdk.unsupported,jdk.crypto.ec,jdk.crypto.cryptoki \
      --strip-debug \
      --no-man-pages \
      --no-header-files \
      --compress=2 \
      --output /jre

FROM alpine:3.20

ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=JRE_BUILDER /jre $JAVA_HOME

ARG APPLICATION_USER=appuser
RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER
RUN mkdir /app && chown -R $APPLICATION_USER /app
USER 1000

ENV PROFILE=${ACTIVE}

ARG JAR_FILE=/app/build/libs/*jar
COPY --chown=1000:1000 --from=JAR_BUILDER ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-jar", "app.jar"]
