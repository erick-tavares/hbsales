package br.com.hbsis.pedido;

import br.com.hbsis.periodovendas.PeriodoVendasDTO;
import br.com.hbsis.periodovendas.PeriodoVendasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping ("pedidos")
public class PedidoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pedido.class);

    private final PedidoService pedidoService;

    @Autowired
    public PedidoRest(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public PedidoDTO save(@Valid @RequestBody PedidoDTO pedidoDTO) {
        LOGGER.info("Recebendo solicitação de persistência de pedido...");
        LOGGER.debug("Payaload: {}", pedidoDTO);

        return this.pedidoService.save(pedidoDTO);
    }

    @GetMapping("/{id}")
    public PedidoDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.pedidoService.findById(id);
    }

    @PutMapping("/{id}")
    public PedidoDTO udpate(@PathVariable("id") Long id, @RequestBody PedidoDTO pedidoDTO) {
        LOGGER.info("Recebendo Update para pedido de ID: {}", id);
        LOGGER.debug("Payload: {}", pedidoDTO);

        return this.pedidoService.update(pedidoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para pedido de ID: {}", id);

        this.pedidoService.delete(id);
    }
}
