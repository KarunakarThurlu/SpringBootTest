FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE=target/SpringBootTest.jar

WORKDIR /karun/Docker

COPY ${JAR_FILE} app.jar

ENTRYPOINT [ "java","-jar","app.jar" ]
