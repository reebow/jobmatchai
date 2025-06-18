# Use a multi-stage build to optimize the image size
# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy Gradle wrapper and configuration files
COPY gradlew gradlew.bat ./
COPY build.gradle settings.gradle ./
COPY src/ ./src/
COPY .gitignore ./

# Copy the entire gradle directory instead of individual files
COPY gradle/ ./gradle/

# Build the application
RUN ./gradlew bootJar

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Create a non-root user
RUN useradd -m appuser

# Copy the built jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Set explicit permissions for the application file
RUN chown appuser:appuser /app/app.jar

# Switch to the non-root user
USER appuser

# Expose the application port
EXPOSE 8081

# Use environment variables for configuration
ENV SERVER_PORT=8081
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${SERVER_PORT}"]
