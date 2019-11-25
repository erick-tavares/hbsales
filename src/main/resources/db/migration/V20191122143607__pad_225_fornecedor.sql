create table fornecedor (
    id    BIGINT IDENTITY(1, 1) NOT NULL,
    razao_social VARCHAR(255)           NOT NULL,
    cnpj VARCHAR(14)           NOT NULL,
    nome VARCHAR(255)           NOT NULL,
    endereco VARCHAR(255)           NOT NULL,
    telefone VARCHAR(255)           NOT NULL,
    email VARCHAR(255)           NOT NULL
);