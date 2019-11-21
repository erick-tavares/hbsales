create table fornecedores
(
    id   bigint identity (1,1) not null,
    nome varchar(255)          not null,
    cnpj varchar(11)           not null
);
