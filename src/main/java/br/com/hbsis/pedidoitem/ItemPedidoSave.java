package br.com.hbsis.pedidoitem;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class ItemPedidoSave {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemPedidoSave.class);

    private final IItemPedidoRepository iItemPedidoRepository;


    public ItemPedidoSave(IItemPedidoRepository iItemPedidoRepository) {
        this.iItemPedidoRepository = iItemPedidoRepository;
    }

    public ItemPedidoDTO save(ItemPedido itemPedidoAdd) {

        LOGGER.info("Salvando pedido");
        LOGGER.debug("Pedido: {}", itemPedidoAdd);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setProdutoId(itemPedidoAdd.getProdutoId());
        itemPedido.setPedidoId(itemPedidoAdd.getPedidoId());
        itemPedido.setQuantidade(itemPedidoAdd.getQuantidade());
        itemPedido.setValorUnitario(itemPedidoAdd.getProdutoId().getPreco());

        this.validate(ItemPedidoDTO.of(itemPedidoAdd));

        itemPedido = this.iItemPedidoRepository.save(itemPedido);

        return ItemPedidoDTO.of(itemPedido);
    }

    private void validate(ItemPedidoDTO itemPedidoDTO) {
        LOGGER.info("Validando pedido");

        if (itemPedidoDTO == null) {
            throw new IllegalArgumentException("PedidoItemDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(itemPedidoDTO.getQuantidade()))) {
            throw new IllegalArgumentException("Quantidade não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(itemPedidoDTO.getValorUnitario()))) {
            throw new IllegalArgumentException("Valor não deve ser nulo/vazio");
        }

        if (StringUtils.isEmpty(String.valueOf(itemPedidoDTO.getProdutoId()))) {
            throw new IllegalArgumentException("ProdutoId não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(itemPedidoDTO.getPedidoId()))) {
            throw new IllegalArgumentException("PedidoId não deve ser nulo/vazio");
        }
    }
}
