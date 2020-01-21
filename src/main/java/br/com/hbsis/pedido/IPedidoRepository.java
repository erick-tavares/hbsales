package br.com.hbsis.pedido;

import br.com.hbsis.pedidoitem.ItemPedido;
import br.com.hbsis.periodovendas.PeriodoVendas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long>{
    List<Pedido> findByPeriodoVendasId(PeriodoVendas periodoVendas);

    List<ItemPedido> findById(Pedido pedido);
}
