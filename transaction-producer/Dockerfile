FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/transaction-producer-0.0.1-SNAPSHOT.jar transaction-producer.jar
ENTRYPOINT ["java", "-jar", "/transaction-producer.jar"]
