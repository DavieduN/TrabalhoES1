# Sistema Integrado de Gestão (Trabalhos T3B, T4A e T4B)

Este repositório contém a implementação de três módulos integrados de Engenharia de Software, desenvolvidos em Java (Jakarta EE / Servlets) com arquitetura multicamadas, persistência em PostgreSQL e containerização Docker.

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos
- **Docker** e **Docker Compose** instalados.

### 1. Iniciar a Aplicação
Abra o terminal na raiz do projeto e execute:

```bash
docker-compose up --build
```

O ambiente subirá os seguintes serviços:
1.  **Banco de Dados (PostgreSQL 15):** Porta 5432.
2.  **Back-end (Tomcat 10):** Porta 8080 (Hospedando 3 WARs distintos).
3.  **Front-ends (React):** Portas 5173, 5174 e 5175.
4.  **Testes:** Executados automaticamente na subida.

### 2. Limpar Dados
Para parar e **resetar o banco de dados** (apagar volumes), execute:

```bash
docker-compose down -v
```

---

## 🏗️ Arquitetura e Implementação

O sistema segue uma arquitetura modular estrita para garantir reuso de código e integridade transacional.

### Módulos Maven
* **MyInfraAPI:** Conexão com Banco (Híbrida JNDI/JDBC) e utilitários HTTP.
* **MyEndereco (Core):** Gestão de Endereços, Cidades e Bairros.
* **MyPessoa (Core Genérico):** Lógica abstrata para Pessoas Físicas, validação de CPF, formatação de nomes e gestão de contatos (Telefone/Email).
* **MyAluguel (T4A):** Implementação concreta de Cliente e regras de aluguel de equipamentos.
* **MyOrdemServico (T4B):** Implementação concreta de Atendente e regras de OS.

### Padrões de Projeto Utilizados
* **Manager (Service Facade):** Orquestra a transação (Atomicidade), gerenciando a conexão e delegando para os Cols.
* **Col (Business Logic):** Validações, regras de negócio e verificação de duplicidade (Idempotência).
* **DAO (Data Access):** Execução pura de SQL recebendo a conexão aberta.
* **Fail-Fast:** Validações estruturais ocorrem antes de abrir conexões com o banco.

---

## 🔌 Catálogo de Serviços (API)

A aplicação roda no Tomcat na porta **8080**, dividida em 3 contextos (WARs).

### 📍 T3B - Gestão de Endereços
**Contexto:** `http://localhost:8080/endereco`

| SERVIÇO | Descrição | Entrada (JSON) | Saída (JSON) | Endereço (Método) |
| :--- | :--- | :--- | :--- | :--- |
| **Listar UFs** | Lista Unidades Federativas para combo. | - | `[{"siglaUF": "PR", "nomeUF": "Paraná"}, ...]` | `/ufs`<br>**(GET)** |
| **Listar Tipos Log.** | Lista Rua, Avenida, etc. | - | `[{"idTipoLogradouro": 1, "nomeTipoLogradouro": "Rua"}, ...]` | `/tipos-logradouro`<br>**(GET)** |
| **Consultar ViaCEP** | Busca endereço em API externa. | URL Param | `{"cep": "85867900", "cidade": {...}, ...}` | `/enderecos/externo/{cep}`<br>**(GET)** |
| **Cadastrar Endereço** | Salva endereço (com cascata para Cidade/Bairro). | Objeto Endereco | Objeto Endereco (com ID) | `/enderecos/cadastrar`<br>**(POST)** |

---

### 🛠️ T4A - Aluguel de Equipamentos
**Contexto:** `http://localhost:8080/aluguel`

#### 1. Clientes
| SERVIÇO | Descrição | Entrada (JSON) | Saída (JSON) | Endereço (Método) |
| :--- | :--- | :--- | :--- | :--- |
| **Cadastrar Cliente** | Salva Cliente + Endereço + Contatos. | **Cliente Completo**<br>*(Ver Mod. A)* | **Cliente Completo**<br>*(Com `idCliente` gerado)* | `/clientes/cadastrar`<br>**(POST)** |
| **Buscar por ID** | Busca dados do cliente. | `{"idCliente": 1}` | **Cliente Completo** | `/clientes/buscar-id`<br>**(POST)** |
| **Buscar por CPF** | Busca cliente por documento. | `{"cpf": "12345678900"}` | **Cliente Completo** | `/clientes/buscar-cpf`<br>**(POST)** |

#### 2. Estoque (Equipamentos)
| SERVIÇO | Descrição | Entrada (JSON) | Saída (JSON) | Endereço (Método) |
| :--- | :--- | :--- | :--- | :--- |
| **Listar Tipos** | Lista categorias de equipamento. | - | `[{"idTipoEquipamento": 1, "nomeTipoEquipamento": "Ferramentas"}]` | `/tipos-equipamento`<br>**(GET)** |
| **Cadastrar Tipo** | Cria nova categoria. | `{"nomeTipoEquipamento": "..."}` | Objeto Tipo (com ID) | `/tipos-equipamento/cadastrar`<br>**(POST)** |
| **Listar Equipamentos** | Lista itens disponíveis. | - | `[{"idEquipamento": 1, "nomeEquipamento": "Furadeira", "valorDiaria": 50.0, "tipoEquipamento": {...}}]` | `/equipamentos`<br>**(GET)** |
| **Cadastrar Item** | Adiciona item ao estoque. | Objeto Equipamento | Objeto Equipamento (com ID) | `/equipamentos/cadastrar`<br>**(POST)** |

#### 3. Aluguel (Transação)
| SERVIÇO | Descrição | Entrada (JSON) | Saída (JSON) | Endereço (Método) |
| :--- | :--- | :--- | :--- | :--- |
| **Registrar Aluguel** | Efetiva a locação. | `{"nroAluguel": 100, "dataLocacao": "...", "cliente": {"idCliente": 1}, "equipamento": {"idEquipamento": 5}}` | Objeto Aluguel (Com total calculado) | `/aluguel/registrar`<br>**(POST)** |
| **Consultar Todos** | Relatório de aluguéis. | - | Lista de Aluguéis | `/aluguel/consultar`<br>**(GET)** |
| **Buscar Aluguel** | Busca pelo número. | `{"nroAluguel": 100}` | Objeto Aluguel Completo | `/aluguel/buscar-numero`<br>**(POST)** |

---

### 🔧 T4B - Ordem de Serviço
**Contexto:** `http://localhost:8080/os`

#### 1. Atendentes
| SERVIÇO | Descrição | Entrada (JSON) | Saída (JSON) | Endereço (Método) |
| :--- | :--- | :--- | :--- | :--- |
| **Cadastrar Atendente** | Salva Funcionário + Endereço + Contatos. | **Atendente Completo**<br>*(Ver Mod. A)* | **Atendente Completo**<br>*(Com `idAtendente` gerado)* | `/atendentes/cadastrar`<br>**(POST)** |
| **Buscar por ID** | Busca dados do atendente. | `{"idAtendente": 1}` | **Atendente Completo** | `/atendentes/buscar-id`<br>**(POST)** |
| **Buscar por CPF** | Busca atendente por documento. | `{"cpf": "12345678900"}` | **Atendente Completo** | `/atendentes/buscar-cpf`<br>**(POST)** |

#### 2. Serviços (Catálogo)
| SERVIÇO | Descrição | Entrada (JSON) | Saída (JSON) | Endereço (Método) |
| :--- | :--- | :--- | :--- | :--- |
| **Listar Tipos** | Lista serviços disponíveis (Mão de obra). | - | `[{"idTipoServico": 1, "nomeTipoServico": "Formatação"}]` | `/tipos-servico`<br>**(GET)** |
| **Cadastrar Tipo** | Adiciona serviço ao catálogo. | `{"nomeTipoServico": "..."}` | Objeto Tipo (com ID) | `/tipos-servico/cadastrar`<br>**(POST)** |

#### 3. Ordem de Serviço (Transação)
| SERVIÇO | Descrição | Entrada (JSON) | Saída (JSON) | Endereço (Método) |
| :--- | :--- | :--- | :--- | :--- |
| **Registrar OS** | Cria OS com lista de itens. | `{"nroOrdemServico": 0, "cliente": {"idCliente": 1}, "atendente": {"idAtendente": 2}, "listaItens": [{"valorServico": 80.0, "tipoServico": {"idTipoServico": 1}}]}` | Objeto OS (Com total calculado e itens salvos) | `/ordem-servico/registrar`<br>**(POST)** |
| **Consultar Todas** | Relatório de OSs. | - | Lista de OSs | `/ordem-servico/consultar`<br>**(GET)** |
| **Buscar OS** | Busca pelo número. | `{"nroOrdemServico": 500}` | Objeto OS Completo | `/ordem-servico/buscar-numero`<br>**(POST)** |

---

### 📦 Modelo de Dados (JSON - Exemplo "Modelo A")
*Estrutura válida para Cliente e Atendente (Pessoa Física Completa).*

```json
{
  "nome": "João da Silva",
  "cpf": "12345678900",
  "numero": "100",
  "complemento": "Apto 101",
  "endereco": {
    "cep": "85867900",
    "cidade": {
      "nomeCidade": "Foz do Iguaçu",
      "unidadeFederativa": { "siglaUF": "PR" }
    },
    "bairro": { "nomeBairro": "Centro" },
    "logradouro": {
      "nomeLogradouro": "Brasil",
      "tipoLogradouro": { "nomeTipoLogradouro": "Avenida" }
    }
  },
  "telefones": [
    { "numero": "999887766", "ddd": { "ddd": 45 }, "ddi": { "ddi": 55 } }
  ],
  "emails": [
    { "enderecoEmail": "joao@email.com" }
  ]
}
```