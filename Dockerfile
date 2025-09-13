# Build stage using an image with JDK 21
FROM eclipse-temurin:21-jdk-alpine AS build

# Switch to the working directory
WORKDIR /opt/app

# Set spring profiles build argument
ARG SPRING_PROFILE
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILE}

# Copy files
COPY pom.xml ./
COPY mvnw ./
COPY .mvn/ .mvn/
COPY src/ ./src/

# Build the jar file
RUN ./mvnw clean package

# Final stage using an image with JRE 21
FROM eclipse-temurin:21-jre-alpine AS final

# Switch to the working directory
WORKDIR /opt/app

# Copy the jar from the build stage
COPY --from=build /opt/app/target/*.jar /opt/app.jar

# Expose the app's port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
