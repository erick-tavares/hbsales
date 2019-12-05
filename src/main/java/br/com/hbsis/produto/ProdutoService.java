package br.com.hbsis.produto;

import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.linhacategoria.LinhaCategoriaService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final LinhaCategoriaService linhaCategoriaService;


    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
    }

    public ProdutoDTO save(ProdutoDTO produtoDTO) {

        //Pegando o linha de categoria completo do banco, pelo ID linha categoria da tabela produto
        this.validate(produtoDTO);

        LOGGER.info("Salvando produto");
        LOGGER.debug("Produto: {}", produtoDTO);

        Produto produto = new Produto();
        produto.setCodigo(produtoDTO.getCodigo());
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produto.setUnidadePorCaixa(produtoDTO.getUnidadePorCaixa());
        produto.setPesoPorUnidade(produtoDTO.getPesoPorUnidade());
        produto.setValidade(produtoDTO.getValidade());

        produto.setLinhaCategoriaId(linhaCategoriaService.findLinhaCategoriaById(produtoDTO.getLinhaCategoriaId()));

        produto = this.iProdutoRepository.save(produto);

        return produtoDTO.of(produto);
    }

    private void validate(ProdutoDTO produtoDTO) {
        LOGGER.info("Validando produto");

        if (produtoDTO == null) {
            throw new IllegalArgumentException("ProdutoDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(produtoDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }
    }

    public List<Produto> listarProduto() {
        List<Produto> produto = this.iProdutoRepository.findAll();
        return produto;
    }

    public ProdutoDTO findById(Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            return ProdutoDTO.of(produtoOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {
        Optional<Produto> produtoExistenteOptional = this.iProdutoRepository.findById(id);

        if (produtoExistenteOptional.isPresent()) {

            Produto produtoExistente = produtoExistenteOptional.get();

            LOGGER.info("Atualizando produto... id: [{}]", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("Produto Existente: {}", produtoExistente);

            produtoExistente.setCodigo(produtoDTO.getCodigo());
            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setPreco(produtoDTO.getPreco());
            produtoExistente.setUnidadePorCaixa(produtoDTO.getUnidadePorCaixa());
            produtoExistente.setPesoPorUnidade(produtoDTO.getPesoPorUnidade());
            produtoExistente.setValidade(produtoDTO.getValidade());

            produtoExistente.setLinhaCategoriaId(linhaCategoriaService.findLinhaCategoriaById(produtoDTO.getLinhaCategoriaId()));

            produtoExistente = this.iProdutoRepository.save(produtoExistente);

            return ProdutoDTO.of(produtoExistente);
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para produto de ID: [{}]", id);

        this.iProdutoRepository.deleteById(id);
    }

    ////// Exportando CSV
    public String exportCSV(HttpServletResponse response) throws IOException {
        String produtoCSV = "produto.csv";
        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", produtoCSV);
        response.setHeader(headerKey, headerValue);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

        String[] header = {"id", "codigo","nome", "preco", "unidadePorCaixa", "pesoPorUnidade", "validade", "linhaCategoriaId"};
        csvWriter.writeHeader(header);

        for (Produto produtoCSVObjeto : listarProduto()) {
            csvWriter.write(produtoCSVObjeto, header);
        }
        csvWriter.close();

        return csvWriter.toString();
    }

    ///Import CSV
    public void importCSV(MultipartFile importProduto) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importProduto.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();
            while ((linhaDoArquivo = leitor.readLine()) != null) {
                String[] produtoCSV = linhaDoArquivo.split(quebraDeLinha);
                Optional<LinhaCategoria> linhaCategoriaOptional = linhaCategoriaService.findByIdOptional(Long.parseLong(produtoCSV[7]));

                if (linhaCategoriaOptional.isPresent()) {
                    Produto produto = new Produto();
                    produto.setCodigo(Integer.parseInt(produtoCSV[1]));
                    produto.setNome(produtoCSV[2]);
                    produto.setPreco(Double.parseDouble(produtoCSV[3]));
                    produto.setUnidadePorCaixa(Integer.parseInt(produtoCSV[4]));
                    produto.setPesoPorUnidade(Double.parseDouble(produtoCSV[5]));
                    produto.setValidade(LocalDateTime.parse(produtoCSV[6]));
                    produto.setLinhaCategoriaId(linhaCategoriaOptional.get());

                    this.iProdutoRepository.save(produto);
                } else {
                    throw new IllegalArgumentException(String.format("Id %s não existe", linhaCategoriaOptional));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    ///Import CSV por Fornecedor
    public void importPorFornecedorCSV(MultipartFile importProdutoFornecedor) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importProdutoFornecedor.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();
            while ((linhaDoArquivo = leitor.readLine()) != null) {
                String[] produtoFornecedorCSV = linhaDoArquivo.split(quebraDeLinha);
                Optional<LinhaCategoria> linhaCategoriaOptional = linhaCategoriaService.findByIdOptional(Long.parseLong(produtoCSV[7]));

                if (linhaCategoriaOptional.isPresent()) {
                    Produto produto = new Produto();
                    produto.setCodigo(Integer.parseInt(produtoCSV[1]));
                    produto.setNome(produtoCSV[2]);
                    produto.setPreco(Double.parseDouble(produtoCSV[3]));
                    produto.setUnidadePorCaixa(Integer.parseInt(produtoCSV[4]));
                    produto.setPesoPorUnidade(Double.parseDouble(produtoCSV[5]));
                    produto.setValidade(LocalDateTime.parse(produtoCSV[6]));
                    produto.setLinhaCategoriaId(linhaCategoriaOptional.get());

                    this.iProdutoRepository.save(produto);
                } else {
                    throw new IllegalArgumentException(String.format("Id %s não existe", linhaCategoriaOptional));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    */

}