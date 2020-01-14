package br.com.hbsis.produto;

import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface IProdutoRepository extends JpaRepository <Produto, Long> {
    Optional<Produto> findByCodigo(String codigo);

  //  List<Produto> findByFornedecodrId(Fornecedor fornecedorId);
}