package br.com.hbsis.categoriaProduto;

import br.com.hbsis.fornecedor.Fornecedor;

public class CategoriaProdutoDTO {
        private Long id;
        private String nome;
        private Fornecedor fornecedorCategoria;
        private int codigo;


    public CategoriaProdutoDTO() {
        }

        public CategoriaProdutoDTO(String nome, Fornecedor fornecedorCategoria, int codigo) {
            this.nome = nome;
            this.fornecedorCategoria = fornecedorCategoria;
            this.codigo = codigo;
        }

        public CategoriaProdutoDTO(Long id, String nome, Fornecedor fornecedorCategoria, int codigo) {
            this.id = id;
            this.nome = nome;
            this.fornecedorCategoria = fornecedorCategoria;
            this.codigo = codigo;
        }

        public static CategoriaProdutoDTO of(CategoriaProduto categoriaProduto) {
            return new CategoriaProdutoDTO(
                    categoriaProduto.getId(),
                    categoriaProduto.getNome(),
                    categoriaProduto.getFornecedorCategoria(),
                    categoriaProduto.getCodigo()
            );
        }

    @Override
    public String toString() {
        return "CategoriaProdutoDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", fornecedorCategoria=" + fornecedorCategoria +
                ", codigo=" + codigo +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Fornecedor getFornecedorCategoria() {
        return fornecedorCategoria;
    }

    public void setFornecedorCategoria(Fornecedor fornecedorCategoria) {
        this.fornecedorCategoria = fornecedorCategoria;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
