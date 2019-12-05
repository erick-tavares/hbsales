CREATE TABLE fornecedor (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY NOT NULL,
    razao_social VARCHAR(100)           NOT NULL,
    cnpj VARCHAR(14)             UNIQUE NOT NULL,
    nome VARCHAR(100)                   NOT NULL,
    endereco VARCHAR(100)               NOT NULL,
    telefone VARCHAR(12)                NOT NULL,
    email VARCHAR(50)                   NOT NULL
);