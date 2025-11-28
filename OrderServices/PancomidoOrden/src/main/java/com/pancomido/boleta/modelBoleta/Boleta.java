package com.pancomido.boleta.modelBoleta;

import com.pancomido.pedido.modelPedido.ItemPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo sencillo de Boleta pensado como DTO para el front.
 * No está anotado como @Entity: se construye a partir de la tabla de pedidos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Boleta {

    private Long pedidoId;
    private String correoUsuario;
    private LocalDateTime fecha;
    private Integer total;
    private Integer cantidadTotal;

    // Opcional: detalle de ítems que componen el pedido
    private List<ItemPedido> items;
}
