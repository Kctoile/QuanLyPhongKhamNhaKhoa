FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM tomcat:10.1-jdk17
COPY --from=build /app/target/QuanLyPhongKhamNhaKhoa.war /usr/local/tomcat/webapps/phongkhamnhakhoa.war
ADD https://jdbc.postgresql.org/download/postgresql-42.7.13.jar /usr/local/tomcat/lib/postgresql.jar
EXPOSE 8080
CMD ["catalina.sh", "run"]
