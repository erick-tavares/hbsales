package br.com.hbsis.categoriaProduto;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

    @Service
    public class CategoriaProdutoService {

        private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

        private final ICategoriaProdutoRepository iCategoriaProdutoRepository;

        public CategoriaProdutoService(ICategoriaProdutoRepository iCategoriaProdutoRepository) {
            this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
        }

        public CategoriaProdutoDTO save(CategoriaProdutoDTO categoriaProdutoDTO) {

            this.validate(categoriaProdutoDTO);

            LOGGER.info("Salvando categoria");
            LOGGER.debug("Categoria: {}", categoriaProdutoDTO);

            CategoriaProduto categoriaProduto = new CategoriaProduto();
            categoriaProduto.setNome(categoriaProdutoDTO.getNome());
            categoriaProduto.setFornecedor(categoriaProdutoDTO.getFornecedor());
            categoriaProduto.setCodigo(categoriaProdutoDTO.getCodigo());

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

            if (StringUtils.isEmpty(categoriaProdutoDTO.getFornecedor())) {
                throw new IllegalArgumentException("Fornecedor não deve ser nulo/vazio");
            }
        }

        public CategoriaProdutoDTO findById(Long id) {
            Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findById(id);

            if (categoriaProdutoOptional.isPresent()) {
                return CategoriaProdutoDTO.of(categoriaProdutoOptional.get());
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
                categoriaProdutoExistente.setFornecedor(categoriaProdutoDTO.getFornecedor());
                categoriaProdutoExistente.setCodigo(categoriaProdutoDTO.getCodigo());

                categoriaProdutoExistente = this.iCategoriaProdutoRepository.save(categoriaProdutoExistente);

                return CategoriaProdutoDTO.of(categoriaProdutoExistente);
            }

            throw new IllegalArgumentException(String.format("ID %s não existe", id));
        }

        public void delete(Long id) {
            LOGGER.info("Executando delete para categoria de ID: [{}]", id);

            this.iCategoriaProdutoRepository.deleteById(id);
        }
    }
