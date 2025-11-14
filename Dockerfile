# Run this project with java 21

# ---------- Stage 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (cached)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy only the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Render's port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]