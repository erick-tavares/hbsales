package br.com.hbsis.pedido;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.pedidoitem.ItemPedido;
import br.com.hbsis.periodovendas.PeriodoVendas;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    private Funcionario funcionarioId;
    @ManyToOne
    @JoinColumn (name = "periodo_vendas_id", referencedColumnName = "id")
    private PeriodoVendas periodoVendasId;
    @ManyToOne
    @JoinColumn (name = "fornecedor_id", referencedColumnName = "id")
    private Fornecedor fornecedorId;

    @OneToMany (mappedBy = "pedidoId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itemList;


    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", status=" + status +
                ", funcionarioId=" + funcionarioId +
                ", periodoVendasId=" + periodoVendasId +
                ", fornecedorId=" + fornecedorId +
                ", itemList=" + itemList +
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

    public Fornecedor getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Fornecedor fornecedorId) {
        this.fornecedorId = fornecedorId;
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

    public Funcionario getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Funcionario funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public PeriodoVendas getPeriodoVendasId() {
        return periodoVendasId;
    }

    public void setPeriodoVendasId(PeriodoVendas periodoVendasId) {
        this.periodoVendasId = periodoVendasId;
    }

    public List<ItemPedido> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemPedido> itemList) {
        this.itemList = itemList;
    }
}
