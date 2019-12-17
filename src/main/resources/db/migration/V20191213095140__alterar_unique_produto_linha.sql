ALTER TABLE produto
ADD CONSTRAINT linha_categoria_id_nome UNIQUE (linha_categoria_id, nome);