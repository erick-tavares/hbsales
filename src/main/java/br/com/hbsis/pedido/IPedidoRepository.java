package br.com.hbsis.pedido;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.periodovendas.PeriodoVendas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long>{
    List<Pedido> findByPeriodoVendasId(PeriodoVendas periodoVendas);
    List<Pedido> findByFornecedorId(Fornecedor fornecedor);
    List<Pedido> findByFuncionarioId(Funcionario funcionario);
}
