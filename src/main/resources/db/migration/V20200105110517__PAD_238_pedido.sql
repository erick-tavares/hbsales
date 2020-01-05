create table pedido (
id BIGINT IDENTITY (1,1) PRIMARY KEY NOT NULL,
codigo varchar (20) not null unique,
data_criacao date not null,
status varchar (10),
fornecedor_id bigint not null,
produto_id bigint not null,

constraint fk_pedido_produto foreign key (produto_id) references produto (id),
constraint  fk_pedido_fornecedor foreign key (fornecedor_id) references fornecedor (id),
)