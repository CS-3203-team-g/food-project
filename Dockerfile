# Stage 1: Build
FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app
# ...existing code: copy entire project...
COPY . /app
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:11-jre-slim
WORKDIR /app
# ...existing code: copy jar from build stage...
COPY --from=build /app/target/PantryPilot.jar /app/PantryPilot.jar
EXPOSE 8080
CMD ["java", "-jar", "PantryPilot.jar"]
