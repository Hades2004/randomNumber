FROM eclipse-temurin:25-jdk-alpine
COPY target/demo.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]