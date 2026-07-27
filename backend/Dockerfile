FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/jobboard-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
CMD ["sh", "-c", "exec java -XX:MaxRAMPercentage=75 -Dserver.port=${PORT:-8084} -jar app.jar"]
