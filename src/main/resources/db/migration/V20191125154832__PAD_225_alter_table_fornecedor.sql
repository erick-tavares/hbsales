ALTER TABLE fornecedor
ADD CONSTRAINT uc_cnpj UNIQUE (cnpj)
;