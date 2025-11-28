package com.pancomido.pedido.modelPedido;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // correo del usuario que hizo el pedido (viene desde auth)
    @Column(nullable = false)
    private String correoUsuario;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private String estado; // CARRITO, PAGADO, ENVIADO, CANCELADO, etc.

    @Column(nullable = false)
    private Integer total; // total del pedido en pesos

    // Ítems del carrito/pedido (embebidos, sin depender de Producto @Entity)
    @ElementCollection
    @CollectionTable(
            name = "pedido_items",
            joinColumns = @JoinColumn(name = "pedido_id")
    )
    private List<ItemPedido> items = new ArrayList<>();
}
