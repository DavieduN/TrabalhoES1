DROP TABLE IF EXISTS Endereco CASCADE;
DROP TABLE IF EXISTS Logradouro CASCADE;
DROP TABLE IF EXISTS TipoLogradouro CASCADE;
DROP TABLE IF EXISTS Bairro CASCADE;
DROP TABLE IF EXISTS Cidade CASCADE;
DROP TABLE IF EXISTS UnidadeFederativa CASCADE;

-- Endereço

CREATE TABLE UnidadeFederativa (
    siglaUF CHAR(2) NOT NULL,
    nomeUF VARCHAR(50) NOT NULL,
    CONSTRAINT pk_uf PRIMARY KEY (siglaUF)
);

CREATE TABLE Cidade (
    idCidade SERIAL NOT NULL,
    nomeCidade VARCHAR(100) NOT NULL,
    siglaUF CHAR(2) NOT NULL,
    CONSTRAINT pk_cidade PRIMARY KEY (idCidade),
    CONSTRAINT fk_cidade_uf FOREIGN KEY (siglaUF) REFERENCES UnidadeFederativa (siglaUF)
);

CREATE TABLE Bairro (
    idBairro SERIAL NOT NULL,
    nomeBairro VARCHAR(100) NOT NULL,
    CONSTRAINT pk_bairro PRIMARY KEY (idBairro)
);

CREATE TABLE TipoLogradouro (
    idTipoLogradouro SERIAL NOT NULL,
    nomeTipoLogradouro VARCHAR(50) NOT NULL,
    CONSTRAINT pk_tipologradouro PRIMARY KEY (idTipoLogradouro)
);

CREATE TABLE Logradouro (
    idLogradouro SERIAL NOT NULL,
    nomeLogradouro VARCHAR(100) NOT NULL,
    idTipoLogradouro INT NOT NULL,
    CONSTRAINT pk_logradouro PRIMARY KEY (idLogradouro),
    CONSTRAINT fk_logradouro_tipo FOREIGN KEY (idTipoLogradouro) REFERENCES TipoLogradouro (idTipoLogradouro)
);

CREATE TABLE Endereco (
    idEndereco SERIAL NOT NULL,
    cep VARCHAR(9) NOT NULL,
    idCidade INT NOT NULL,
    idBairro INT NOT NULL,
    idLogradouro INT NOT NULL,
    CONSTRAINT pk_endereco PRIMARY KEY (idEndereco),
    CONSTRAINT uk_endereco_unico UNIQUE (cep, idCidade, idBairro, idLogradouro),
    CONSTRAINT fk_endereco_cidade FOREIGN KEY (idCidade) REFERENCES Cidade (idCidade),
    CONSTRAINT fk_endereco_bairro FOREIGN KEY (idBairro) REFERENCES Bairro (idBairro),
    CONSTRAINT fk_endereco_logradouro FOREIGN KEY (idLogradouro) REFERENCES Logradouro (idLogradouro)
);

INSERT INTO UnidadeFederativa (siglaUF, nomeUF) VALUES
    ('AC', 'Acre'),
    ('AL', 'Alagoas'),
    ('AP', 'Amapá'),
    ('AM', 'Amazonas'),
    ('BA', 'Bahia'),
    ('CE', 'Ceará'),
    ('DF', 'Distrito Federal'),
    ('ES', 'Espírito Santo'),
    ('GO', 'Goiás'),
    ('MA', 'Maranhão'),
    ('MT', 'Mato Grosso'),
    ('MS', 'Mato Grosso do Sul'),
    ('MG', 'Minas Gerais'),
    ('PA', 'Pará'),
    ('PB', 'Paraíba'),
    ('PR', 'Paraná'),
    ('PE', 'Pernambuco'),
    ('PI', 'Piauí'),
    ('RJ', 'Rio de Janeiro'),
    ('RN', 'Rio Grande do Norte'),
    ('RS', 'Rio Grande do Sul'),
    ('RO', 'Rondônia'),
    ('RR', 'Roraima'),
    ('SC', 'Santa Catarina'),
    ('SP', 'São Paulo'),
    ('SE', 'Sergipe'),
    ('TO', 'Tocantins');

INSERT INTO TipoLogradouro (nomeTipoLogradouro) VALUES
    ('Rua'),
    ('Avenida'),
    ('Travessa'),
    ('Alameda'),
    ('Rodovia'),
    ('Praça'),
    ('Estrada'),
    ('Viela'),
    ('Largo'),
    ('Beco');

-- Pessoa

CREATE TABLE IF NOT EXISTS DDD (
    idDdd SERIAL PRIMARY KEY,
    ddd INT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS DDI (
    idDdi SERIAL PRIMARY KEY,
    ddi INT NOT NULL UNIQUE
);

INSERT INTO DDD (ddd) VALUES (45), (41), (11), (21), (31) ON CONFLICT DO NOTHING;
INSERT INTO DDI (ddi) VALUES (55), (1), (351) ON CONFLICT DO NOTHING;

-- Aluguel

CREATE TABLE Cliente (
    idCliente SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nomeSocial VARCHAR(100),
    cpf VARCHAR(14) NOT NULL UNIQUE,
    
    idEndereco INT NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(50),
    
    CONSTRAINT fk_cliente_endereco FOREIGN KEY (idEndereco) REFERENCES Endereco (idEndereco)
);

CREATE TABLE TelefoneCliente (
    idTelefone SERIAL PRIMARY KEY,
    numero VARCHAR(20) NOT NULL,
    idDdd INT NOT NULL,
    idDdi INT NOT NULL,
    idCliente INT NOT NULL,
    
    CONSTRAINT fk_tel_ddd FOREIGN KEY (idDdd) REFERENCES DDD (idDdd),
    CONSTRAINT fk_tel_ddi FOREIGN KEY (idDdi) REFERENCES DDI (idDdi),
    CONSTRAINT fk_tel_cliente FOREIGN KEY (idCliente) REFERENCES Cliente (idCliente) ON DELETE CASCADE
);

CREATE TABLE EmailCliente (
    idEmail SERIAL PRIMARY KEY,
    enderecoEmail VARCHAR(150) NOT NULL,
    idCliente INT NOT NULL,
    CONSTRAINT fk_email_cliente FOREIGN KEY (idCliente) REFERENCES Cliente (idCliente) ON DELETE CASCADE
);

CREATE TABLE TipoEquipamento (
    idTipoEquipamento SERIAL PRIMARY KEY,
    nomeTipoEquipamento VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Equipamento (
    idEquipamento SERIAL PRIMARY KEY,
    nomeEquipamento VARCHAR(100) NOT NULL,
    valorDiaria FLOAT NOT NULL,
    idTipoEquipamento INT NOT NULL,
    CONSTRAINT fk_equip_tipo FOREIGN KEY (idTipoEquipamento) REFERENCES TipoEquipamento (idTipoEquipamento)
);

CREATE TABLE Aluguel (
    nroAluguel SERIAL PRIMARY KEY,
    dataPedido DATE NOT NULL,
    dataLocacao DATE NOT NULL,
    dataDevolucao DATE NOT NULL,
    valorTotalLocacao FLOAT NOT NULL,
    
    idCliente INT NOT NULL,
    idEquipamento INT NOT NULL,
    
    CONSTRAINT fk_aluguel_cliente FOREIGN KEY (idCliente) REFERENCES Cliente (idCliente),
    CONSTRAINT fk_aluguel_equip FOREIGN KEY (idEquipamento) REFERENCES Equipamento (idEquipamento)
);

INSERT INTO TipoEquipamento (nomeTipoEquipamento) VALUES ('Ferramentas Manuais'), ('Ferramentas Elétricas'), ('Maquinário Leve') ON CONFLICT DO NOTHING;

INSERT INTO Equipamento (nomeEquipamento, valorDiaria, idTipoEquipamento) VALUES 
('Furadeira de Impacto', 50.00, 2),
('Betoneira 400L', 120.00, 3),
('Jogo de Chaves Combinadas', 15.00, 1);

