# ========================================
# 1️⃣ BUILD STAGE (Maven + OpenJDK 25)
# ========================================
FROM maven:3.9.6-eclipse-temurin-25 AS build
WORKDIR /app

# Copy pom.xml first (cache dependencies)
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copy source code & build
COPY src ./src
RUN mvn -q clean package -DskipTests

# ========================================
# 2️⃣ RUNTIME STAGE (Small OpenJDK 25 JRE)
# ========================================
FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]
