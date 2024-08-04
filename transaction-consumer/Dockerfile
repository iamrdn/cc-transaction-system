FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/transaction-consumer-0.0.1-SNAPSHOT.jar transaction-consumer.jar
ENTRYPOINT ["java", "-jar", "/transaction-consumer.jar"]
