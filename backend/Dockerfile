# Stage 1: Build compile stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime image stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/jobboard-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
CMD ["sh", "-c", "exec java -XX:MaxRAMPercentage=75 -Dserver.port=${PORT:-8084} -jar app.jar"]
