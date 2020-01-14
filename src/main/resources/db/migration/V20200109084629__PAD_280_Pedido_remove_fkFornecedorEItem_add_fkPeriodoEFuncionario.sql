alter table pedido
drop constraint fk_pedido_fornecedor;
alter table pedido
drop column fornecedor_id;


alter table pedido
drop constraint fk_pedido_item;
alter table pedido
drop column item_id;


alter table pedido
add periodo_vendas_id bigint not null;
alter table pedido
add constraint fk_pedido_periodo foreign key (periodo_vendas_id) references  periodo_vendas (id);


alter table pedido
add funcionario_id bigint not null;
alter table pedido
add constraint fk_pedido_funcionario foreign key (funcionario_id) references funcionario(id);
