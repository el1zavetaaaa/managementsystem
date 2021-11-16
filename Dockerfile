FROM openjdk:16
EXPOSE 8080
ARG JAR_FILE=target/managementSystem.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]