package com.inventario.dto;

import lombok.Data;

@Data
public class InventarioDTO {
    private Long productoId; // ID del producto
    private Integer cantidad; // cantidad a agregar
}
