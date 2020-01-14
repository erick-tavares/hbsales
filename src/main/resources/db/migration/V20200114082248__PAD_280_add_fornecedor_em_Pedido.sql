alter table pedido
add fornecedor_id bigint not null;

alter table pedido
add constraint fk_pedido_fornecedor foreign key (fornecedor_id) references fornecedor (id);