package br.com.hbsis.exportimportcsv;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoDTO;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.linhacategoria.LinhaCategoriaDTO;
import br.com.hbsis.linhacategoria.LinhaCategoriaService;
import br.com.hbsis.produto.Produto;
import br.com.hbsis.produto.ProdutoDTO;
import br.com.hbsis.produto.ProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@SpringBootApplication
@Component
public class ImportCSV {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);
    private final FornecedorService fornecedorService;
    private final CategoriaProdutoService categoriaProdutoService;
    private final LinhaCategoriaService linhaCategoriaService;
    private final ProdutoService produtoService;

    public ImportCSV(FornecedorService fornecedorService, CategoriaProdutoService categoriaProdutoService, LinhaCategoriaService linhaCategoriaService, ProdutoService produtoService) {
        this.fornecedorService = fornecedorService;
        this.categoriaProdutoService = categoriaProdutoService;
        this.linhaCategoriaService = linhaCategoriaService;
        this.produtoService = produtoService;
    }

    public void importCategoriaCSV(MultipartFile importCategoria) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importCategoria.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();
            while ((linhaDoArquivo = leitor.readLine()) != null) {
                String[] categoriaCSV = linhaDoArquivo.split(quebraDeLinha);
                Optional<Fornecedor> fornecedorOptional = Optional.ofNullable(fornecedorService.findByCnpj(categoriaCSV[3].replaceAll("\\D", "")));
                Optional<CategoriaProduto> categoriaProdutoExiste = categoriaProdutoService.findByCodigoOptional(categoriaCSV[1]);

                if (!(categoriaProdutoExiste.isPresent()) && fornecedorOptional.isPresent()) {
                    CategoriaProduto categoriaProduto = new CategoriaProduto();
                    categoriaProduto.setNome(categoriaCSV[0]);
                    categoriaProduto.setCodigo(categoriaCSV[1]);

                    Fornecedor fornecedor = fornecedorService.findByCnpj(categoriaCSV[3].replaceAll("\\D", ""));
                    categoriaProduto.setFornecedorId(fornecedor);

                    categoriaProdutoService.save(CategoriaProdutoDTO.of(categoriaProduto));
                    LOGGER.info("Importando categoria de produto... id: [{}]", categoriaProduto.getId());
                }
            }
        } catch (IOException e) {
            LOGGER.error ("Erro ao importar a categoria");
        }
        
    }

    public void importLinhaCategoriaCSV(MultipartFile importLinhaCategoria) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importLinhaCategoria.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();
            while ((linhaDoArquivo = leitor.readLine()) != null) {
                String[] linhaCategoriaCSV = linhaDoArquivo.split(quebraDeLinha);
                Optional<CategoriaProduto> categoriaProdutoOptional = Optional.ofNullable(categoriaProdutoService.findByCodigo(linhaCategoriaCSV[2]));
                Optional<LinhaCategoria> linhaCategoriaExisteOptional = linhaCategoriaService.findByCodigoOptional(linhaCategoriaCSV[0]);


                if (!(linhaCategoriaExisteOptional.isPresent()) && categoriaProdutoOptional.isPresent()) {
                    LinhaCategoria linhaCategoria = new LinhaCategoria();
                    linhaCategoria.setCodigo(linhaCategoriaCSV[0]);
                    linhaCategoria.setNome(linhaCategoriaCSV[1]);

                    CategoriaProduto categoriaProduto = categoriaProdutoService.findByCodigo(linhaCategoriaCSV[2]);
                    linhaCategoria.setCategoriaId(categoriaProduto);

                   linhaCategoriaService.save(LinhaCategoriaDTO.of(linhaCategoria));
                    LOGGER.info("Importando linha de categoria... id: [{}]", linhaCategoria.getId());
                }
            }

        } catch (IOException e) {
            LOGGER.error ("Erro ao importar a linha da categoria");
        }
    }

    public void importProdutoCSV(MultipartFile importProduto) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importProduto.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();
            while ((linhaDoArquivo = leitor.readLine()) != null) {
                String[] produtoCSV = linhaDoArquivo.split(quebraDeLinha);
                Optional<LinhaCategoria> linhaCategoriaOptional = Optional.ofNullable(linhaCategoriaService.findByCodigo(produtoCSV[6]));
                Optional<Produto> produtoOptional = produtoService.findByCodigoOptional(produtoCSV[0]);

                if (!(produtoOptional.isPresent()) && linhaCategoriaOptional.isPresent()) {
                    Produto produto = new Produto();
                    produto.setCodigo(produtoCSV[0]);
                    produto.setNome(produtoCSV[1]);
                    produto.setPreco(Double.parseDouble(produtoCSV[2].replaceAll("R\\$", "").replace(",", ".")));
                    produto.setUnidadePorCaixa(Integer.parseInt(produtoCSV[3]));
                    produto.setPesoPorUnidade(Double.parseDouble(produtoCSV[4].replaceAll("[kgm]", "").replace(",", ".")));
                    produto.setUnidadeMedidaPeso(produtoCSV[4].replaceAll("[\\d,]", ""));
                    produto.setValidade(LocalDate.parse(produtoCSV[5].replaceAll("/", "-"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                    LinhaCategoria linhaCategoria = linhaCategoriaService.findByCodigo(produtoCSV[6]);
                    produto.setLinhaCategoriaId(linhaCategoria);

                    produtoService.save(ProdutoDTO.of(produto));
                    LOGGER.info("Importando produto... id: [{}]", produto.getId());
                }
            }
        } catch (IOException e) {
            LOGGER.error("Erro ao importar produto");
        }
    }

}
