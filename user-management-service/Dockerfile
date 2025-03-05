FROM openjdk:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} identity-service.jar

ENTRYPOINT ["java", "-jar", "identity-service.jar", "--spring.profiles.active=pro"]

EXPOSE 8080