package br.com.hbsis.produto;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import br.com.hbsis.categoriaproduto.ICategoriaProdutoRepository;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.linhacategoria.LinhaCategoriaDTO;
import br.com.hbsis.linhacategoria.LinhaCategoriaService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;
import javax.validation.constraints.Size;
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
    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final ICategoriaProdutoRepository iLinhaCategoriaRepository;
    private final LinhaCategoriaService linhaCategoriaService;
    private final CategoriaProdutoService categoriaProdutoService;
    private final FornecedorService fornecedorService;


    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService, CategoriaProdutoService categoriaProdutoService,
                          FornecedorService fornecedorService, ICategoriaProdutoRepository iCategoriaProdutoRepository, ICategoriaProdutoRepository iLinhaCategoriaRepository) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
        this.categoriaProdutoService = categoriaProdutoService;
        this.fornecedorService = fornecedorService;
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
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
        if (produtoDTO.getCodigo().length() > 10) {
            throw new IllegalArgumentException("Código não deve ter mais que 10 digitos");
        }
        if (StringUtils.isEmpty(produtoDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }
        if (produtoDTO.getNome().length() > 200) {
            throw new IllegalArgumentException("Nome não deve ter mais que 200 caracteres");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getPreco()))) {
            throw new IllegalArgumentException("Preço não deve ser nulo/vazio");
        }
        if (!(StringUtils.isNumeric(String.valueOf(produtoDTO.getPreco())))) {
            throw new IllegalArgumentException("Preço deve conter apenas números");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getUnidadePorCaixa()))) {
            throw new IllegalArgumentException("UnidadePorCaixa não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getPesoPorUnidade()))) {
            throw new IllegalArgumentException("PesoPorUnidade não deve ser nulo/vazio");
        }
        if (!(StringUtils.isNumeric(String.valueOf(produtoDTO.getPesoPorUnidade())))) {
            throw new IllegalArgumentException("PesoPorUnidade deve conter apenas números");
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
                    produto.setCodigo(gerarCodigoProduto(produtoCSV[0]));
                    produto.setNome(produtoCSV[1]);
                    produto.setPreco(Double.parseDouble(produtoCSV[2].replaceAll("R\\$", "").replace(",", ".")));
                    produto.setUnidadePorCaixa(Integer.parseInt(produtoCSV[3]));
                    produto.setPesoPorUnidade(Double.parseDouble(produtoCSV[4].replaceAll("[kgm]", "").replace(",", ".")));
                    produto.setUnidadeMedidaPeso(produtoCSV[4].replaceAll("[\\d,]", ""));

                    produto.setValidade(LocalDate.parse(produtoCSV[5].replaceAll("/", "-"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                    LinhaCategoria linhaCategoria = linhaCategoriaService.findByCodigo(produtoCSV[6]);
                    produto.setLinhaCategoriaId(linhaCategoria);

                    this.iProdutoRepository.save(produto);
                    LOGGER.info("Importando produto... id: [{}]");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importCSVPorFornecedor(MultipartFile importProdutoPorFornecedor, Long id) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importProdutoPorFornecedor.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();
            while ((linhaDoArquivo = leitor.readLine()) != null) {
                String[] produtoCSV = linhaDoArquivo.split(quebraDeLinha);
                Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigo(produtoCSV[0]);
                Optional<LinhaCategoria> linhaCategoriaOptional = Optional.ofNullable(linhaCategoriaService.findByCodigo(produtoCSV[7]));
                Optional<CategoriaProduto> categoriaProdutoOptional = Optional.ofNullable(categoriaProdutoService.findByCodigo(produtoCSV[9]));


                //Se o produto não existe
                if (!(produtoOptional.isPresent())) {
                    Produto produto = new Produto();
                    produto.setCodigo(gerarCodigoProduto(produtoCSV[0]));
                    produto.setNome(produtoCSV[1]);
                    produto.setPreco(Double.parseDouble(produtoCSV[2].replaceAll("R\\$", "").replace(",", ".")));
                    produto.setUnidadePorCaixa(Integer.parseInt(produtoCSV[3]));
                    produto.setPesoPorUnidade(Double.parseDouble(produtoCSV[4].replace(",", ".")));
                    produto.setUnidadeMedidaPeso(produtoCSV[5]);
                    produto.setValidade(LocalDate.parse(produtoCSV[6].replaceAll("/", "-"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                    LinhaCategoria linhaCategoria = linhaCategoriaService.findByCodigo(produtoCSV[7]);
                    produto.setLinhaCategoriaId(linhaCategoria);

                    this.iProdutoRepository.save(produto);
                    LOGGER.info("Criando produto... id: [{}]");
                }

                //Se o produto existe
                if (produtoOptional.isPresent()) {

                    Produto produtoExixtente = produtoOptional.get();

                    produtoExixtente.setCodigo(gerarCodigoProduto(produtoCSV[0]));
                    produtoExixtente.setNome(produtoCSV[1]);
                    produtoExixtente.setPreco(Double.parseDouble(produtoCSV[2].replaceAll("R\\$", "").replace(",", ".")));
                    produtoExixtente.setUnidadePorCaixa(Integer.parseInt(produtoCSV[3]));
                    produtoExixtente.setPesoPorUnidade(Double.parseDouble(produtoCSV[4].replace(",", ".")));
                    produtoExixtente.setUnidadeMedidaPeso(produtoCSV[5]);
                    produtoExixtente.setValidade(LocalDate.parse(produtoCSV[6].replaceAll("/", "-"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                    LinhaCategoria linhaCategoria = linhaCategoriaService.findByCodigo(produtoCSV[7]);
                    produtoExixtente.setLinhaCategoriaId(linhaCategoria);

                    this.iProdutoRepository.save(produtoExixtente);
                    LOGGER.info("Atualizando produto... id: [{}]", produtoOptional.get());
                }

                //Se a categoria produto não existe
                if (produtoOptional.isPresent() && !(categoriaProdutoOptional.isPresent())) {

                    CategoriaProduto categoriaProduto = new CategoriaProduto();
                    categoriaProduto.setNome(produtoCSV[9]);
                    categoriaProduto.setCodigo(produtoCSV[8]);

                    categoriaProduto.setFornecedorId(fornecedorService.findFornecedorById(id));

                    this.iCategoriaProdutoRepository.save(categoriaProduto);
                    LOGGER.info("Criando categoria de produto... id: [{}]", categoriaProdutoOptional.get());
                }

                //Se a categoria produto existe
                if (produtoOptional.isPresent() && categoriaProdutoOptional.isPresent()) {

                    CategoriaProduto categoriaProdutoExiste = categoriaProdutoOptional.get();

                    categoriaProdutoExiste.setNome(produtoCSV[10]);
                    categoriaProdutoExiste.setCodigo(produtoCSV[9]);

                    categoriaProdutoExiste.setFornecedorId(fornecedorService.findFornecedorById(id));

                    this.iCategoriaProdutoRepository.save(categoriaProdutoExiste);
                    LOGGER.info("Alterando categoria de produto... id: [{}]", categoriaProdutoOptional.get());
                }

                //Se a linha da categoria não existe
                if (produtoOptional.isPresent() && !(linhaCategoriaOptional.isPresent())) {

                    LinhaCategoria linhaCategoria = new LinhaCategoria();
                    linhaCategoria.setNome(produtoCSV[8]);
                    linhaCategoria.setCodigo(produtoCSV[7]);

                    linhaCategoria.setCategoriaId(categoriaProdutoService.findCategoriaProdutoById(id));

                    this.linhaCategoriaService.save(LinhaCategoriaDTO.of(linhaCategoria));
                    LOGGER.info("Criando linha de categoria ... id: [{}]", linhaCategoriaOptional.get());
                }

                //Se a linha da categoria existe
                if (produtoOptional.isPresent() && linhaCategoriaOptional.isPresent()) {

                    LinhaCategoria linhaCategoriaExiste = linhaCategoriaOptional.get();
                    linhaCategoriaExiste.setNome(produtoCSV[8]);
                    linhaCategoriaExiste.setCodigo(produtoCSV[7]);

                    linhaCategoriaExiste.setCategoriaId(categoriaProdutoService.findCategoriaProdutoById(id));

                    this.linhaCategoriaService.save(LinhaCategoriaDTO.of(linhaCategoriaExiste));
                    LOGGER.info("Atualizando linha de categoria ... id: [{}]", linhaCategoriaOptional.get());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}