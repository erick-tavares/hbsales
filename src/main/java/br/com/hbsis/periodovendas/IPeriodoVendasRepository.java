package br.com.hbsis.periodovendas;


import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface IPeriodoVendasRepository extends JpaRepository<PeriodoVendas, Long> {
    List<PeriodoVendas> findByFornecedorId(Fornecedor idFornecedor);
    List<PeriodoVendas> findByFornecedorId_Id(Long id);

}
