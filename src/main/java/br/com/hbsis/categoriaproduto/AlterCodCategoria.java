package br.com.hbsis.categoriaproduto;

import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AlterCodCategoria {
    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final FornecedorService fornecedorService;

    public AlterCodCategoria(ICategoriaProdutoRepository iCategoriaProdutoRepository, FornecedorService fornecedorService) {
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
        this.fornecedorService = fornecedorService;
    }

    public String gerarCodigoCategoria(Long idFornecedor, String codigoDoUsuario) {

        String codigoCategoria = "";

        String fornecedorCnpj = "";
        FornecedorDTO fornecedorDTO = fornecedorService.findById(idFornecedor);
        fornecedorCnpj = fornecedorDTO.getCnpj().substring(10, 14);

        String codigoGerado = "";
        codigoGerado =  StringUtils.leftPad(codigoDoUsuario.toUpperCase(),3,"0");

        codigoCategoria = "CAT" + fornecedorCnpj + codigoGerado;

        return codigoCategoria;
    }

    public CategoriaProdutoDTO update(CategoriaProdutoDTO categoriaProdutoDTO, Long id) {
        Optional<CategoriaProduto> categoriProdutoExistenteOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriProdutoExistenteOptional.isPresent()) {

            CategoriaProduto categoriaProdutoExistente = categoriProdutoExistenteOptional.get();

            categoriaProdutoExistente.setNome(categoriaProdutoDTO.getNome());
            categoriaProdutoExistente.setCodigo(gerarCodigoCategoria(categoriaProdutoDTO.getFornecedorId(), categoriaProdutoDTO.getCodigo()));
            categoriaProdutoExistente.setFornecedorId(fornecedorService.findFornecedorById(categoriaProdutoDTO.getFornecedorId()));

            categoriaProdutoExistente = this.iCategoriaProdutoRepository.save(categoriaProdutoExistente);

            return CategoriaProdutoDTO.of(categoriaProdutoExistente);
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
}
