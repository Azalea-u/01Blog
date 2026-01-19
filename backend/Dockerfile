# Stage 1: Build the Spring Boot app
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml first (for dependency caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (caching layer)
RUN ./mvnw dependency:go-offline -B || true

# Copy the source code
COPY src ./src

# Build the project
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the Spring Boot app
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]
