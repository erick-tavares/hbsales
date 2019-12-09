package br.com.hbsis.categoriaproduto;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import freemarker.template.utility.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final FornecedorService fornecedorService;


    public CategoriaProdutoService(ICategoriaProdutoRepository iCategoriaProdutoRepository, FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;

    }

    public String gerarCodigoCategoria(Long idFornecedor, String codigoDoUsuario ) {

        String codigoCategoria = "";

        String fornecedorCnpj = "";
        FornecedorDTO fornecedorDTO = null;
        fornecedorDTO = fornecedorService.findById(idFornecedor);
        fornecedorCnpj = fornecedorDTO.getCnpj().substring(10, 14);


        //CategoriaProdutoDTO categoriaProdutoDTO = null;
        //categoriaProdutoDTO.getCodigo();

        String codigoGerado = "";
        codigoGerado = String.format("%03d", Integer.parseInt(codigoDoUsuario));

        codigoCategoria = "CAT" + fornecedorCnpj + codigoGerado;

        return codigoCategoria;
    }

    public CategoriaProdutoDTO save(CategoriaProdutoDTO categoriaProdutoDTO) {

        //Pegando o fornecedor completo do banco, pelo ID fornecedor da tabela categoria_produto
        this.validate(categoriaProdutoDTO);

        LOGGER.info("Salvando categoria");
        LOGGER.debug("Categoria: {}", categoriaProdutoDTO);

        CategoriaProduto categoriaProduto = new CategoriaProduto();
        categoriaProduto.setNome(categoriaProdutoDTO.getNome());
        categoriaProduto.setCodigo(gerarCodigoCategoria(categoriaProdutoDTO.getFornecedorId(),categoriaProdutoDTO.getCodigo()));
        categoriaProduto.setFornecedorId(fornecedorService.findFornecedorById(categoriaProdutoDTO.getFornecedorId()));

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

    ///Import CSV
    public void importCSV(MultipartFile importCategoria) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importCategoria.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();
            while ((linhaDoArquivo = leitor.readLine()) != null) {
                String[] categoriaCSV = linhaDoArquivo.split(quebraDeLinha);
                Optional<Fornecedor> fornecedorOptional = fornecedorService.findByIdOptional(Long.parseLong(categoriaCSV[3]));

                if (fornecedorOptional.isPresent()) {
                    CategoriaProduto categoriaProduto = new CategoriaProduto();
                    categoriaProduto.setNome(categoriaCSV[1]);
                    categoriaProduto.setCodigo(categoriaCSV[2]);
                    categoriaProduto.setFornecedorId(fornecedorOptional.get());

                    this.iCategoriaProdutoRepository.save(categoriaProduto);
                } else {
                    throw new IllegalArgumentException(String.format("Id %s não existe", fornecedorOptional));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /////////////////////////
    public Optional<CategoriaProduto> findByIdOptional(Long id) {
        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoOptional.isPresent()) {
            return categoriaProdutoOptional;
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public CategoriaProdutoDTO findById(Long id) {
        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoOptional.isPresent()) {
            return CategoriaProdutoDTO.of(categoriaProdutoOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public CategoriaProduto findCategoriaProdutoById(Long id) {
        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoOptional.isPresent()) {
            return categoriaProdutoOptional.get();
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
            categoriaProdutoExistente.setCodigo(gerarCodigoCategoria(categoriaProdutoDTO.getFornecedorId(),categoriaProdutoDTO.getCodigo()));
            categoriaProdutoExistente.setFornecedorId(fornecedorService.findFornecedorById(categoriaProdutoDTO.getFornecedorId()));

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
