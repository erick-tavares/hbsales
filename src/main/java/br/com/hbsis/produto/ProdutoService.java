package br.com.hbsis.produto;

import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.linhacategoria.LinhaCategoriaService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        this.validate(produtoDTO);

        LOGGER.info("Salvando produto");
        LOGGER.debug("Produto: {}", produtoDTO);

        Produto produto = new Produto();
        produto.setCodigo(gerarCodigoProduto(produtoDTO.getCodigo()));
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produto.setUnidadePorCaixa(produtoDTO.getUnidadePorCaixa());
        produto.setPesoPorUnidade(produtoDTO.getPesoPorUnidade());
        produto.setUnidadeMedidaPeso(produtoDTO.getUnidadeMedidaPeso());
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

        if (StringUtils.isEmpty(produtoDTO.getCodigo())) {
            throw new IllegalArgumentException("Código não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(produtoDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getPreco()))) {
            throw new IllegalArgumentException("Preço não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getUnidadePorCaixa()))) {
            throw new IllegalArgumentException("UnidadePorCaixa não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getPesoPorUnidade()))) {
            throw new IllegalArgumentException("PesoPorUnidade não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(produtoDTO.getUnidadeMedidaPeso())) {
            throw new IllegalArgumentException("UnidadeMedidaPeso não deve ser nulo/vazio");
        }

        if (!(produtoDTO.getUnidadeMedidaPeso().equalsIgnoreCase("kg")) && !(produtoDTO.getUnidadeMedidaPeso().equalsIgnoreCase("g"))
                && !(produtoDTO.getUnidadeMedidaPeso().equalsIgnoreCase("mg"))) {
            throw new IllegalArgumentException("UnidadeMedidaPeso só aceita os valore kg,g e mg");
        }

        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getValidade()))) {
            throw new IllegalArgumentException("Validade não deve ser nula/vazia");
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

    public Produto findByCodigo(String codigo) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigo(codigo);

        if (produtoOptional.isPresent()) {
            return produtoOptional.get();
        }
        throw new IllegalArgumentException(String.format("Código %s não existe", codigo));
    }

    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {
        Optional<Produto> produtoExistenteOptional = this.iProdutoRepository.findById(id);

        if (produtoExistenteOptional.isPresent()) {

            Produto produtoExistente = produtoExistenteOptional.get();

            LOGGER.info("Atualizando produto... id: [{}]", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("Produto Existente: {}", produtoExistente);

            produtoExistente.setCodigo(gerarCodigoProduto(produtoDTO.getCodigo()));
            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setPreco(produtoDTO.getPreco());
            produtoExistente.setUnidadePorCaixa(produtoDTO.getUnidadePorCaixa());
            produtoExistente.setPesoPorUnidade(produtoDTO.getPesoPorUnidade());
            produtoExistente.setUnidadeMedidaPeso(produtoDTO.getUnidadeMedidaPeso());
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


    public String gerarCodigoProduto(String codigoDoUsuario) {

        String codigoGerado = String.format("%10s", codigoDoUsuario).toUpperCase();
        codigoGerado = codigoGerado.replace(' ', '0');

        return codigoGerado;
    }

    public String gerarPrecoFormatado(double precoProduto) {

        String precoFormatado = new DecimalFormat("R$ #,##0.00").format(precoProduto);

        return precoFormatado;
    }

    public String formatarData(LocalDate dataValidade) {

        String dataFormatada = dataValidade.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return dataFormatada;
    }

    public void exportCSV(HttpServletResponse response) throws IOException, ParseException {
        String produtoCSV = "produto.csv";
        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", produtoCSV);
        response.setHeader(headerKey, headerValue);
        PrintWriter printWriter = response.getWriter();

        MaskFormatter mask = new MaskFormatter("##.###.###/####-##");
        mask.setValueContainsLiteralCharacters(false);

        String header = "Código do produto;Produto;Preço;Unidade por caixa;Peso por unidade;Validade;Código da linha da Categoria;" +
                "Linha da categoria;Código da categoria;Categoria;CNPJ Fornecedor;Fornecedor";
        printWriter.println(header);

        for (Produto produtoCSVObjeto : listarProduto()) {
            String produtoCodigo = gerarCodigoProduto(produtoCSVObjeto.getCodigo());
            String produtoNome = produtoCSVObjeto.getNome();
            String produtoPreco = gerarPrecoFormatado(produtoCSVObjeto.getPreco());
            String produtoUnidadePorCaixa = String.valueOf(produtoCSVObjeto.getUnidadePorCaixa());
            String produtoPesoPorUnidadeMedida = (produtoCSVObjeto.getPesoPorUnidade()) + produtoCSVObjeto.getUnidadeMedidaPeso();
            String produtoValidade = formatarData(produtoCSVObjeto.getValidade());

            String linhaCategoriaCodigo = produtoCSVObjeto.getLinhaCategoriaId().getCodigo();
            String linhaCategoriaNome = produtoCSVObjeto.getLinhaCategoriaId().getNome();

            String categoriaCodigo = produtoCSVObjeto.getLinhaCategoriaId().getCategoriaId().getCodigo();
            String categoriaNome = produtoCSVObjeto.getLinhaCategoriaId().getCategoriaId().getNome();

            String fornecedorCnpj = mask.valueToString(produtoCSVObjeto.getLinhaCategoriaId().getCategoriaId().getFornecedorId().getCnpj());
            String fornecedorRazaoSocial = produtoCSVObjeto.getLinhaCategoriaId().getCategoriaId().getFornecedorId().getRazaoSocial();

            printWriter.println(produtoCodigo + ";" + produtoNome + ";" + produtoPreco + ";" + produtoUnidadePorCaixa + ";" +
                    produtoPesoPorUnidadeMedida + ";" + produtoValidade + ";" + linhaCategoriaCodigo + ";" + linhaCategoriaNome +
                    ";" + categoriaCodigo + ";" + categoriaNome + ";" + fornecedorCnpj + ";" + fornecedorRazaoSocial);
        }
        printWriter.close();
    }

    public void importCSV(MultipartFile importProduto) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importProduto.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();
            while ((linhaDoArquivo = leitor.readLine()) != null) {
                String[] produtoCSV = linhaDoArquivo.split(quebraDeLinha);
                Optional<LinhaCategoria> linhaCategoriaOptional = Optional.ofNullable(linhaCategoriaService.findByCodigo(produtoCSV[6]));
                Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigo(produtoCSV[0]);

                if (!(produtoOptional.isPresent()) && linhaCategoriaOptional.isPresent()) {
                    Produto produto = new Produto();
                    produto.setCodigo(produtoCSV[0]);
                    produto.setNome(produtoCSV[1]);
                    produto.setPreco(Double.parseDouble(produtoCSV[2].replaceAll("\\D", "")));
                    produto.setUnidadePorCaixa(Integer.parseInt(produtoCSV[3]));
                    produto.setPesoPorUnidade(Double.parseDouble(produtoCSV[4].replaceAll("\\D", "")));
                    produto.setUnidadeMedidaPeso(produtoCSV[4].replaceAll("[^0-9]", ""));
                    produto.setValidade(LocalDate.parse((produtoCSV[5].replaceAll("/", "")), DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                    //produto.setValidade(LocalDate.parse("yyyy-MM-dd", DateTimeFormatter.ofPattern(produtoCSV[5].replaceAll("/", ""))));

                    LinhaCategoria linhaCategoria = linhaCategoriaService.findByCodigo(produtoCSV[6]);
                    produto.setLinhaCategoriaId(linhaCategoria);

                    this.iProdutoRepository.save(produto);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}