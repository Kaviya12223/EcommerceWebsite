# Stage 1: Build the application
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
# Add this line to make mvnw executable
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]