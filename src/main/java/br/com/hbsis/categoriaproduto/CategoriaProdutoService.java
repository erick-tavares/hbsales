package br.com.hbsis.categoriaproduto;

import br.com.hbsis.exportimportcsv.ExportCSV;
import br.com.hbsis.exportimportcsv.ImportCSV;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
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
public class CategoriaProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final FornecedorService fornecedorService;
    private final ExportCSV exportCSV;
    private final ImportCSV importCSV;

    public CategoriaProdutoService(ICategoriaProdutoRepository iCategoriaProdutoRepository, FornecedorService fornecedorService, ExportCSV exportCSV, ImportCSV importCSV) {
        this.fornecedorService = fornecedorService;
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
        this.exportCSV = exportCSV;
        this.importCSV = importCSV;
    }

    public String gerarCodigoCategoria(Long idFornecedor, String codigoDoUsuario) {

        String codigoCategoria = "";
        String fornecedorCnpj = "";

        if (codigoDoUsuario.length() < 4) {
            FornecedorDTO fornecedorDTO = fornecedorService.findById(idFornecedor);
            fornecedorCnpj = fornecedorDTO.getCnpj().substring(10, 14);

            String codigoGerado = "";
            codigoGerado = StringUtils.leftPad(codigoDoUsuario.toUpperCase(), 3, "0");

            codigoCategoria = "CAT" + fornecedorCnpj + codigoGerado;
        } else {
            FornecedorDTO fornecedorDTO = fornecedorService.findById(idFornecedor);
            fornecedorCnpj = fornecedorDTO.getCnpj().substring(10, 14);

            String codigoGerado = codigoDoUsuario.substring(7, 10);
            codigoGerado = StringUtils.leftPad(codigoDoUsuario.toUpperCase(), 3, "0");

            codigoCategoria = "CAT" + fornecedorCnpj + codigoGerado.substring(7, 10);
        }

        return codigoCategoria;

    }

    public CategoriaProdutoDTO save(CategoriaProdutoDTO categoriaProdutoDTO) {

        this.validate(categoriaProdutoDTO);

        LOGGER.info("Salvando categoria");
        LOGGER.debug("Categoria: {}", categoriaProdutoDTO);

        CategoriaProduto categoriaProduto = new CategoriaProduto();
        categoriaProduto.setNome(categoriaProdutoDTO.getNome());
        categoriaProduto.setCodigo(gerarCodigoCategoria(categoriaProdutoDTO.getFornecedorId(), categoriaProdutoDTO.getCodigo()));
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
        if (StringUtils.isEmpty(categoriaProdutoDTO.getCodigo())) {
            throw new IllegalArgumentException("Código não deve ser nula/vazia");
        }
        if (categoriaProdutoDTO.getCodigo().length() <= 3 && (!(StringUtils.isNumeric(categoriaProdutoDTO.getCodigo())))) {
            throw new IllegalArgumentException("Código deve ser apenas números");
        }
        if (StringUtils.isEmpty(String.valueOf(categoriaProdutoDTO.getFornecedorId()))) {
            throw new IllegalArgumentException("FornecedorId não deve ser nulo/vazio");
        }
    }

    public void exportCSV(HttpServletResponse response) throws IOException, ParseException {
        String header = "Nome;Código;Razão social;CNPJ";
        exportCSV.exportarCSV(response, header);

        PrintWriter printWriter = response.getWriter();
        for (CategoriaProduto categoriaCSVObjeto : this.iCategoriaProdutoRepository.findAll()) {

            String categoriaNome = categoriaCSVObjeto.getNome();
            String categoriaCodigo = categoriaCSVObjeto.getCodigo();
            String razaoSocial = categoriaCSVObjeto.getFornecedorId().getRazaoSocial();
            String fornecedorCnpj = exportCSV.mask(categoriaCSVObjeto.getFornecedorId().getCnpj());

            printWriter.println(categoriaNome + ";" + categoriaCodigo + ";" + razaoSocial + ";" + fornecedorCnpj);
        }
        printWriter.close();
    }

    public CategoriaProduto findByCodigo(String codigo) {
        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findByCodigo(codigo);

        if (categoriaProdutoOptional.isPresent()) {
            return categoriaProdutoOptional.get();
        }
        throw new IllegalArgumentException(String.format("Código %s não existe", codigo));
    }

    public Optional<CategoriaProduto> findByCodigoOptional(String codigo) {
        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findByCodigo(codigo);

        return categoriaProdutoOptional;
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
            categoriaProdutoExistente.setCodigo(gerarCodigoCategoria(categoriaProdutoDTO.getFornecedorId(), categoriaProdutoDTO.getCodigo()));
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

    public void importCategoriaCSV(MultipartFile importCategoria) {

        List<List<String>> listaCSV = importCSV.importCSV(importCategoria);
        for (List<String> strings : listaCSV) {
            String nome = strings.get(0);
            String codigo = strings.get(1);
            String cnpj = strings.get(3).replaceAll("\\D", "");


            Optional<Fornecedor> fornecedorOptional = Optional.ofNullable(fornecedorService.findByCnpj(cnpj));
            Optional<CategoriaProduto> categoriaProdutoExiste = Optional.ofNullable(this.findByCodigo(codigo));

            if (categoriaProdutoExiste.isPresent() && fornecedorOptional.isPresent()) {

                CategoriaProduto categoriaProduto = categoriaProdutoExiste.get();
                categoriaProduto.setNome(nome);
                categoriaProduto.setCodigo(codigo);
                Fornecedor fornecedor = fornecedorService.findByCnpj(cnpj);
                categoriaProduto.setFornecedorId(fornecedor);

                this.update(CategoriaProdutoDTO.of(categoriaProduto), categoriaProdutoExiste.get().getId());
                LOGGER.info("Alterando categoria de produto... id: [{}]", categoriaProduto.getId());

            } else {
                CategoriaProduto categoriaProduto = new CategoriaProduto();
                categoriaProduto.setNome(nome);
                categoriaProduto.setCodigo(codigo);
                Fornecedor fornecedor = fornecedorService.findByCnpj(cnpj);
                categoriaProduto.setFornecedorId(fornecedor);

                this.save(CategoriaProdutoDTO.of(categoriaProduto));
                LOGGER.info("Importando categoria de produto... id: [{}]", categoriaProduto.getId());
            }
        }

    }

}
