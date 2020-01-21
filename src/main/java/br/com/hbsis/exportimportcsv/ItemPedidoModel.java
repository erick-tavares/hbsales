package br.com.hbsis.exportimportcsv;

import br.com.hbsis.produto.Produto;

public class ItemPedidoModel {

    private int quantidade;
    private Produto produto;

    public ItemPedidoModel(int quantidade, Produto produto) {
        this.quantidade = quantidade;
        this.produto = produto;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ItemPedidoModel{" +
                "quantidade=" + quantidade +
                ", produto=" + produto +
                '}';
    }
}
