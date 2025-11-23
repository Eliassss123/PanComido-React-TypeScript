package com.pancomido.boleta.repositoryBoleta;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancomido.pancomido.boleta.modelBoleta.Boleta;
import com.pancomido.pancomido.pedido.modelPedido.Pedido;

import java.util.List;

public interface BoletaRepository extends JpaRepository<Boleta, Integer> {
    List<Boleta> findByPedido(Pedido pedido);
}





