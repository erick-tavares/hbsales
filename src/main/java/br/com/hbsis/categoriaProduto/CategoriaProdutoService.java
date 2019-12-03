package br.com.hbsis.categoriaProduto;

import br.com.hbsis.fornecedor.FornecedorService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final FornecedorService fornecedorService;


    public CategoriaProdutoService(ICategoriaProdutoRepository iCategoriaProdutoRepository, FornecedorService fornecedorService) throws IOException {
        this.fornecedorService = fornecedorService;
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;

    }

    public CategoriaProdutoDTO save(CategoriaProdutoDTO categoriaProdutoDTO) {

        //Pegando o fornecedor completo do banco pelo ID fornecedor da tabela categoria_produto
        this.validate(categoriaProdutoDTO);

        LOGGER.info("Salvando categoria");
        LOGGER.debug("Categoria: {}", categoriaProdutoDTO);

        CategoriaProduto categoriaProduto = new CategoriaProduto();
        categoriaProduto.setNome(categoriaProdutoDTO.getNome());
        categoriaProduto.setFornecedorId(fornecedorService.findFornecedorById(categoriaProdutoDTO.getFornecedorId()));
        categoriaProduto.setCodigo(categoriaProdutoDTO.getCodigo());

        categoriaProduto = this.iCategoriaProdutoRepository.save(categoriaProduto);

        return CategoriaProdutoDTO.of(categoriaProduto);
    }

    private void validate(CategoriaProdutoDTO categoriaProdutoDTO) {
        LOGGER.info("Validando categoria");

        if (categoriaProdutoDTO == null) {
            throw new IllegalArgumentException("CategoriaProdutoDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nula/vazia");
        }
    }

    public List<CategoriaProduto> listarCategoria() {
        List<CategoriaProduto> categoriaProduto = this.iCategoriaProdutoRepository.findAll();
        return categoriaProduto;
    }

    ////// Exportando CSV, setando filename e conteúdo
    public String exportCSV(HttpServletResponse response) throws IOException {
        String categoriaProdutoCSV = "categoriaProduto.csv";
        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", categoriaProdutoCSV);
        response.setHeader(headerKey, headerValue);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

        String[] header = {"id", "nome", "codigo", "fornecedorId"};
        csvWriter.writeHeader(header);

        for (CategoriaProduto categoriaCSVObjeto : listarCategoria()) {
            csvWriter.write(categoriaCSVObjeto, header);
        }
        csvWriter.close();

        return csvWriter.toString();
    }

    public CategoriaProdutoDTO findById(Long id) {
        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoOptional.isPresent()) {
            return CategoriaProdutoDTO.of(categoriaProdutoOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public CategoriaProdutoDTO update(CategoriaProdutoDTO categoriaProdutoDTO, Long id) {
        Optional<CategoriaProduto> categoriProdutoExistenteOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriProdutoExistenteOptional.isPresent()) {

            CategoriaProduto categoriaProdutoExistente = categoriProdutoExistenteOptional.get();

            LOGGER.info("Atualizando categoria... id: [{}]", categoriaProdutoExistente.getId());
            LOGGER.debug("Payload: {}", categoriaProdutoDTO);
            LOGGER.debug("Categoria Existente: {}", categoriaProdutoExistente);

            categoriaProdutoExistente.setNome(categoriaProdutoDTO.getNome());
            categoriaProdutoExistente.setFornecedorId(fornecedorService.findFornecedorById(categoriaProdutoDTO.getFornecedorId()));
            categoriaProdutoExistente.setCodigo(categoriaProdutoDTO.getCodigo());

            categoriaProdutoExistente = this.iCategoriaProdutoRepository.save(categoriaProdutoExistente);

            return CategoriaProdutoDTO.of(categoriaProdutoExistente);
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para categoria de ID: [{}]", id);

        this.iCategoriaProdutoRepository.deleteById(id);
    }
}
