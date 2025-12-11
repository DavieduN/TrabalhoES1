FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
COPY MyInfraAPI/pom.xml MyInfraAPI/
COPY MyEnderecoBO/pom.xml MyEnderecoBO/
COPY MyEnderecoServicos/pom.xml MyEnderecoServicos/
COPY MyEnderecoTeste/pom.xml MyEnderecoTeste/

RUN mvn dependency:go-offline -B -DfailIfNoTests=false

COPY MyInfraAPI/src MyInfraAPI/src
COPY MyEnderecoBO/src MyEnderecoBO/src
COPY MyEnderecoServicos/src MyEnderecoServicos/src
COPY MyEnderecoTeste/src MyEnderecoTeste/src

RUN mvn package -DskipTests -DfailIfNoTests=false

FROM tomcat:10.1-jdk17
WORKDIR /usr/local/tomcat

RUN rm -rf webapps/*

COPY --from=builder /app/MyEnderecoServicos/target/endereco.war webapps/endereco.war

EXPOSE 8080

CMD ["catalina.sh", "run"]