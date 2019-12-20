package br.com.hbsis.periodovendas;


import br.com.hbsis.fornecedor.Fornecedor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "periodo_vendas")
public class PeriodoVendas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "inicio_vendas", nullable = false)
    private LocalDate inicioVendas;
    @Column(name = "fim_vendas", nullable = false)
    private LocalDate fimVendas;
    @Column(name = "retirada_pedido", nullable = false)
    private LocalDate retiradaPedido;
    @Column(name = "descricao", nullable = false, length = 50)
    private String descricao;

    @ManyToOne
    @JoinColumn (name = "fornecedor_id", referencedColumnName = "id")
    private Fornecedor fornecedorId;

    public PeriodoVendas() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getInicioVendas() {
        return inicioVendas;
    }

    public void setInicioVendas(LocalDate inicioVendas) {
        this.inicioVendas = inicioVendas;
    }

    public LocalDate getFimVendas() {
        return fimVendas;
    }

    public void setFimVendas(LocalDate fimVendas) {
        this.fimVendas = fimVendas;
    }

    public LocalDate getRetiradaPedido() {
        return retiradaPedido;
    }

    public void setRetiradaPedido(LocalDate retiradaPedido) {
        this.retiradaPedido = retiradaPedido;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Fornecedor getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Fornecedor fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    @Override
    public String toString() {
        return "PeriodoVendas{" +
                "id=" + id +
                ", inicioVendas=" + inicioVendas +
                ", fimVendas=" + fimVendas +
                ", retiradaPedido=" + retiradaPedido +
                ", descricao='" + descricao + '\'' +
                ", fornecedorId=" + fornecedorId +
                '}';
    }
}
