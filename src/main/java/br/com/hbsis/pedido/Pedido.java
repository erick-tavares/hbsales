package br.com.hbsis.pedido;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.produto.Produto;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table (name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;
    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @Column (name = "status", nullable = false)
    private StatusPedido status;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", referencedColumnName = "id")
    private Fornecedor fornecedorId;
    @ManyToOne
    @JoinColumn(name = "produto_id", referencedColumnName = "id")
    private Produto produtoId;

    @Column(name = "quantidade_itens")
    private int quantidadeItens;

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", status=" + status +
                ", fornecedorId=" + fornecedorId +
                ", produtoId=" + produtoId +
                ", quantidadeItens=" + quantidadeItens +
                '}';
    }

    public int getQuantidadeItens() {
        return quantidadeItens;
    }

    public void setQuantidadeItens(int quantidadeItens) {
        this.quantidadeItens = quantidadeItens;
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

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Fornecedor getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Fornecedor fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public Produto getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Produto produtoId) {
        this.produtoId = produtoId;
    }
}
