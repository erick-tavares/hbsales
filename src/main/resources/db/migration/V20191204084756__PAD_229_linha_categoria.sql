create table linha_categoria(

id bigint identity (1,1) primary key not null,
codigo int unique not null,
nome varchar (225) not null,
categoria_id bigint not null,

constraint categoria_id_nome unique (categoria_id,nome),

constraint fk_categoria_linha foreign key (categoria_id) references categoria_produto (id)
);