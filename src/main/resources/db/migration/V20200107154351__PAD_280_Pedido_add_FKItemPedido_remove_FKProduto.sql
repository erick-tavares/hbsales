alter table pedido
drop constraint fk_pedido_produto;

alter table pedido
drop column produto_id;

alter table pedido
add item_id bigint not null;

alter table pedido
add constraint fk_pedido_item foreign key (item_id) references item_pedido (id)
;