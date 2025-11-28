package com.pancomido.boleta.serviceBoleta;

import com.pancomido.boleta.modelBoleta.Boleta;
import com.pancomido.pedido.modelPedido.ItemPedido;
import com.pancomido.pedido.modelPedido.Pedido;
import com.pancomido.pedido.repositoryPedido.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoletaService {

    private final PedidoRepository pedidoRepository;

    /**
     * Genera un resumen de boleta a partir de un pedido ya almacenado en la BD.
     */
    public Boleta generarResumen(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + pedidoId));

        int cantidadTotal = pedido.getItems().stream()
                .mapToInt(ItemPedido::getCantidad)
                .sum();

        Boleta boleta = new Boleta();
        boleta.setPedidoId(pedido.getId());
        boleta.setCorreoUsuario(pedido.getCorreoUsuario());
        boleta.setFecha(pedido.getFechaCreacion());
        boleta.setTotal(pedido.getTotal());
        boleta.setCantidadTotal(cantidadTotal);
        boleta.setItems(pedido.getItems());

        return boleta;
    }
}
