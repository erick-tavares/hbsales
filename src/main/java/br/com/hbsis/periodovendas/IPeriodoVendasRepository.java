package br.com.hbsis.periodovendas;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface IPeriodoVendasRepository extends JpaRepository<PeriodoVendas, Long> {

    Optional<PeriodoVendas> findFornecedorById(Long idFornecedor);

    Optional<PeriodoVendas> findByData(LocalDate dataPeriodo);
}
