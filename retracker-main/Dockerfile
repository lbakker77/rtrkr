FROM maven:3-amazoncorretto-21 AS builder
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src
# Build the application using Maven
RUN mvn clean package -DskipTests
RUN cp ./target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract



FROM amazoncorretto:21
# Set the working directory in the container
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
