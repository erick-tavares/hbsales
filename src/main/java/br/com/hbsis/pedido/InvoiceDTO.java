package br.com.hbsis.pedido;

import br.com.hbsis.pedidoitem.ItemPedidoDTO;

public class InvoiceDTO {
    private String fornecedorCnpj;
    private String employeeUuid;
    private ItemPedidoDTO itemPedidoDTO;
    private Double valorTotal;

    public InvoiceDTO(String fornecedorCnpj, String employeeUuid, ItemPedidoDTO itemPedidoDTO, Double valorTotal) {
        this.fornecedorCnpj = fornecedorCnpj;
        this.employeeUuid = employeeUuid;
        this.itemPedidoDTO = itemPedidoDTO;
        this.valorTotal = valorTotal;
    }

    public String getFornecedorCnpj() {
        return fornecedorCnpj;
    }

    public void setFornecedorCnpj(String fornecedorCnpj) {
        this.fornecedorCnpj = fornecedorCnpj;
    }

    public String getEmployeeUuid() {
        return employeeUuid;
    }

    public void setEmployeeUuid(String employeeUuid) {
        this.employeeUuid = employeeUuid;
    }

    public ItemPedidoDTO getItemPedidoDTO() {
        return itemPedidoDTO;
    }

    public void setItemPedidoDTO(ItemPedidoDTO itemPedidoDTO) {
        this.itemPedidoDTO = itemPedidoDTO;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
