# Serviço de Gestão de Endereços (Trabalho T3B)

API RESTful desenvolvida em Java 17 (Jakarta EE 9 / Tomcat 10) para gestão centralizada de endereços, focada em integridade de dados e alta performance. O projeto utiliza PostgreSQL, é totalmente containerizado com Docker e segue uma arquitetura multicamadas estrita.

## 📋 Visão Geral para Consumidores da API

Esta API atua como um **Catálogo Mestre de Endereços Únicos**.

**Diferença Importante:** Diferente de cadastros comuns, este serviço armazena apenas a definição do logradouro (CEP + Cidade + Bairro + Rua). Dados específicos como **Número Predial** e **Complemento** (ex: "Apto 101") foram removidos deste escopo e devem ser geridos pelas aplicações clientes (ex: Módulo de Pessoas), referenciando o ID do Endereço gerado aqui.

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos
- **Docker** e **Docker Compose** instalados. (Não é necessário Java/Maven local).

### Passo Único
Abra o terminal na raiz do projeto e execute:

```bash
docker-compose up --build
```

O ambiente subirá os seguintes serviços:
1.  **Banco de Dados (PostgreSQL 15):** Porta 5432.
2.  **Back-end (Tomcat 10):** `http://localhost:8080/endereco`
3.  **Frontend (React/Vite):** `http://localhost:5173`
4.  **Testes Automatizados:** Rodam automaticamente na subida do container `testes`.

*Para resetar o banco de dados (limpar volumes):* `docker-compose down -v`

---

## 🔌 Documentação da API (Endpoints)

Todas as requisições e respostas são em JSON (UTF-8).

### 📍 Domínios (Auxiliares)
Endpoints de leitura para popular combos no Frontend.

| Método | Endpoint                     | Descrição                                                  |
|---|------------------------------|------------------------------------------------------------|
| GET | `/endereco/ufs`              | Lista todas as Unidades Federativas (Sigla/Nome) ordenadas |
| GET | `/endereco/tipos-logradouro` | Lista tipos (Rua, Avenida, Praça...) ordenados             |

### 🔍 Consultas de Endereço

#### 1. Consultar no ViaCEP (Externo)
Busca dados na base nacional. Retorna um objeto `Endereco` transitório (não salvo no banco).
- **GET** `/endereco/enderecos/externo/{cep}`
- **Exemplo:** `GET .../endereco/enderecos/externo/85867900`

#### 2. Consultar no Banco Local (Por CEP)
Retorna lista de endereços já cadastrados naquele CEP.
- **GET** `/endereco/enderecos/cep/{cep}`

#### 3. Buscar Endereço por ID
Recupera a árvore completa (Cidade, UF, Bairro) de um endereço.
- **POST** `/endereco/enderecos/buscar-id`
- **Body:** `{ "idEndereco": 1 }`

#### 4. Buscar Cidade por ID
- **POST** `/endereco/cidades/buscar-id`
- **Body:** `{ "idCidade": 5 }`

---

### 📝 Cadastro Inteligente (Orquestrado)

Este é o endpoint principal. Ele implementa o padrão **Idempotente**: se você tentar cadastrar um endereço que já existe (mesmo CEP, Cidade, Bairro e Logradouro), a API **não duplica** o registro e nem gera erro; ela retorna o endereço existente com seu ID.

Ele também realiza o cadastro em cascata: se a Cidade ou Bairro informados não existirem, são criados automaticamente.

- **POST** `/endereco/enderecos/cadastrar`
- **Body (JSON):**

```json
{
  "cep": "85867900",
  "cidade": {
    "nomeCidade": "Foz do Iguaçu",
    "unidadeFederativa": { "siglaUF": "PR" }
  },
  "bairro": {
    "nomeBairro": "Parque Tecnológico"
  },
  "logradouro": {
    "nomeLogradouro": "Tancredo Neves",
    "tipoLogradouro": { "nomeTipoLogradouro": "Avenida" }
  }
}
```

---

## 🏗️ Arquitetura e Decisões de Design

O sistema foi projetado seguindo rigorosamente a separação de responsabilidades em camadas Java puras (sem Spring), facilitando o entendimento do ciclo de vida da transação.

### 1. Camadas do Back-end
* **HTTP (Servlets):** Camada de fronteira. Recebe JSON, faz a desserialização segura e valida a presença básica dos dados. Delega para o Manager.
* **Manager (Service Facade):** O "Maestro". Responsável por abrir a conexão com o banco, iniciar a transação (`setAutoCommit(false)`), orquestrar a chamada sequencial aos Cols (Valida UF -> Obtém Cidade -> Obtém Bairro -> Salva Endereço) e realizar o `commit` ou `rollback`.
* **Col (Business Logic):** O "Especialista". Contém as regras de validação (Regex, Tamanho), formatação de texto e a lógica de "Obter ou Cadastrar". Não abre conexões; recebe a conexão ativa do Manager.
* **DAO (Data Access):** O "Executor". Executa SQL puro via JDBC. Totalmente passivo, apenas usa a conexão recebida.

### 2. Padrões e Soluções Técnicas
* **Idempotência (`obterOuCadastrar`):** Garante a integridade referencial e evita a poluição do banco com duplicatas.
* **Sanitização de Dados (`TextoUtil`):**
    * **Title Case Inteligente:** Formata nomes respeitando a gramática brasileira (ex: "Foz **do** Iguaçu" vs "Praça **da** Sé" vs "**D'Oeste**").
    * **Validação Regex:** Bloqueia caracteres maliciosos (SQL Injection) e garante que apenas letras/números válidos sejam persistidos.
* **Conexão Híbrida:** A classe `ConexaoBD` detecta o ambiente. Se estiver no Tomcat, usa **JNDI** (Pool gerenciado). Se estiver em testes unitários (Maven), faz fallback para **JDBC Direto** no Docker.
* **Fail-Fast:** O Manager valida a integridade estrutural do objeto (ex: "Tem Cidade?") **antes** de abrir a conexão com o banco, economizando recursos.

## 🛠️ Estrutura Modular (Maven)

- **MyInfraAPI:** Utilitários de infraestrutura (Conexão BD, ViaCEP).
- **MyEnderecoBO:** Entidades de Domínio (POJOs).
- **MyEnderecoServicos:** Regras de Negócio, DAOs e API HTTP (Gera o WAR).
- **MyEnderecoTeste:** Testes de Integração (JUnit 5) com ordenação de execução para garantir cenários consistentes.