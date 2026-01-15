FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
COPY MyInfraAPI/pom.xml MyInfraAPI/
COPY MyEnderecoBO/pom.xml MyEnderecoBO/
COPY MyEnderecoServicos/pom.xml MyEnderecoServicos/
COPY MyEnderecoTeste/pom.xml MyEnderecoTeste/
COPY MyPessoaBO/pom.xml MyPessoaBO/
COPY MyPessoaServicos/pom.xml MyPessoaServicos/
COPY MyAluguelBO/pom.xml MyAluguelBO/
COPY MyAluguelServicos/pom.xml MyAluguelServicos/
COPY MyOrdemServicoBO/pom.xml MyOrdemServicoBO/
COPY MyOrdemServicoServicos/pom.xml MyOrdemServicoServicos/

RUN mvn dependency:go-offline -B -DfailIfNoTests=false || true

COPY MyInfraAPI/src MyInfraAPI/src
COPY MyEnderecoBO/src MyEnderecoBO/src
COPY MyEnderecoServicos/src MyEnderecoServicos/src
COPY MyEnderecoTeste/src MyEnderecoTeste/src
COPY MyPessoaBO/src MyPessoaBO/src
COPY MyPessoaServicos/src MyPessoaServicos/src
COPY MyAluguelBO/src MyAluguelBO/src
COPY MyAluguelServicos/src MyAluguelServicos/src
COPY MyOrdemServicoBO/src MyOrdemServicoBO/src
COPY MyOrdemServicoServicos/src MyOrdemServicoServicos/src

RUN mvn package -DskipTests -DfailIfNoTests=false

FROM tomcat:10.1-jdk17
WORKDIR /usr/local/tomcat
RUN rm -rf webapps/*

COPY --from=builder /app/MyEnderecoServicos/target/endereco.war webapps/endereco.war
COPY --from=builder /app/MyAluguelServicos/target/aluguel.war webapps/aluguel.war
COPY --from=builder /app/MyOrdemServicoServicos/target/os.war webapps/os.war

EXPOSE 8080
CMD ["catalina.sh", "run"]