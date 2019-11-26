package br.com.hbsis.categoriaProduto;

import javax.persistence.*;

@Entity
@Table(name = "categoria_produto")
class CategoriaProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_categoria_produto",  nullable = false, length = 255)
    private String nome;
    @Column(name = "fornecedor_categoria_produto", nullable = false, length = 255)
    private String fornecedor;
    @Column(name = "codigo_categoria_produto", nullable = false)
    private int codigo;

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

    @Override
    public String toString() {
        return "CategoriaProduto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", fornecedor='" + fornecedor + '\'' +
                ", codigo=" + codigo +
                '}';
    }
}
