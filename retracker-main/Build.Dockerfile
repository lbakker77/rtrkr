FROM maven:3-amazoncorretto-23
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src
# Build the application using Maven
RUN mvn clean package -DskipTests
# Use an official OpenJDK image as the base image
FROM amazoncorretto:23-headless
# Set the working directory in the container
WORKDIR /app
# Copy the built JAR file from the previous stage to the container
COPY - from=build /app/target/my-application.jar .
# Set the command to run the application
CMD ["java", "-jar", "my-application.jar"]