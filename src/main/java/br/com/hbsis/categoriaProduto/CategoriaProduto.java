package br.com.hbsis.categoriaProduto;


import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.*;

@Entity
@ConfigurationProperties(prefix = "file") ///
@Table(name = "categoria_produto")
public class CategoriaProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria_produto", nullable = false)
    private Long id;
    @Column(name = "nome_categoria_produto",  nullable = false, length = 255)
    private String nome;
    @Column(name = "codigo_categoria_produto", nullable = false)
    private int codigo;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", referencedColumnName = "id_fornecedor")
        private Fornecedor fornecedorCategoria;
///
        private String uploadDir;
        public String getUploadDir() {
            return uploadDir;
        }
        public void setUploadDir(String uploadDir) {
            this.uploadDir = uploadDir;
        }

    ///
    public CategoriaProduto(){
    }

    public CategoriaProduto(String nome, int codigo, Fornecedor fornecedorCategoria) {
        this.nome = nome;
        this.codigo = codigo;
        this.fornecedorCategoria = fornecedorCategoria;
    }

    public CategoriaProduto(Long id, String nome, int codigo, Fornecedor fornecedorCategoria) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.fornecedorCategoria = fornecedorCategoria;
    }

    public Fornecedor getFornecedorCategoria() {
        return fornecedorCategoria;
    }

    public void setFornecedorCategoria(Fornecedor fornecedorCategoria) {
        this.fornecedorCategoria = fornecedorCategoria;
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
                ", codigo=" + codigo +
                ", fornecedorCategoria=" + fornecedorCategoria +
                '}';
    }
}
