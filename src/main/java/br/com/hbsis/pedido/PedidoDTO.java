package br.com.hbsis.pedido;

import br.com.hbsis.pedidoitem.ItemPedidoDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDTO {

    private Long id;
    private String codigo;
    private LocalDate dataCriacao;
    private StatusPedido status;
    private Long funcionarioId;
    private Long periodoVendasId;
    private List<ItemPedidoDTO> itemDTOList;
    private Long fornecedorId;


    public PedidoDTO(Long id, String codigo, LocalDate dataCriacao, StatusPedido status, Long funcionarioId, Long periodoVendasId, List<ItemPedidoDTO> itemDTOList, Long fornecedorId) {
        this.id = id;
        this.codigo = codigo;
        this.dataCriacao = dataCriacao;
        this.status = status;
        this.funcionarioId = funcionarioId;
        this.periodoVendasId = periodoVendasId;
        this.itemDTOList = itemDTOList;
        this.fornecedorId = fornecedorId;
    }

    public PedidoDTO() {

    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", status=" + status +
                ", funcionarioId=" + funcionarioId +
                ", periodoVendasId=" + periodoVendasId +
                ", itemDTOList=" + itemDTOList +
                ", fornecedorId=" + fornecedorId +
                '}';
    }

    public static PedidoDTO of(Pedido pedido) {
        List<ItemPedidoDTO> itemDTOList = new ArrayList<>();

        pedido.getItemList().forEach(itemPedido -> itemDTOList.add(ItemPedidoDTO.of(itemPedido)));
        return new PedidoDTO(pedido.getId(),
                pedido.getCodigo(),
                pedido.getDataCriacao(),
                pedido.getStatus(),
                pedido.getFuncionarioId().getId(),
                pedido.getPeriodoVendasId().getId(),
                itemDTOList,
                pedido.getFornecedorId().getId());
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
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

    public List<ItemPedidoDTO> getItemDTOList() {
        return itemDTOList;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Long getPeriodoVendasId() {
        return periodoVendasId;
    }

    public void setPeriodoVendasId(Long periodoVendasId) {
        this.periodoVendasId = periodoVendasId;
    }

    public void setItemDTOList(List<ItemPedidoDTO> itemDTOList) {
        this.itemDTOList = itemDTOList;
    }
}
