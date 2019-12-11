ALTER TABLE linha_categoria
drop CONSTRAINT UQ__linha_ca__40F9A206C1CE1267;

ALTER TABLE linha_categoria
ALTER COLUMN nome VARCHAR(50) NOT NULL;
ALTER TABLE linha_categoria
ALTER COLUMN codigo VARCHAR(10) NOT NULL;
ALTER TABLE linha_categoria
ADD CONSTRAINT categoria_id_nome UNIQUE (categoria_id, nome);