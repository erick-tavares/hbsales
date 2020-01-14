package br.com.hbsis.pedidoitem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

    @RestController
    @RequestMapping("pedido_itens")
    public class ItemPedidoRest {
        private static final Logger LOGGER = LoggerFactory.getLogger(ItemPedido.class);

        private final ItemPedidoService itemPedidoService;

        @Autowired
        public ItemPedidoRest(ItemPedidoService itemPedidoService) {
            this.itemPedidoService = itemPedidoService;
        }

        @PostMapping
        public ItemPedidoDTO save(@Valid @RequestBody ItemPedidoDTO itemPedidoDTO) {
            LOGGER.info("Recebendo solicitação de persistência de item...");
            LOGGER.debug("Payaload: {}", itemPedidoDTO);

            return this.itemPedidoService.save(itemPedidoDTO);
        }

        @GetMapping("/{id}")
        public ItemPedidoDTO find(@PathVariable("id") Long id) {

            LOGGER.info("Recebendo find by ID... id: [{}]", id);

            return this.itemPedidoService.findById(id);
        }

        @PutMapping("/{id}")
        public ItemPedidoDTO udpate(@PathVariable("id") Long id, @RequestBody ItemPedidoDTO itemPedidoDTO) {
            LOGGER.info("Recebendo Update para item de ID: {}", id);
            LOGGER.debug("Payload: {}", itemPedidoDTO);

            return this.itemPedidoService.update(itemPedidoDTO, id);
        }

        @DeleteMapping("/{id}")
        public void delete(@PathVariable("id") Long id) {
            LOGGER.info("Recebendo Delete para pedido de ID: {}", id);

            this.itemPedidoService.delete(id);
        }


}
