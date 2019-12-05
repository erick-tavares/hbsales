package br.com.hbsis.linhacategoria;

import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);

    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaProdutoService categoriaProdutoService;


    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaProdutoService categoriaProdutoService) {
        this.categoriaProdutoService = categoriaProdutoService;
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;

    }

    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO) {

        //Pegando o categoria produto completo do banco pelo ID categoria produto da tabela linha_categoria
        this.validate(linhaCategoriaDTO);

        LOGGER.info("Salvando linha de categoria");
        LOGGER.debug("Linha de Categoria: {}", linhaCategoriaDTO);

        LinhaCategoria linhaCategoria = new LinhaCategoria();
        linhaCategoria.setCodigo(linhaCategoriaDTO.getCodigo());
        linhaCategoria.setNome(linhaCategoriaDTO.getNome());

        linhaCategoria.setCategoriaId(categoriaProdutoService.findCategoriProdutoById(linhaCategoriaDTO.getCategoriaId()));

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
    }

    public List<LinhaCategoria> listarLinhaCategoria() {
        List<LinhaCategoria> linhaCategoria = this.iLinhaCategoriaRepository.findAll();
        return linhaCategoria;
    }

    public LinhaCategoriaDTO findById(Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaOptional.isPresent()) {
            return LinhaCategoriaDTO.of(linhaCategoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public LinhaCategoriaDTO update(LinhaCategoriaDTO linhaCategoriaDTO, Long id) {
        Optional<LinhaCategoria> linhaCategoriExistenteOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriExistenteOptional.isPresent()) {

            LinhaCategoria linhaCategoriaExistente = linhaCategoriExistenteOptional.get();

            LOGGER.info("Atualizando linha da categoria... id: [{}]", linhaCategoriaExistente.getId());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Linha da categoria Existente: {}", linhaCategoriaExistente);

            linhaCategoriaExistente.setNome(linhaCategoriaDTO.getNome());
            linhaCategoriaExistente.setCodigo(linhaCategoriaDTO.getCodigo());
            linhaCategoriaExistente.setCategoriaId(categoriaProdutoService.findCategoriProdutoById(linhaCategoriaDTO.getCategoriaId()));

            linhaCategoriaExistente = this.iLinhaCategoriaRepository.save(linhaCategoriaExistente);

            return LinhaCategoriaDTO.of(linhaCategoriaExistente);
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para linha da categoria de ID: [{}]", id);

        this.iLinhaCategoriaRepository.deleteById(id);
    }
}