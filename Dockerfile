FROM tomcat:10.1.44-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

# Copy thư mục "web" của bạn vào ROOT
COPY web/ /usr/local/tomcat/webapps/ROOT/

EXPOSE 8080
CMD ["catalina.sh", "run"]
