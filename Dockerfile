# Use official Maven image to build the app
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

# Build the Spring Boot app
RUN mvn clean package -DskipTests

# Use a lightweight JDK image to run the app
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/sdcart-0.0.1-SNAPSHOT.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
