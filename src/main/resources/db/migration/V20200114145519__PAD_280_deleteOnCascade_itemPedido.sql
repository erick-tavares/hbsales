alter table item_pedido
drop constraint fk_item_pedido;

alter table item_pedido
add constraint fk_item_pedido foreign key (pedido_id) references pedido (id) on delete cascade on update cascade;