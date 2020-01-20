package br.com.hbsis.pedidoitem;

import br.com.hbsis.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
interface IItemPedidoRepository extends JpaRepository<ItemPedido, Long>{
    List<ItemPedido> findByPedidoId(Pedido pedido);
}