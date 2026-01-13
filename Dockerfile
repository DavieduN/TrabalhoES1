FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# 1. Copia o POM Pai e os POMs dos módulos
# A ordem aqui não afeta a lógica do Maven, desde que todos estejam presentes antes do 'go-offline'
COPY pom.xml .
COPY MyInfraAPI/pom.xml MyInfraAPI/
COPY MyEnderecoBO/pom.xml MyEnderecoBO/
COPY MyEnderecoServicos/pom.xml MyEnderecoServicos/
COPY MyEnderecoTeste/pom.xml MyEnderecoTeste/
COPY MyPessoaBO/pom.xml MyPessoaBO/
COPY MyPessoaServicos/pom.xml MyPessoaServicos/
COPY MyAluguelBO/pom.xml MyAluguelBO/
COPY MyAluguelServicos/pom.xml MyAluguelServicos/

# 2. Baixa as dependências (Cache Layer)
# O -DfailIfNoTests=false é vital. O --fail-never ajuda a ignorar erros de resolução parciais nessa etapa de cache
RUN mvn dependency:go-offline -B -DfailIfNoTests=false || true

# 3. Copia o código fonte de TODOS os módulos
# É crucial copiar TUDO antes de rodar o 'package', pois o Reactor precisa ver todos os fontes para compilar na ordem certa
COPY MyInfraAPI/src MyInfraAPI/src
COPY MyEnderecoBO/src MyEnderecoBO/src
COPY MyEnderecoServicos/src MyEnderecoServicos/src
COPY MyEnderecoTeste/src MyEnderecoTeste/src
COPY MyPessoaBO/src MyPessoaBO/src
COPY MyPessoaServicos/src MyPessoaServicos/src
COPY MyAluguelBO/src MyAluguelBO/src
COPY MyAluguelServicos/src MyAluguelServicos/src

# 4. Compila Tudo (Package)
# O Maven Reactor vai detectar a ordem: MyPessoaBO -> MyPessoaServicos -> MyAluguelServicos
RUN mvn package -DskipTests -DfailIfNoTests=false

# --- Etapa 2: Runtime (Servidor) ---
FROM tomcat:10.1-jdk17
WORKDIR /usr/local/tomcat

# Limpa apps padrão
RUN rm -rf webapps/*

# Deploy dos WARs gerados
# O caminho '/app/...' refere-se à pasta definida no WORKDIR do builder
COPY --from=builder /app/MyEnderecoServicos/target/endereco.war webapps/endereco.war
COPY --from=builder /app/MyAluguelServicos/target/aluguel.war webapps/aluguel.war

EXPOSE 8080
CMD ["catalina.sh", "run"]