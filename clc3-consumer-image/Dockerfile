FROM azul/zulu-openjdk-alpine:8
COPY jhawk-command-line jhawk-command-line
COPY clc3-consumer.jar app.jar
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
