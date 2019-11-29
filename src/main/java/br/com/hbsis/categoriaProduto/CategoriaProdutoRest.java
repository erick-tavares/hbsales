package br.com.hbsis.categoriaProduto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
    @RequestMapping("/categorias")
    public class CategoriaProdutoRest {
        private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoRest.class);

        private final CategoriaProdutoService categoriaProdutoService;

        @Autowired
        public CategoriaProdutoRest(CategoriaProdutoService categoriaProdutoService) {
            this.categoriaProdutoService = categoriaProdutoService;
        }

        @PostMapping
        public CategoriaProdutoDTO save(@RequestBody CategoriaProdutoDTO categoriaProdutoDTO) {
            LOGGER.info("Recebendo solicitação de persistência de categoria...");
            LOGGER.debug("Payaload: {}", categoriaProdutoDTO);

            return this.categoriaProdutoService.save(categoriaProdutoDTO);
        }

        @GetMapping("/{id}")
        public CategoriaProdutoDTO find(@PathVariable("id") Long id) {

            LOGGER.info("Recebendo find by ID... id: [{}]", id);

            return this.categoriaProdutoService.findById(id);
        }
        // Exportando CSV, setando filename e conteúdo
        @RequestMapping(value = "/export-categorias")
        public void exportCSV (HttpServletResponse response) throws IOException {
            String categoriaProdutoCSV = "categoriaProduto.csv";
            response.setContentType("text/csv");

            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", categoriaProdutoCSV);
            response.setHeader(headerKey, headerValue);

            List<CategoriaProduto> listaDeCategoria = categoriaProdutoService.listarCategoria();

            ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                    CsvPreference.STANDARD_PREFERENCE);

            String[] header = { "id", "nome", "codigo", "fornecedorCategoria" };
            csvWriter.writeHeader (header);

            for ( CategoriaProduto categoriaCSVObjeto : listaDeCategoria) {
                csvWriter.write(categoriaCSVObjeto, header);
            }
                csvWriter.close();
        }

        @PutMapping("/{id}")
        public CategoriaProdutoDTO udpate(@PathVariable("id") Long id, @RequestBody CategoriaProdutoDTO categoriaProdutoDTO) {
            LOGGER.info("Recebendo Update para categoria de ID: {}", id);
            LOGGER.debug("Payload: {}", categoriaProdutoDTO);

            return this.categoriaProdutoService.update(categoriaProdutoDTO, id);
        }

        @DeleteMapping("/{id}")
        public void delete(@PathVariable("id") Long id) {
            LOGGER.info("Recebendo Delete para categoria de ID: {}", id);

            this.categoriaProdutoService.delete(id);
        }

        //////////////////////////////////
    /*
    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", categoriaProdutoService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(CategoriaProdutoRest.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = categoriaProdutoService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        categoriaProdutoService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
*/



    }
