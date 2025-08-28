FROM tomcat:10.1.44-jdk17

# Xóa webapps mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# 1) Copy web (JSP/HTML + web.xml) vào ROOT
COPY web/ /usr/local/tomcat/webapps/ROOT/

# 2) Copy source Java và biên dịch vào WEB-INF/classes
COPY src/java/ /opt/src/

# Tomcat 10 có servlet-api trong thư mục lib; dùng nó làm classpath khi compile
RUN find /opt/src -name "*.java" > /tmp/sources.txt && \
    javac --release 11 -encoding UTF-8 \
      -cp /usr/local/tomcat/lib/servlet-api.jar \
      -d /usr/local/tomcat/webapps/ROOT/WEB-INF/classes \
      @/tmp/sources.txt

EXPOSE 8080
CMD ["catalina.sh", "run"]
