FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY MyInfraAPI/pom.xml MyInfraAPI/
COPY MyEnderecoBO/pom.xml MyEnderecoBO/
COPY MyEnderecoServicos/pom.xml MyEnderecoServicos/
COPY MyEnderecoTeste/pom.xml MyEnderecoTeste/

COPY MyInfraAPI/src MyInfraAPI/src
COPY MyEnderecoBO/src MyEnderecoBO/src
COPY MyEnderecoServicos/src MyEnderecoServicos/src
COPY MyEnderecoTeste/src MyEnderecoTeste/src

RUN mvn clean install -DskipTests

FROM tomcat:10.1.19-jdk17

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY --from=build /app/MyEnderecoServicos/target/MyEnderecoServicos-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/api.war

EXPOSE 8080