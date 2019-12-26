package br.com.hbsis.linhacategoria;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import javax.persistence.*;

@Entity
@Table(name = "linha_categoria")
public class LinhaCategoria {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codigo", unique = true, nullable = false, length = 10)
    private String codigo;
    @Column (name = "nome", nullable = false, length = 50)
    private String nome;

    @ManyToOne
    @JoinColumn (name = "categoria_id" , referencedColumnName = "id")
    private CategoriaProduto categoriaId;


    public LinhaCategoria() {
    }

    @Override
    public String toString() {
        return "LinhaCategoria{" +
                "id=" + id +
                ", codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", categoriaId=" + categoriaId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaProduto getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(CategoriaProduto categoriaId) {
        this.categoriaId = categoriaId;
    }
}
