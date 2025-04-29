# Use an official JDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/LearningSystem-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (Make sure this matches your app's config)
EXPOSE 9090

# Run the application
CMD ["java", "-jar", "app.jar"]
