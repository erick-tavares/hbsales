package br.com.hbsis.pedidoitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
interface IItemPedidoRepository extends JpaRepository<ItemPedido, Long>{
}