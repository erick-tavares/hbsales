package br.com.hbsis.exportimportcsv;

import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.produto.Produto;

public class ItemPedidoModel {

    private int quantidade;
    private Produto produto;
    private String funcionario;

    public ItemPedidoModel(int quantidade, Produto produto) {
        this.quantidade = quantidade;
        this.produto = produto;
    }

    public ItemPedidoModel(int quantidade, Produto produto, String funcionario) {
        this.quantidade = quantidade;
        this.produto = produto;
        this.funcionario = funcionario;
    }

    @Override
    public String toString() {
        return "ItemPedidoModel{" +
                "quantidade=" + quantidade +
                ", produto=" + produto +
                ", funcionario='" + funcionario + '\'' +
                '}';
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
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

}
