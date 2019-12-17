CREATE TABLE categoria_produto(
      id BIGINT IDENTITY(1, 1) PRIMARY KEY NOT NULL,
      nome VARCHAR (255)   NOT NULL,
      codigo INT NOT NULL,
      fornecedor_id BIGINT NOT NULL,

      constraint fornecedor_id_nome unique (fornecedor_id,nome),

      CONSTRAINT fk_categoria_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedor (id)
  );