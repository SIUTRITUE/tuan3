FROM openjdk:17-jdk-slim AS build

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src/ src/
COPY web/ web/

# Build with Maven - sẽ tự động download Jakarta Mail dependencies
RUN mvn clean package -DskipTests

# Runtime stage
FROM tomcat:10.1.44-jdk17

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy built WAR file
COPY --from=build /app/target/tuan3-survey-email.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
