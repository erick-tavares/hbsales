alter table linha_categoria drop constraint nome_categoria_id;

alter table linha_categoria add constraint categoria_id_nome unique (categoria_id,nome);


alter table categoria_produto drop constraint nome_fornecedor_id;

alter table categoria_produto add constraint fornecedor_id_nome unique (fornecedor_id,nome);

