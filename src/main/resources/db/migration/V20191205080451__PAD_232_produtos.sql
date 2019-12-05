create table produto(

id bigint identity (1,1) primary key not null,
codigo int not null,
nome varchar (225) unique not null,
preco float not null,
unidade_por_caixa int not null,
peso_por_unidade float not null,
validade date not null,
linha_categoria_id bigint not null,

constraint linha_produto unique (linha_categoria_id,nome),

constraint fk_linha_produto foreign key (linha_categoria_id) references linha_categoria(id)
)
;