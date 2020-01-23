package br.com.hbsis.pedido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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

    @GetMapping("/export-produtos-periodo/{periodoId}")
    public void exportCSVPorPeriodo(HttpServletResponse response, @PathVariable("periodoId") Long id) throws IOException, ParseException {
        LOGGER.info("Exportando CSV");

        this.pedidoService.exportCSVPorPeriodoPorFornecedor(response, id);
    }

    @GetMapping("/view/{funcionarioId}")
    public List<PedidoDTO> visualizarPedido(@PathVariable("funcionarioId") Long id) {
        LOGGER.info("Visualizando pedidos do funcionario de ID: {}", id);

        return this.pedidoService.visualizarPedidoDoFuncionario(id);
    }

    @GetMapping("/export-produtos-funcionario/{fornecedorId}")
    public void exportCSVPorFuncionario(HttpServletResponse response, @PathVariable("fornecedorId") Long id) throws IOException, ParseException {
        LOGGER.info("Exportando CSV");

        this.pedidoService.exportCSVPorFornecedorPorFuncionario(response, id);
    }

    @PutMapping("cancelar/{id}")
    public PedidoDTO cancelar(@PathVariable("id") Long id, @RequestBody PedidoDTO pedidoDTO) {
        LOGGER.info("Cancelando pedido de ID: {}", id);
        LOGGER.debug("Payload: {}", pedidoDTO);

        return this.pedidoService.cancelarPedido(id);
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
