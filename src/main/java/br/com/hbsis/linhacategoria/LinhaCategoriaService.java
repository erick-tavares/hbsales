package br.com.hbsis.linhacategoria;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);

    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaProdutoService categoriaProdutoService;

    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaProdutoService categoriaProdutoService) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.categoriaProdutoService = categoriaProdutoService;
    }

    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO) {

        this.validate(linhaCategoriaDTO);

        LOGGER.info("Salvando linha de categoria");
        LOGGER.debug("Linha de Categoria: {}", linhaCategoriaDTO);

        LinhaCategoria linhaCategoria = new LinhaCategoria();
        linhaCategoria.setCodigo(gerarCodigoLinhaCategoria(linhaCategoriaDTO.getCodigo()));
        linhaCategoria.setNome(linhaCategoriaDTO.getNome());

        linhaCategoria.setCategoriaId(categoriaProdutoService.findCategoriaProdutoById(linhaCategoriaDTO.getCategoriaId()));

        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);

        return linhaCategoriaDTO.of(linhaCategoria);
    }

    private void validate(LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Validando linha da categoria");

        if (linhaCategoriaDTO == null) {
            throw new IllegalArgumentException("LinhaCategoriaDTO não deve ser nula");
        }

        if (StringUtils.isEmpty(linhaCategoriaDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(linhaCategoriaDTO.getCodigo())) {
            throw new IllegalArgumentException("Código não deve ser nula/vazia");
        }
    }


    public String gerarCodigoLinhaCategoria(String codigoDoUsuario) {

        String codigoGerado = String.format("%10s", codigoDoUsuario).toUpperCase();
        codigoGerado = codigoGerado.replace(' ','0');

        return codigoGerado;
    }

    public List<LinhaCategoria> listarLinhaCategoria() {
        List<LinhaCategoria> linhaCategoria = this.iLinhaCategoriaRepository.findAll();
        return linhaCategoria;
    }

    public LinhaCategoria findByCodigo(String codigo) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findByCodigo(codigo);

        if (linhaCategoriaOptional.isPresent()) {
            return linhaCategoriaOptional.get();
        }
        throw new IllegalArgumentException(String.format("Código %s não existe", codigo));
    }

    public Optional<LinhaCategoria> findByIdOptional(Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaOptional.isPresent()) {
            return linhaCategoriaOptional;
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public LinhaCategoriaDTO findById(Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaOptional.isPresent()) {
            return LinhaCategoriaDTO.of(linhaCategoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public LinhaCategoria findLinhaCategoriaById(Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaOptional.isPresent()) {
            return linhaCategoriaOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public LinhaCategoriaDTO update(LinhaCategoriaDTO linhaCategoriaDTO, Long id) {
        Optional<LinhaCategoria> linhaCategoriaExistenteOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaExistenteOptional.isPresent()) {

            LinhaCategoria linhaCategoriaExistente = linhaCategoriaExistenteOptional.get();

            LOGGER.info("Atualizando linha da categoria... id: [{}]", linhaCategoriaExistente.getId());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Linha da categoria Existente: {}", linhaCategoriaExistente);

            linhaCategoriaExistente.setNome(linhaCategoriaDTO.getNome());
            linhaCategoriaExistente.setCodigo(gerarCodigoLinhaCategoria(linhaCategoriaDTO.getCodigo()));
            linhaCategoriaExistente.setCategoriaId(categoriaProdutoService.findCategoriaProdutoById(linhaCategoriaDTO.getCategoriaId()));

            linhaCategoriaExistente = this.iLinhaCategoriaRepository.save(linhaCategoriaExistente);

            return LinhaCategoriaDTO.of(linhaCategoriaExistente);
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para linha da categoria de ID: [{}]", id);

        this.iLinhaCategoriaRepository.deleteById(id);
    }

    public void exportCSV(HttpServletResponse response) throws IOException {
        String linhaCategoriaCSV = "linhaCategoria.csv";
        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", linhaCategoriaCSV);
        response.setHeader(headerKey, headerValue);

        PrintWriter printWriter = response.getWriter();

        String header = "Código da Linha;Linha da categoria;Código da categoria;Categoria";
        printWriter.println(header);

        for (LinhaCategoria linhaCategoriaCSVObjeto : listarLinhaCategoria()) {
            String linhaCategoriaCodigo = linhaCategoriaCSVObjeto.getCodigo();
            String linhaCategoriaNome = linhaCategoriaCSVObjeto.getNome();
            String categoriaProdutoCodigo = linhaCategoriaCSVObjeto.getCategoriaId().getCodigo();
            String categoriaProdutoNome = linhaCategoriaCSVObjeto.getCategoriaId().getNome();

            printWriter.println(linhaCategoriaCodigo + ";" + linhaCategoriaNome + ";" + categoriaProdutoCodigo + ";" + categoriaProdutoNome);
        }
        printWriter.close();
    }

    public void importCSV(MultipartFile importLinhaCategoria) {
        String linhaDoArquivo = "";
        String quebraDeLinha = ";";

        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importLinhaCategoria.getInputStream()))) {

            linhaDoArquivo = leitor.readLine();
            while ((linhaDoArquivo = leitor.readLine()) != null) {
                String[] linhaCategoriaCSV = linhaDoArquivo.split(quebraDeLinha);
                Optional<CategoriaProduto> categoriaProdutoOptional = Optional.ofNullable(categoriaProdutoService.findByCodigo(linhaCategoriaCSV[2]));
                Optional<LinhaCategoria> linhaCategoriaExisteOptional = this.iLinhaCategoriaRepository.findByCodigo(linhaCategoriaCSV[0]);


                    if (!(linhaCategoriaExisteOptional.isPresent()) && categoriaProdutoOptional.isPresent()) {
                        LinhaCategoria linhaCategoria = new LinhaCategoria();
                        linhaCategoria.setCodigo(linhaCategoriaCSV[0]);
                        linhaCategoria.setNome(linhaCategoriaCSV[1]);

                        CategoriaProduto categoriaProduto = categoriaProdutoService.findByCodigo(linhaCategoriaCSV[2]);
                        linhaCategoria.setCategoriaId(categoriaProduto);

                        this.iLinhaCategoriaRepository.save(linhaCategoria);
                    }
                }

            } catch(IOException e){
                e.printStackTrace();
            }

    }
}