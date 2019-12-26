create table periodo_vendas (
id bigint identity (1, 1) PRIMARY KEY not null,
inicio_vendas date not null,
fim_vendas date not null,
fornecedor_id bigint not null,
retirada_pedido date not null,
descricao varchar (50),

constraint fornecedor_inicio_vendas unique (fornecedor_id,inicio_vendas),
constraint fornecedor_fim_vendas unique (fornecedor_id,fim_vendas),

constraint fk_fornecedor_periodo_vendas foreign key (fornecedor_id) references fornecedor (id)

)