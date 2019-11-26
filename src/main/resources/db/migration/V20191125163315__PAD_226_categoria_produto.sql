CREATE TABLE categoria_produto(
    id_categoria_produto BIGINT IDENTITY(1, 1) PRIMARY KEY NOT NULL,
    nome_categoria_produto VARCHAR (255)   NOT NULL,
    fornecedor_categoria_produto VARCHAR (255) NOT NULL,
    codigo_categoria_produto INT NOT NULL,
    fornecedor_id BIGINT,

    CONSTRAINT fk_categoria_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedor (id_fornecedor)
);