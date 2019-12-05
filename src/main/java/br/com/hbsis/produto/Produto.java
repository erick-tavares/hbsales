package br.com.hbsis.produto;


import br.com.hbsis.linhacategoria.LinhaCategoria;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codigo", nullable = false)
    private int codigo;
    @Column(name = "nome", nullable = false, length = 225)
    private String nome;
    @Column(name = "preco", nullable = false)
    private double preco;
    @Column(name = "unidade_por_caixa", nullable = false)
    private int unidadePorCaixa;
    @Column(name = "peso_por_unidade", nullable = false)
    private double pesoPorUnidade;
    @Column(name = "validade", nullable = false)
    private LocalDateTime validade;

    @ManyToOne
    @JoinColumn (name = "linha_categoria_id" , referencedColumnName = "id")
    private LinhaCategoria linhaCategoriaId;

    public Produto() {
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", unidadePorCaixa=" + unidadePorCaixa +
                ", pesoPorUnidade=" + pesoPorUnidade +
                ", validade=" + validade +
                ", linhaCategoriaId=" + linhaCategoriaId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getUnidadePorCaixa() {
        return unidadePorCaixa;
    }

    public void setUnidadePorCaixa(int unidadePorCaixa) {
        this.unidadePorCaixa = unidadePorCaixa;
    }

    public double getPesoPorUnidade() {
        return pesoPorUnidade;
    }

    public void setPesoPorUnidade(double pesoPorUnidade) {
        this.pesoPorUnidade = pesoPorUnidade;
    }

    public LocalDateTime getValidade() {
        return validade;
    }

    public void setValidade(LocalDateTime validade) {
        this.validade = validade;
    }

    public Long getLinhaCategoriaId() {
        return linhaCategoriaId.getId();
    }

    public void setLinhaCategoriaId(LinhaCategoria linhaCategoriaId) {
        this.linhaCategoriaId = linhaCategoriaId;
    }
}
