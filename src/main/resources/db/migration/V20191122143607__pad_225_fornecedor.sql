CREATE TABLE fornecedor (
    id_fornecedor BIGINT IDENTITY(1, 1) PRIMARY KEY NOT NULL,
    razao_social VARCHAR(255)           NOT NULL,
    cnpj_fornecedor VARCHAR(14)          UNIQUE NOT NULL,
    nome_fornecedor VARCHAR(255)           NOT NULL,
    endereco_fornecedor VARCHAR(255)           NOT NULL,
    telefone_fornecedor VARCHAR(255)           NOT NULL,
    email_fornecedor VARCHAR(255)           NOT NULL
);