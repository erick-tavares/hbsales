package br.com.hbsis.linhacategoria;

import br.com.hbsis.exportimportcsv.ImportCSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/linha-categorias")
public class LinhaCategoriaRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaRest.class);

    private final LinhaCategoriaService linhaCategoriaService;
    private final ImportCSV importCSV;

    @Autowired
    public LinhaCategoriaRest(LinhaCategoriaService linhaCategoriaService, ImportCSV importCSV) {
        this.linhaCategoriaService = linhaCategoriaService;
        this.importCSV = importCSV;
    }

    @PostMapping
    public LinhaCategoriaDTO save(@Valid @RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Recebendo solicitação de persistência de linha de categoria...");
        LOGGER.debug("Payaload: {}", linhaCategoriaDTO);

        return this.linhaCategoriaService.save(linhaCategoriaDTO);
    }

    @GetMapping("/{id}")
    public LinhaCategoriaDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.linhaCategoriaService.findById(id);
    }

    @PutMapping("/{id}")
    public LinhaCategoriaDTO udpate(@PathVariable("id") Long id, @RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Recebendo Update para linha de categoria de ID: {}", id);
        LOGGER.debug("Payload: {}", linhaCategoriaDTO);

        return this.linhaCategoriaService.update(linhaCategoriaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para linha de categoria de ID: {}", id);

        this.linhaCategoriaService.delete(id);
    }

    @PostMapping (value = "/import-linhas", consumes = "multipart/form-data")
    public void importCSV (@RequestParam("file") MultipartFile importLinhaCategoria) {
        importCSV.importLinhaCategoriaCSV(importLinhaCategoria);
    }

    @GetMapping("/export-linhas")
    public void exportCSV(HttpServletResponse response) throws IOException {
        LOGGER.info("Exportando CSV");

        this.linhaCategoriaService.exportCSV(response);
    }

}