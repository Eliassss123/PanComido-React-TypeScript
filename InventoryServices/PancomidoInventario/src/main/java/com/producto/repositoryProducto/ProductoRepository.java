package com.producto.repositoryProducto;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.producto.modelProducto.Producto;



public interface ProductoRepository extends JpaRepository<Producto, Long> {  
    List<Producto> findByNombreContaining(String nombre);
    List<Producto> findByStockProductoGreaterThan(int stock);
   
}