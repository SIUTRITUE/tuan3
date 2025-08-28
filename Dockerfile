# Tomcat 10.1 (Jakarta) + JDK 17
FROM tomcat:10.1.44-jdk17

# Xoá các webapp mặc định (ROOT, docs, examples...)
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy web exploded của bạn vào ROOT (context path = "/")
COPY webapp/ /usr/local/tomcat/webapps/ROOT/

EXPOSE 8080
CMD ["catalina.sh", "run"]
