# Build stage using an Ubuntu 24.04 image with JDK 21
FROM eclipse-temurin:21-jdk-noble AS build

# Switch to the working directory
WORKDIR /opt/app

# Copy files
COPY pom.xml ./
COPY mvnw ./
COPY .mvn/ .mvn/
COPY src/ ./src/

# Build the jar file
RUN ./mvnw clean package

# Final stage using an Ubuntu 24.04 image with JRE 21
FROM eclipse-temurin:21-jdk-noble AS final

# Switch to the working directory
WORKDIR /opt/app

# Copy the jar from the build stage
COPY --from=build /opt/app/target/*.jar /opt/app.jar

# Expose the app's port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
