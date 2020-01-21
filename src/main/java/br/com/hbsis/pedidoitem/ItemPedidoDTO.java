package br.com.hbsis.pedidoitem;


public class ItemPedidoDTO {
    private Long id;
    private Long produtoId;
    private int quantidade;
    private double valorUnitario;
    private Long pedidoId;

    @Override
    public String toString() {
        return "PedidoItemDTO{" +
                "id=" + id +
                ", produtoId=" + produtoId +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                ", pedidoId=" + pedidoId +
                '}';
    }

    public static ItemPedidoDTO of(ItemPedido itemPedido) {
        return new ItemPedidoDTO(itemPedido.getId(),
                itemPedido.getProdutoId().getId(),
                itemPedido.getQuantidade(),
                itemPedido.getValorUnitario(),
                itemPedido.getPedidoId().getId());
    }

    public ItemPedidoDTO(Long id, Long produtoId, int quantidade, double valorUnitario, Long pedidoId) {
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.pedidoId = pedidoId;
    }


    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }


    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}
