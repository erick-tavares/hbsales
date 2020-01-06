package br.com.hbsis.pedido;

import java.time.LocalDate;

public class PedidoDTO {

    private Long id;
    private String codigo;
    private LocalDate dataCriacao;
    private StatusPedido status;
    private Long fornecedorId;
    private Long produtoId;
    private int quantidadeItens;

    public PedidoDTO(Long id, String codigo, LocalDate dataCriacao, StatusPedido status, Long fornecedorId, Long produtoId, int quantidadeItens) {
        this.id = id;
        this.codigo = codigo;
        this.dataCriacao = dataCriacao;
        this.status = status;
        this.fornecedorId = fornecedorId;
        this.produtoId = produtoId;
        this.quantidadeItens = quantidadeItens;
    }

    public int getQuantidadeItens() {
        return quantidadeItens;
    }

    public void setQuantidadeItens(int quantidadeItens) {
        this.quantidadeItens = quantidadeItens;
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
                ", quantidadeItens=" + quantidadeItens +
                '}';
    }

    public static PedidoDTO of(Pedido pedido) {
        return new PedidoDTO(pedido.getId(),
                pedido.getCodigo(),
                pedido.getDataCriacao(),
                pedido.getStatus(),
                pedido.getFornecedorId().getId(),
                pedido.getProdutoId().getId(),
                pedido.getQuantidadeItens());
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
