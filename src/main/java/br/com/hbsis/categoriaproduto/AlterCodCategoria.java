package br.com.hbsis.categoriaproduto;

import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlterCodCategoria {
    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;

    public AlterCodCategoria (ICategoriaProdutoRepository iCategoriaProdutoRepository) {
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
    }

    public void alterarCodCategoria (Fornecedor fornecedor) {
        List<CategoriaProduto> listaCategoriaProduto = iCategoriaProdutoRepository.findAllByFornecedorId_Id(fornecedor.getId());

        if (listaCategoriaProduto != null) {
            for (CategoriaProduto cat : listaCategoriaProduto) {
                cat.setCodigo(cat.getCodigo().substring(7, 10));
                cat.setFornecedorId(fornecedor);

               this.iCategoriaProdutoRepository.save(cat);
            }
        }
    }
}
