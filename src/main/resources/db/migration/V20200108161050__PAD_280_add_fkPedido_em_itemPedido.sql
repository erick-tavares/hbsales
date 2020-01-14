alter table item_pedido
add pedido_id bigint not null;
alter table item_pedido
add constraint fk_item_pedido foreign key (pedido_id) references pedido (id)
;