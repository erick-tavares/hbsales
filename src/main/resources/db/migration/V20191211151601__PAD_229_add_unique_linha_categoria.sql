ALTER TABLE linha_categoria
drop CONSTRAINT categoria_id_nome;

ALTER TABLE linha_categoria
add constraint uq_codigo unique (codigo);

ALTER TABLE linha_categoria
ADD CONSTRAINT categoria_id_nome UNIQUE (categoria_id, nome);