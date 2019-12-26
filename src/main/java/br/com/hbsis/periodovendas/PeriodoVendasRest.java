package br.com.hbsis.periodovendas;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("vendas")
public class PeriodoVendasRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendasRest.class);

    private final PeriodoVendasService periodoVendasService;

    @Autowired
    public PeriodoVendasRest(PeriodoVendasService periodoVendasService) {
        this.periodoVendasService = periodoVendasService;
    }

    @PostMapping
    public PeriodoVendasDTO save(@Valid @RequestBody PeriodoVendasDTO periodoVendasDTO) {
        LOGGER.info("Recebendo solicitação de persistência de vendas...");
        LOGGER.debug("Payaload: {}", periodoVendasDTO);

        return this.periodoVendasService.save(periodoVendasDTO);
    }

    @GetMapping("/{id}")
    public PeriodoVendasDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.periodoVendasService.findById(id);
    }

    @PutMapping("/{id}")
    public PeriodoVendasDTO udpate(@PathVariable("id") Long id, @RequestBody PeriodoVendasDTO periodoVendasDTO) {
        LOGGER.info("Recebendo Update para vendas de ID: {}", id);
        LOGGER.debug("Payload: {}", periodoVendasDTO);

        return this.periodoVendasService.update(periodoVendasDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para vendas de ID: {}", id);

        this.periodoVendasService.delete(id);
    }
}
