package br.com.hbsis.produto;

import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import br.com.hbsis.exportimportcsv.ExportCSV;
import br.com.hbsis.exportimportcsv.ImportCSV;
import br.com.hbsis.fornecedor.FornecedorService;
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
import java.util.Optional;

@Service
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final LinhaCategoriaService linhaCategoriaService;
    private final CategoriaProdutoService categoriaProdutoService;
    private final FornecedorService fornecedorService;
    private final ExportCSV exportCSV;


    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService,
                          CategoriaProdutoService categoriaProdutoService, FornecedorService fornecedorService, ExportCSV exportCSV) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
        this.categoriaProdutoService = categoriaProdutoService;
        this.fornecedorService = fornecedorService;
        this.exportCSV = exportCSV;

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

    public Produto findByCodigo(String codigo) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigo(codigo);

        if (produtoOptional.isPresent()) {
            return produtoOptional.get();
        }
        throw new IllegalArgumentException(String.format("Código %s não existe", codigo));
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

        return produtoDTO.of(produto);
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


   // public void importCSVPorFornecedor(MultipartFile importProdutoPorFornecedor, Long id) throws IOException {




//        String linhaDoArquivo = "";
//        String quebraDeLinha = ";";
//
//        BufferedReader leitor = new BufferedReader(new InputStreamReader(importProdutoPorFornecedor.getInputStream()));
//
//        linhaDoArquivo = leitor.readLine();
//        while ((linhaDoArquivo = leitor.readLine()) != null) {
//            String[] produtoCSV = linhaDoArquivo.split(quebraDeLinha);
//
//            FornecedorDTO fornecedorExiste = fornecedorService.findById(id);
//
//            Optional<CategoriaProduto> categoriaProdutoExistente = categoriaProdutoService.findByCodigoOptional(produtoCSV[9]);
//            if (categoriaProdutoExistente.isPresent() && fornecedorExiste != null) {
//
//                CategoriaProduto categoriaProduto = categoriaProdutoExistente.get();
//                categoriaProduto.setCodigo(produtoCSV[9].substring(7,10));
//                categoriaProduto.setFornecedorId(fornecedorService.findFornecedorById(id));
//
//                categoriaProdutoService.update(CategoriaProdutoDTO.of(categoriaProduto), categoriaProdutoExistente.get().getId());
//                LOGGER.info("Alterando categoria de produto... id: [{}]", categoriaProdutoExistente.get());
//            }
//
//
//
//            Optional<LinhaCategoria> linhaCategoriaExistente = linhaCategoriaService.findByCodigoOptional(produtoCSV[7]);
//            if (linhaCategoriaExistente.isPresent()) {
//
//                LinhaCategoria linhaCategoria = linhaCategoriaExistente.get();
//                linhaCategoria.setCodigo(produtoCSV[7]);
//                linhaCategoria.setCategoriaId(categoriaProdutoService.findCategoriaProdutoById(categoriaProdutoService.findByCodigo(produtoCSV[9]).getId()));
//
//                linhaCategoriaService.update(LinhaCategoriaDTO.of(linhaCategoria), linhaCategoriaExistente.get().getId());
//                LOGGER.info("Atualizando linha de categoria ... id: [{}]", linhaCategoriaExistente.get());
//
//            }
//            if (!(linhaCategoriaExistente.isPresent())) {
//                LinhaCategoria linhaCategoria = new LinhaCategoria();
//                linhaCategoria.setCodigo(produtoCSV[7]);
//                linhaCategoria.setNome(produtoCSV[8]);
//                linhaCategoria.setCategoriaId(categoriaProdutoService.findCategoriaProdutoById(categoriaProdutoService.findByCodigo(produtoCSV[9]).getId()));
//
//                linhaCategoriaService.save(LinhaCategoriaDTO.of(linhaCategoria));
//                LOGGER.info("Criando nova linha de categoria");
//            }
//
//            Optional<Produto> produtoExistente = this.iProdutoRepository.findByCodigo(produtoCSV[0]);
//            if (produtoExistente.isPresent()) {
//
//                Produto produto = produtoExistente.get();
//                LinhaCategoria linhaCategoria = linhaCategoriaService.findByCodigo(produtoCSV[7]);
//                produto.setCodigo(produtoCSV[0]);
//                produto.setLinhaCategoriaId(linhaCategoria);
//
//                this.update(ProdutoDTO.of(produto), this.findByCodigo(produtoCSV[0]).getId());
//                LOGGER.info("Atualizando produto... id: [{}]", produtoExistente.get().getCodigo());
//            }
//            if (!(produtoExistente.isPresent())) {
//                Produto produto = new Produto();
//                produto.setCodigo(produtoCSV[0]);
//                produto.setNome(produtoCSV[1]);
//                produto.setPreco(Double.parseDouble(produtoCSV[2].replaceAll("R\\$", "").replace(",", ".")));
//                produto.setUnidadePorCaixa(Integer.parseInt(produtoCSV[3]));
//                produto.setPesoPorUnidade(Double.parseDouble(produtoCSV[4].replace(",", ".")));
//                produto.setUnidadeMedidaPeso(produtoCSV[5]);
//                produto.setValidade(LocalDate.parse(produtoCSV[6].replaceAll("/", "-"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//
//                LinhaCategoria linhaCategoria = linhaCategoriaService.findByCodigo(produtoCSV[7]);
//                produto.setLinhaCategoriaId(linhaCategoria);
//
//                this.iProdutoRepository.save(produto);
//                LOGGER.info("Criando novo produto");
//            }
//
//        }
//    }
//    }
}