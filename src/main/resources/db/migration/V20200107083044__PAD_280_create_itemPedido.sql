create table item_pedido (
id BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
quantidade int not null,
valor_unitario decimal (12,2) not null,

pedido_id bigint not null,
produto_id bigint not null,

constraint fk_item_pedido foreign key (pedido_id) references pedido (id),
constraint  fk_item_produto foreign key (produto_id) references produto (id)
)
;