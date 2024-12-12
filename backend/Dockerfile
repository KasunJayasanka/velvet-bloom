# Stage 1: Build the JAR file
FROM maven:3.9.5-eclipse-temurin-21 AS builder

# Set working directory for the build
WORKDIR /app

# Use a writable Maven repository cache
ENV MAVEN_CONFIG=/app/.m2

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: Create the final image with JAR file
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/velvetbloom-0.0.1-SNAPSHOT.jar app.jar

# Copy the base64-encoded service account key
COPY src/main/resources/gcsServiceAccountKey.base64 /app/gcsServiceAccountKey.base64

# Create the directory for the decoded file
RUN mkdir -p /app/src/main/resources

# Decode the base64 file and write the result to gcsServiceAccountKey.json
RUN base64 -d /app/gcsServiceAccountKey.base64 > /app/src/main/resources/gcsServiceAccountKey.json

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
