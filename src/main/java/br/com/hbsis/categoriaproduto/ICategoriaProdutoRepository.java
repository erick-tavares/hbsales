package br.com.hbsis.categoriaproduto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long>{
}



