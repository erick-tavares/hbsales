package br.com.hbsis.exportimportcsv;

import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ImportCSV {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

    public List<List<String>> importCSV(MultipartFile importCategoria) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";
        List<List<String>> listaString = new ArrayList<>();

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importCategoria.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();

            while ((linhaDoArquivo = leitor.readLine()) != null) {

                 String[] linhas= linhaDoArquivo.split(quebraDeLinha);
                List<String> colunas = new ArrayList<>(Arrays.asList(linhas));
                listaString.add(colunas);
            }
        } catch (IOException e) {
            LOGGER.error("Erro ao importar a CSV");
        }
        return listaString;
     }

    public List<List<String>> importCSVPorFornecedor(MultipartFile importCategoria) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";
        List<List<String>> listaString = new ArrayList<>();

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importCategoria.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();

            while ((linhaDoArquivo = leitor.readLine()) != null) {

                String[] linhas= linhaDoArquivo.split(quebraDeLinha);
                List<String> colunas = new ArrayList<>(Arrays.asList(linhas));
                listaString.add(colunas);
            }
        } catch (IOException e) {
            LOGGER.error("Erro ao importar a CSV");
        }
        return listaString;
    }

}
