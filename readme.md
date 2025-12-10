# Serviço de Gerenciamento de Endereços (Trabalho T3B)

API RESTful desenvolvida em Java (Jakarta EE / Servlets) para gerenciamento de endereços, com integração ao ViaCEP e persistência em PostgreSQL. O projeto é containerizado usando Docker e segue uma arquitetura multicamadas.

## 📋 Pré-requisitos

- **Docker** e **Docker Desktop** (ou Docker Engine no Linux) instalados e rodando.
- Não é necessário ter Java ou Maven instalados na máquina local (o Docker cuida disso).

---

## 🚀 Como Rodar o Projeto

### 1. Iniciar a Aplicação
Abra o terminal na raiz do projeto e execute:

```bash
docker-compose up --build
```

Isso irá:
1. Compilar o código Java e rodar os testes unitários (JUnit 5).
2. Subir o banco de dados PostgreSQL.
3. Iniciar o servidor Tomcat na porta **8080**.

*Aguarde até aparecer a mensagem de que o Tomcat iniciou no terminal.*

### 2. Parar a Aplicação e Limpar Dados
Para parar e **remover o banco de dados** (resetar para o estado inicial), execute:

```bash
docker-compose down -v
```

---

## 🔌 Documentação da API

A API responde em `http://localhost:8080`. Todas as respostas são em JSON (UTF-8).

### 📍 Domínios (Listas para ComboBox)
Use estes endpoints para preencher as opções de seleção no Front-End.

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/ufs` | Lista todas as Unidades Federativas (UF) |
| GET | `/api/cidades` | Lista todas as Cidades cadastradas |
| GET | `/api/bairros` | Lista todos os Bairros cadastrados |
| GET | `/api/tipos-logradouro` | Lista Tipos (Rua, Av, Travessa...) |
| GET | `/api/logradouros` | Lista todos os Logradouros (nomes de ruas) |

### 🔍 Consultas de Endereço

#### 1. Consultar no ViaCEP (Externo)
Busca dados na nuvem para pré-preencher o formulário. Retorna um objeto `EnderecoEspecifico` semi-preenchido.
- **GET** `/api/enderecos/externo/{cep}`
- **Exemplo:** `GET http://localhost:8080/api/enderecos/externo/85867900`

#### 2. Consultar no Banco Local (Por CEP)
Retorna uma lista de endereços específicos cadastrados naquele CEP.
- **GET** `/api/enderecos/cep/{cep}`
- **Exemplo:** `GET http://localhost:8080/api/enderecos/cep/85867900`

#### 3. Buscar Endereço Específico por ID
Recupera um endereço completo dado o ID do registro específico.
- **POST** `/api/enderecos/buscar-id`
- **Body (JSON):**
```json
{
  "idEnderecoEspecifico": 1
}
```

#### 4. Buscar Cidade por ID
Recupera o objeto cidade completo.
- **POST** `/api/cidades/buscar-id`
- **Body (JSON):**
```json
{
  "idCidade": 5
}
```

---

### 📝 Cadastro de Endereço

Este é o endpoint principal. Ele recebe o objeto montado e realiza a persistência em cascata (salva UF, Cidade, Bairro e Logradouro se não existirem, evitando duplicidades).

- **POST** `/api/enderecos/cadastrar`
- **Body (JSON):**

```json
{
  "numero": "1000",
  "complemento": "Bloco B - Sala 2",
  "endereco": {
    "cep": "85867900",
    "cidade": {
      "nomeCidade": "Foz do Iguaçu",
      "unidadeFederativa": {
        "siglaUF": "PR"
      }
    },
    "bairro": {
      "nomeBairro": "Parque Tecnológico"
    },
    "logradouro": {
      "nomeLogradouro": "Tancredo Neves",
      "tipoLogradouro": {
        "nomeTipoLogradouro": "Avenida"
      }
    }
  }
}
```

---

## 🛠️ Estrutura do Projeto

O projeto é modularizado com Maven:

- **MyInfraAPI:** Conexão com Banco de Dados (Híbrida: Properties + Docker ENV).
- **MyEnderecoBO:** Objetos de Negócio (Entidades).
- **MyEnderecoServicos:** Lógica de Negócio (Cols), Acesso a Dados (DAOs), Gerenciador (Manager) e API (Servlets).
- **MyEnderecoTeste:** Testes de Integração automatizados com JUnit 5.