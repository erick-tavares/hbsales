alter table produto
drop CONSTRAINT linha_produto;

alter table produto
drop CONSTRAINT UQ__produto__6F71C0DCB06B6DCC;

alter table produto
ALTER COLUMN codigo varchar (10) not null;

alter table produto
ALTER COLUMN nome varchar (200) not null;

alter table produto
ALTER COLUMN preco decimal (12,2) not null;

alter table produto
ALTER COLUMN unidade_por_caixa int not null;

alter table produto
ALTER COLUMN peso_por_unidade decimal (12,3) not null;

alter table produto
ADD unidade_medida_peso varchar (50) not null;