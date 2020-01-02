package br.com.hbsis.produto;

import br.com.hbsis.exportimportcsv.ImportCSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/produtos")
public class ProdutoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoRest.class);

    private final ProdutoService produtoService;
    private final ImportCSV importCSV;

    @Autowired
    public ProdutoRest(ProdutoService produtoService, ImportCSV importCSV) {
        this.produtoService = produtoService;
        this.importCSV = importCSV;
    }

    @PostMapping
    public ProdutoDTO save(@Valid @RequestBody ProdutoDTO produtoDTO) {
        LOGGER.info("Recebendo solicitação de persistência de produto...");
        LOGGER.debug("Payaload: {}", produtoDTO);

        return this.produtoService.save(produtoDTO);
    }

    @GetMapping("/{id}")
    public ProdutoDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.produtoService.findById(id);
    }

    @PutMapping("/{id}")
    public ProdutoDTO udpate(@PathVariable("id") Long id, @RequestBody ProdutoDTO produtoDTO) {
        LOGGER.info("Recebendo Update para produto de ID: {}", id);
        LOGGER.debug("Payload: {}", produtoDTO);

        return this.produtoService.update(produtoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para produto de ID: {}", id);

        this.produtoService.delete(id);
    }

    @PostMapping (value = "/import-produtos", consumes = "multipart/form-data")
    public void importCSV (@RequestParam("file") MultipartFile importProduto) {
        importCSV.importProdutoCSV(importProduto);
    }

    @PostMapping (value = "/import-produtos/{id}", consumes = "multipart/form-data")
    public void importCSVPorFornecedor (@RequestParam("file") MultipartFile importProdutoPorFornecedor, @PathVariable("id") Long id)throws IOException {
        this.produtoService.importCSVPorFornecedor(importProdutoPorFornecedor, id);
    }

    @GetMapping("/export-produtos")
    public void exportCSV(HttpServletResponse response) throws IOException, ParseException {
        LOGGER.info("Exportando CSV");

        this.produtoService.exportCSV(response);
    }

}