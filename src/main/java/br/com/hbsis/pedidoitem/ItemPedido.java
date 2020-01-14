package br.com.hbsis.pedidoitem;

import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.produto.Produto;

import javax.persistence.*;

    @Entity
    @Table(name = "item_pedido")
    public class ItemPedido {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "quantidade", nullable = false)
        private int quantidade;
        @Column(name = "valor_unitario", nullable = false)
        private double valorUnitario;

        @ManyToOne
        @JoinColumn(name = "produto_id", referencedColumnName = "id")
        private Produto produtoId;
        @ManyToOne
        @JoinColumn(name = "pedido_id", referencedColumnName = "id")
        private Pedido pedidoId;

        @Override
        public String toString() {
            return "PedidoItem{" +
                    "id=" + id +
                    ", quantidade=" + quantidade +
                    ", valorUnitario=" + valorUnitario +
                    ", produtoId=" + produtoId +
                    ", pedidoId=" + pedidoId +
                    '}';
        }

        public Pedido getPedidoId() {
            return pedidoId;
        }

        public void setPedidoId(Pedido pedidoId) {
            this.pedidoId = pedidoId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public Produto getProdutoId() {
            return produtoId;
        }

        public void setProdutoId(Produto produtoId) {
            this.produtoId = produtoId;
        }
    }
