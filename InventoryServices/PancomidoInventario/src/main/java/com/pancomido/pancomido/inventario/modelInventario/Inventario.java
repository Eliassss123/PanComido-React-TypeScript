package com.pancomido.pancomido.inventario.modelInventario;

import com.pancomido.pancomido.producto.modelProducto.Producto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto; // puede ser null

    @Column(nullable = false)
    private Integer cantidad;
}
