package br.com.hbsis.produto;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoDTO;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import br.com.hbsis.exportimportcsv.ExportCSV;
import br.com.hbsis.exportimportcsv.ImportCSV;
import br.com.hbsis.fornecedor.Fornecedor;
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
import java.io.IOException;
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
    private final FornecedorService fornecedorService;
    private final CategoriaProdutoService categoriaProdutoService;
    private final ExportCSV exportCSV;
    private final ImportCSV importCSV;


    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService, FornecedorService fornecedorService, CategoriaProdutoService categoriaProdutoService, ExportCSV exportCSV, ImportCSV importCSV) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
        this.fornecedorService = fornecedorService;
        this.categoriaProdutoService = categoriaProdutoService;
        this.exportCSV = exportCSV;
        this.importCSV = importCSV;
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
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getUnidadePorCaixa()))) {
            throw new IllegalArgumentException("UnidadePorCaixa não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getPesoPorUnidade()))) {
            throw new IllegalArgumentException("PesoPorUnidade não deve ser nulo/vazio");
        }
        if (!(produtoDTO.getUnidadeMedidaPeso().equalsIgnoreCase("kg")) && !(produtoDTO.getUnidadeMedidaPeso().equalsIgnoreCase("g"))
                && !(produtoDTO.getUnidadeMedidaPeso().equalsIgnoreCase("mg"))) {
            throw new IllegalArgumentException("UnidadeMedidaPeso só aceita os valore kg,g e mg");
        }
        if (StringUtils.isEmpty(produtoDTO.getUnidadeMedidaPeso())) {
            throw new IllegalArgumentException("UnidadeMedidaPeso não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getValidade()))) {
            throw new IllegalArgumentException("Validade não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getLinhaCategoriaId()))) {
            throw new IllegalArgumentException("LinhaCategoriaId não deve ser nulo/vazio");
        }
    }

    public ProdutoDTO findById(Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            return ProdutoDTO.of(produtoOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Produto findProdutoById(Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            return produtoOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }


    public Optional<Produto> findByCodigoOptional(String codigo) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigo(codigo);

        return produtoOptional;
    }

    public ProdutoDTO save(ProdutoDTO produtoDTO) {

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

        this.validate(produtoDTO);

        produto = this.iProdutoRepository.save(produto);

        return ProdutoDTO.of(produto);
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

        if (codigoDoUsuario.length() < 10) {
            String codigoGerado = String.format("%10s", codigoDoUsuario).toUpperCase();
            codigoGerado = codigoGerado.replace(' ', '0');

            return codigoGerado;
        }
        return codigoDoUsuario;
    }

    public String gerarPrecoFormatado(double precoProduto) {
        String precoFormatado = new DecimalFormat("R$ #,##0.00").format(precoProduto);
        return precoFormatado;
    }

    public String formatarData(LocalDate dataValidade) {
        String dataFormatada = dataValidade.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return dataFormatada;
    }

    public void importProdutoCSV(MultipartFile importProduto) {

        List<List<String>> listaCSV = importCSV.importCSV(importProduto);

        for (List<String> strings : listaCSV) {

            String codigo = strings.get(0);
            String nome = strings.get(1);
            String preco = strings.get(2).replaceAll("R\\$", "").replace(",", ".");
            String unidadePorCaixa = strings.get(3);
            String pesoPorUnidade = strings.get(4).replaceAll("[kgm]", "").replace(",", ".");
            String unidadeMedidaPeso = strings.get(4).replaceAll("[\\d,]", "").replace(".", "");
            String validade = strings.get(5).replaceAll("/", "-");
            String linhaCategoriaCod = strings.get(6);

            Optional<LinhaCategoria> linhaCategoriaOptional = Optional.ofNullable(linhaCategoriaService.findByCodigo(linhaCategoriaCod));
            Optional<Produto> produtoExiste = this.findByCodigoOptional(codigo);

            if (produtoExiste.isPresent() && linhaCategoriaOptional.isPresent()) {

                Produto produto = produtoExiste.get();
                produto.setCodigo(codigo);
                produto.setNome(nome);
                produto.setPreco(Double.parseDouble(preco));
                produto.setUnidadePorCaixa(Integer.parseInt(unidadePorCaixa));
                produto.setPesoPorUnidade(Double.parseDouble(pesoPorUnidade));
                produto.setUnidadeMedidaPeso(unidadeMedidaPeso);
                produto.setValidade(LocalDate.parse(validade, DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                LinhaCategoria linhaCategoria = linhaCategoriaService.findByCodigo(linhaCategoriaCod);
                produto.setLinhaCategoriaId(linhaCategoria);

                this.update(ProdutoDTO.of(produto), produtoExiste.get().getId());
                LOGGER.info("Atualizando produto... id: [{}]", produto.getId());

            } else {
                Produto produto = new Produto();
                produto.setCodigo(codigo);
                produto.setNome(nome);
                produto.setPreco(Double.parseDouble(preco));
                produto.setUnidadePorCaixa(Integer.parseInt(unidadePorCaixa));
                produto.setPesoPorUnidade(Double.parseDouble(pesoPorUnidade));
                produto.setUnidadeMedidaPeso(unidadeMedidaPeso);
                produto.setValidade(LocalDate.parse(validade, DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                LinhaCategoria linhaCategoria = linhaCategoriaService.findByCodigo(linhaCategoriaCod);
                produto.setLinhaCategoriaId(linhaCategoria);

                this.save(ProdutoDTO.of(produto));
                LOGGER.info("Importando produto... id: [{}]", produto.getId());
            }
        }
    }

    public void importCSVCategoriaPorFornecedor(MultipartFile importCategoriaPorFornecedor, Long id) {
        List<List<String>> listaCSV = importCSV.importCSVPorFornecedor(importCategoriaPorFornecedor);

        for (List<String> strings : listaCSV) {

            String codigo = strings.get(9);
            String nome = strings.get(10);

            Optional<Fornecedor> fornecedorExiste = Optional.ofNullable(fornecedorService.findFornecedorById(id));
            Optional<CategoriaProduto> categoriaProdutoExistente = categoriaProdutoService.findByCodigoOptional(categoriaProdutoService.gerarCodigoCategoria(id, codigo));
            if (categoriaProdutoExistente.isPresent() && fornecedorExiste.isPresent()) {
                CategoriaProduto categoriaProduto = categoriaProdutoExistente.get();
                categoriaProduto.setCodigo(codigo);
                categoriaProduto.setFornecedorId(fornecedorService.findFornecedorById(id));

                categoriaProdutoService.update(CategoriaProdutoDTO.of(categoriaProduto), categoriaProdutoExistente.get().getId());
                LOGGER.info("Atualizando categoria de produto... id: [{}]", categoriaProduto.getId());
            } else {
                CategoriaProduto categoriaProduto = new CategoriaProduto();
                categoriaProduto.setNome(nome);
                categoriaProduto.setCodigo(codigo);
                categoriaProduto.setFornecedorId(fornecedorService.findFornecedorById(id));

                categoriaProdutoService.save(CategoriaProdutoDTO.of(categoriaProduto));
                LOGGER.info("Importando categoria de produto... id: [{}]", categoriaProduto.getId());
            }
        }
    }

    public void importCSVLinhaPorFornecedor(MultipartFile importLinhaPorFornecedor, Long id) {
        List<List<String>> listCSV = importCSV.importCSVPorFornecedor(importLinhaPorFornecedor);
        for (List<String> strings : listCSV) {
            String codigo = strings.get(7);
            String nome = strings.get(8);
            String categoria = strings.get(9);

            Optional<CategoriaProduto> categoriaProdutoExiste = categoriaProdutoService.findByCodigoOptional(categoriaProdutoService.gerarCodigoCategoria(id, categoria));
            Optional<LinhaCategoria> linhaCategoriaExistente = linhaCategoriaService.findByCodigoOptional(linhaCategoriaService.gerarCodigoLinhaCategoria(codigo));
            if (linhaCategoriaExistente.isPresent() && categoriaProdutoExiste.isPresent()) {

                LinhaCategoria linhaCategoria = linhaCategoriaExistente.get();
                linhaCategoria.setCodigo(codigo);
                linhaCategoria.setNome(nome);
                linhaCategoria.setCategoriaId(categoriaProdutoService.findCategoriaProdutoById(categoriaProdutoService.findByCodigo
                        (categoriaProdutoService.gerarCodigoCategoria(id, categoria)).getId()));

                linhaCategoriaService.update(LinhaCategoriaDTO.of(linhaCategoria), linhaCategoriaExistente.get().getId());
                LOGGER.info("Atualizando linha de categoria ... id: [{}]", linhaCategoria.getId());

            } else {
                LinhaCategoria linhaCategoria = new LinhaCategoria();
                linhaCategoria.setCodigo(codigo);
                linhaCategoria.setNome(nome);
                linhaCategoria.setCategoriaId(categoriaProdutoService.findCategoriaProdutoById
                        (categoriaProdutoService.findByCodigo(categoriaProdutoService.gerarCodigoCategoria(id, categoria)).getId()));

                linhaCategoriaService.save(LinhaCategoriaDTO.of(linhaCategoria));
                LOGGER.info("Importando linha de categoria... id: [{}]", linhaCategoria.getId());
            }
        }
    }

    public void importCSVProdutoPorFornecedor(MultipartFile importProdutoPorFornecedor, Long id) {
        List<List<String>> listaCSV = importCSV.importCSVPorFornecedor(importProdutoPorFornecedor);
        for (List<String> strings : listaCSV) {
            String codigo = strings.get(0);
            String nome = strings.get(1);
            String preco = strings.get(2).replaceAll("R\\$", "").replace(",", ".");
            String unidadePorCaixa = strings.get(3);
            String pesoPorUnidade = strings.get(4).replace(",", ".");
            String unidadeMedidaPeso = strings.get(5);
            String validade = strings.get(6).replaceAll("/", "-");
            String linhaCategoriaCod = strings.get(7);


            Optional<LinhaCategoria> linhaCategoriaExistente = linhaCategoriaService.findByCodigoOptional(linhaCategoriaService.gerarCodigoLinhaCategoria(linhaCategoriaCod));
            Optional<Produto> produtoExiste = this.findByCodigoOptional(this.gerarCodigoProduto(codigo));
            if (produtoExiste.isPresent() && linhaCategoriaExistente.isPresent()) {

                Produto produto = produtoExiste.get();
                produto.setCodigo(codigo);
                produto.setNome(nome);
                produto.setPreco(Double.parseDouble(preco));
                produto.setUnidadePorCaixa(Integer.parseInt(unidadePorCaixa));
                produto.setPesoPorUnidade(Double.parseDouble(pesoPorUnidade));
                produto.setUnidadeMedidaPeso(unidadeMedidaPeso);
                produto.setValidade(LocalDate.parse(validade, DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                LinhaCategoria linhaCategoria = linhaCategoriaService.findByCodigo(linhaCategoriaService.gerarCodigoLinhaCategoria(linhaCategoriaCod));
                produto.setLinhaCategoriaId(linhaCategoria);

                this.update(ProdutoDTO.of(produto), produtoExiste.get().getId());
                LOGGER.info("Atualizando produto... id: [{}]", produto.getId());

            } else {
                Produto produto = new Produto();
                produto.setCodigo(codigo);
                produto.setNome(nome);
                produto.setPreco(Double.parseDouble(preco));
                produto.setUnidadePorCaixa(Integer.parseInt(unidadePorCaixa));
                produto.setPesoPorUnidade(Double.parseDouble(pesoPorUnidade));
                produto.setUnidadeMedidaPeso(unidadeMedidaPeso);
                produto.setValidade(LocalDate.parse(validade, DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                LinhaCategoria linhaCategoria = linhaCategoriaService.findByCodigo(linhaCategoriaService.gerarCodigoLinhaCategoria(linhaCategoriaCod));
                produto.setLinhaCategoriaId(linhaCategoria);

                this.save(ProdutoDTO.of(produto));
                LOGGER.info("Importando produto... id: [{}]", produto.getId());
            }

        }
    }


    public void exportCSV(HttpServletResponse response) throws IOException, ParseException {
        String header = "Código do produto;Produto;Preço;Unidade por caixa;Peso por unidade;Validade;Código da linha da Categoria;" +
                "Linha da categoria;Código da categoria;Categoria;CNPJ Fornecedor;Fornecedor";
        exportCSV.exportarCSV(response, header);

        PrintWriter printWriter = response.getWriter();
        for (Produto produtoCSVObjeto : this.iProdutoRepository.findAll()) {
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

            String fornecedorCnpj = exportCSV.mask(produtoCSVObjeto.getLinhaCategoriaId().getCategoriaId().getFornecedorId().getCnpj());
            String fornecedorRazaoSocial = produtoCSVObjeto.getLinhaCategoriaId().getCategoriaId().getFornecedorId().getRazaoSocial();

            printWriter.println(produtoCodigo + ";" + produtoNome + ";" + produtoPreco + ";" + produtoUnidadePorCaixa + ";" +
                    produtoPesoPorUnidadeMedida + ";" + produtoValidade + ";" + linhaCategoriaCodigo + ";" + linhaCategoriaNome +
                    ";" + categoriaCodigo + ";" + categoriaNome + ";" + fornecedorCnpj + ";" + fornecedorRazaoSocial);
        }
        printWriter.close();
    }

}