FROM eclipse-temurin:25-jre-jammy
ARG JAR_FILE=target/*.jar
RUN useradd -r -u 10001 appuser
COPY ${JAR_FILE} app.jar
USER appuser
ENTRYPOINT ["java", "-jar", "/app.jar"]