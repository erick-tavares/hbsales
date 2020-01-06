package br.com.hbsis.pedido;

import br.com.hbsis.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long>{

//    @Query(value = "SELECT * FROM pedido WHERE pedido.fornecedor_id = ?1"){
//        List<Pedido> findB
//
//    }
}