package br.com.hbsis.pedidoitem;

import br.com.hbsis.pedido.PedidoService;
import br.com.hbsis.produto.ProdutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemPedidoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemPedidoService.class);

    private final IItemPedidoRepository iItemPedidoRepository;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    public ItemPedidoService(IItemPedidoRepository iItemPedidoRepository, ProdutoService produtoService, PedidoService pedidoService) {
        this.iItemPedidoRepository = iItemPedidoRepository;
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
    }

    public ItemPedidoDTO save(ItemPedidoDTO itemPedidoDTO) {

        LOGGER.info("Salvando pedido");
        LOGGER.debug("Pedido: {}", itemPedidoDTO);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setProdutoId(produtoService.findProdutoById(itemPedidoDTO.getProdutoId()));
        itemPedido.setPedidoId(pedidoService.findPedidoById(itemPedidoDTO.getPedidoId()));
        itemPedido.setQuantidade(itemPedidoDTO.getQuantidade());
        itemPedido.setValorUnitario(produtoService.findProdutoById(itemPedidoDTO.getProdutoId()).getPreco());

        this.validate(itemPedidoDTO);

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

    public ItemPedidoDTO findById(Long id) {
        Optional<ItemPedido> itemPedidoOptional = this.iItemPedidoRepository.findById(id);

        if (itemPedidoOptional.isPresent()) {
            return ItemPedidoDTO.of(itemPedidoOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public ItemPedidoDTO update(ItemPedidoDTO itemPedidoDTO, Long id) {
        Optional<ItemPedido> itemPedidoExistenteOptional = this.iItemPedidoRepository.findById(id);

        if (itemPedidoExistenteOptional.isPresent()) {

            ItemPedido itemPedidoExistente = itemPedidoExistenteOptional.get();

            LOGGER.info("Atualizando pedido... id: [{}]", itemPedidoExistente.getId());
            LOGGER.debug("Payload: {}", itemPedidoDTO);
            LOGGER.debug("Pedido Existente: {}", itemPedidoExistente);

            itemPedidoExistente.setQuantidade(itemPedidoDTO.getQuantidade());
            itemPedidoExistente.setValorUnitario(itemPedidoDTO.getValorUnitario());

            itemPedidoExistente.setProdutoId(produtoService.findProdutoById(itemPedidoDTO.getProdutoId()));
            itemPedidoExistente.setPedidoId(pedidoService.findPedidoById(itemPedidoDTO.getProdutoId()));

            itemPedidoExistente = this.iItemPedidoRepository.save(itemPedidoExistente);

            return ItemPedidoDTO.of(itemPedidoExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para pedido de ID: [{}]", id);

        this.iItemPedidoRepository.deleteById(id);
    }
}
