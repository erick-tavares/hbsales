alter table pedido
drop constraint fk_pedido_item;

alter table pedido
drop column item_id;

alter table pedido
add item_id bigint;

alter table pedido
add constraint fk_pedido_item foreign key (item_id) references item_pedido (id)
;