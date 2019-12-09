ALTER TABLE categoria_produto
 drop constraint fornecedor_id_nome;

ALTER TABLE categoria_produto
ALTER COLUMN codigo VARCHAR(10);
ALTER TABLE categoria_produto
ALTER COLUMN nome VARCHAR(50);

ALTER TABLE categoria_produto
 add constraint fornecedor_id_nome unique (fornecedor_id,nome);