package br.com.hbsis.periodovendas;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPeriodoVendasRepository extends JpaRepository<PeriodoVendas, Long> {

    Optional<PeriodoVendas> findFornecedorById(Long idFornecedor);

    List<PeriodoVendas> findAllFornecedorById(Long idFornecedor);

}
