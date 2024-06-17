FROM eclipse-temurin:17-alpine

# copy the fat jar and the run script to /opt
WORKDIR /app
COPY target/*.jar /opt/service.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/customerAccountServiceApplication.jar"]

