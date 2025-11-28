package com.pancomido.pedido.modelPedido;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private Integer precioUnitario; // precio al momento de la compra
}
