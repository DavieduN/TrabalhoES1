-- 1. Unidade Federativa
CREATE TABLE UnidadeFederativa (
    siglaUF CHAR(2) NOT NULL,
    nomeUF VARCHAR(50) NOT NULL,
    CONSTRAINT pk_uf PRIMARY KEY (siglaUF)
);

-- 2. Cidade
CREATE TABLE Cidade (
    idCidade SERIAL NOT NULL,
    nomeCidade VARCHAR(100) NOT NULL,
    siglaUF CHAR(2) NOT NULL,
    CONSTRAINT pk_cidade PRIMARY KEY (idCidade),
    CONSTRAINT fk_cidade_uf FOREIGN KEY (siglaUF) REFERENCES UnidadeFederativa (siglaUF)
);

-- 3. Bairro
CREATE TABLE Bairro (
    idBairro SERIAL NOT NULL,
    nomeBairro VARCHAR(100) NOT NULL,
    CONSTRAINT pk_bairro PRIMARY KEY (idBairro)
);

-- 4. Tipo de Logradouro
CREATE TABLE TipoLogradouro (
    idTipoLogradouro SERIAL NOT NULL,
    nomeTipoLogradouro VARCHAR(50) NOT NULL,
    CONSTRAINT pk_tipologradouro PRIMARY KEY (idTipoLogradouro)
);

-- 5. Logradouro
CREATE TABLE Logradouro (
    idLogradouro SERIAL NOT NULL,
    nomeLogradouro VARCHAR(100) NOT NULL,
    idTipoLogradouro INT NOT NULL,
    CONSTRAINT pk_logradouro PRIMARY KEY (idLogradouro),
    CONSTRAINT fk_logradouro_tipo FOREIGN KEY (idTipoLogradouro) REFERENCES TipoLogradouro (idTipoLogradouro)
);

-- 6. Endereço
CREATE TABLE Endereco (
    idEndereco SERIAL NOT NULL,
    cep VARCHAR(9) NOT NULL,
    idCidade INT NOT NULL,
    idBairro INT NOT NULL,
    idLogradouro INT NOT NULL,
    CONSTRAINT pk_endereco PRIMARY KEY (idEndereco),
    CONSTRAINT fk_endereco_cidade FOREIGN KEY (idCidade) REFERENCES Cidade (idCidade),
    CONSTRAINT fk_endereco_bairro FOREIGN KEY (idBairro) REFERENCES Bairro (idBairro),
    CONSTRAINT fk_endereco_logradouro FOREIGN KEY (idLogradouro) REFERENCES Logradouro (idLogradouro)
);

-- 7. Endereço Específico
CREATE TABLE EnderecoEspecifico (
    idEnderecoEspecifico SERIAL NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(50),
    idEndereco INT NOT NULL,
    CONSTRAINT pk_enderecoespecifico PRIMARY KEY (idEnderecoEspecifico),
    CONSTRAINT fk_especifico_generico FOREIGN KEY (idEndereco) REFERENCES Endereco (idEndereco)
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