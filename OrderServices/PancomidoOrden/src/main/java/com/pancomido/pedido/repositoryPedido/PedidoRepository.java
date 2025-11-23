package com.pancomido.pedido.repositoryPedido;

import org.springframework.data.jpa.repository.JpaRepository;


import com.pancomido.pancomido.pedido.modelPedido.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}

