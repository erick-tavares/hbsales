package br.com.hbsis.pedido;

import java.time.LocalDate;

public class PedidoDTO {

    private Long id;
    private String codigo;
    private LocalDate dataCriacao;
    private StatusPedido status;
    private Long fornecedorId;
    private Long produtoId;

    public PedidoDTO(Long id, String codigo, LocalDate dataCriacao, StatusPedido status, Long fornecedorId, Long produtoId) {
        this.id = id;
        this.codigo = codigo;
        this.dataCriacao = dataCriacao;
        this.status = status;
        this.fornecedorId = fornecedorId;
        this.produtoId = produtoId;
    }

    public static PedidoDTO of(Pedido pedido) {
        return new PedidoDTO(pedido.getId(),
                pedido.getCodigo(),
                pedido.getDataCriacao(),
                pedido.getStatus(),
                pedido.getFornecedorId().getId(),
                pedido.getProdutoId().getId()
        );
    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", status=" + status +
                ", fornecedorId=" + fornecedorId +
                ", produtoId=" + produtoId +
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

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
}
