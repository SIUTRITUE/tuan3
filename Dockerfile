FROM maven:3.9.4-openjdk-17 AS build

# Copy pom.xml và source code
COPY pom.xml /app/
COPY src/ /app/src/
COPY web/ /app/web/

WORKDIR /app

# Build với Maven (sẽ download Jakarta Mail dependencies)
RUN mvn clean package -DskipTests

# Stage 2: Runtime với Tomcat
FROM tomcat:10.1.44-jdk17

# Xóa webapps mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy war file đã build từ stage 1
COPY --from=build /app/target/tuan3-survey-email.war /usr/local/tomcat/webapps/ROOT.war

# Hoặc copy trực tiếp thư mục đã build
# COPY --from=build /app/target/tuan3-survey-email/ /usr/local/tomcat/webapps/ROOT/

EXPOSE 8080
CMD ["catalina.sh", "run"]
