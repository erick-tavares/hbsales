package br.com.hbsis.produto;


import br.com.hbsis.linhacategoria.LinhaCategoria;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codigo", nullable = false, unique = true, length = 10)
    private String codigo;
    @Column(name = "nome", nullable = false, length = 200)
    private String nome;
    @Column(name = "preco", nullable = false)
    private double preco;
    @Column(name = "unidade_por_caixa", nullable = false)
    private int unidadePorCaixa;
    @Column(name = "peso_por_unidade", nullable = false)
    private double pesoPorUnidade;
    @Column(name = "unidade_medida_peso", nullable = false)
    private String unidadeMedidaPeso = "";
    @Column(name = "validade", nullable = false)
    private LocalDate validade;

    @ManyToOne
    @JoinColumn(name = "linha_categoria_id", referencedColumnName = "id")
    private LinhaCategoria linhaCategoriaId;

    public Produto() {
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", unidadePorCaixa=" + unidadePorCaixa +
                ", pesoPorUnidade=" + pesoPorUnidade +
                ", unidadeMedidaPeso='" + unidadeMedidaPeso + '\'' +
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

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public LinhaCategoria getLinhaCategoriaId() {
        return linhaCategoriaId;
    }

    public void setLinhaCategoriaId(LinhaCategoria linhaCategoriaId) {
        this.linhaCategoriaId = linhaCategoriaId;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getUnidadeMedidaPeso() {
        return unidadeMedidaPeso;
    }

    public void setUnidadeMedidaPeso(String unidadeMedidaPeso) {
        this.unidadeMedidaPeso = unidadeMedidaPeso;
    }
}
