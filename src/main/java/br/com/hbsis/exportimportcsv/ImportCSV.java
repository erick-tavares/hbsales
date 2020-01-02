package br.com.hbsis.exportimportcsv;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoDTO;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

@SpringBootApplication
public class ImportCSV {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);
    private final FornecedorService fornecedorService;
    private final CategoriaProdutoService categoriaProdutoService;

    public ImportCSV(FornecedorService fornecedorService, CategoriaProdutoService categoriaProdutoService) {
        this.fornecedorService = fornecedorService;
        this.categoriaProdutoService = categoriaProdutoService;
    }


    public void importCategoriaCSV(MultipartFile importCategoria) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importCategoria.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();
            while ((linhaDoArquivo = leitor.readLine()) != null) {
                String[] categoriaCSV = linhaDoArquivo.split(quebraDeLinha);
                Optional<Fornecedor> fornecedorOptional = Optional.ofNullable(fornecedorService.findByCnpj(categoriaCSV[3].replaceAll("\\D", "")));
                Optional<CategoriaProduto> categoriaProdutoExisteOptional = Optional.ofNullable(categoriaProdutoService.findByCodigo(categoriaCSV[1]));

                if (!(categoriaProdutoExisteOptional.isPresent()) && fornecedorOptional.isPresent()) {
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
}
