package br.com.hbsis.categoriaProduto;

public class CategoriaProdutoDTO {
        private Long id;
        private String nome;
        private String fornecedor;
        private int codigo;

        public CategoriaProdutoDTO() {
        }

        public CategoriaProdutoDTO(String nome, String fornecedor, int codigo) {
            this.nome = nome;
            this.fornecedor = fornecedor;
            this.codigo = codigo;
        }

        public CategoriaProdutoDTO(Long id, String nome, String fornecedor, int codigo) {
            this.id = id;
            this.nome = nome;
            this.fornecedor = fornecedor;
            this.codigo = codigo;
        }

        public static CategoriaProdutoDTO of(CategoriaProduto categoriaProduto) {
            return new CategoriaProdutoDTO(
                    categoriaProduto.getId(),
                    categoriaProduto.getNome(),
                    categoriaProduto.getFornecedor(),
                    categoriaProduto.getCodigo()
            );
        }

    @Override
    public String toString() {
        return "CategoriaProdutoDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", fornecedor='" + fornecedor + '\'' +
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

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
