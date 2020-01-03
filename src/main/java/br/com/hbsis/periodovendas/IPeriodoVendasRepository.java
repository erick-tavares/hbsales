package br.com.hbsis.periodovendas;


import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface IPeriodoVendasRepository extends JpaRepository<PeriodoVendas, Long> {

    //Optional<PeriodoVendas> findFornecedorById(Long idFornecedor);

    List<PeriodoVendas> findByFornecedorId(Fornecedor idFornecedor);

}
