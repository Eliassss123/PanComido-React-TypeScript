package com.pancomido.pedido.repositoryPedido;

import com.pancomido.pedido.modelPedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Para historial por usuario (correo)
    List<Pedido> findByCorreoUsuarioOrderByFechaCreacionDesc(String correoUsuario);
}
