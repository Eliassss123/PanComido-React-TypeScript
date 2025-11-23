package com.pancomido.pancomido.inventario.serviceInventario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pancomido.pancomido.inventario.modelInventario.Inventario;
import com.pancomido.pancomido.inventario.repositoryInventario.InventarioRepository;
import com.pancomido.pancomido.producto.modelProducto.Producto;
import com.pancomido.pancomido.producto.repositoryProducto.ProductoRepository;
import com.pancomido.pancomido.inventario.dto.InventarioDTO;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;





// Nuevo método que usa DTO para agregar cantidad
            public Inventario agregarInventario(InventarioDTO dto) {
                Producto producto = productoRepository.findById(dto.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                // Actualizamos stock del producto
                producto.setStockProducto(producto.getStockProducto() + dto.getCantidad());
                productoRepository.save(producto);

                // Creamos el inventario
                Inventario inventario = new Inventario();
                inventario.setProducto(producto);
                inventario.setCantidad(dto.getCantidad());
                return inventarioRepository.save(inventario);
            }

            public List<Inventario> obtenerInventario() {
                return inventarioRepository.findAll();
            }

            public Optional<Inventario> buscarPorId(Integer id) {
                return inventarioRepository.findById(id);
            }

            public void eliminarPorId(Integer id) {
                inventarioRepository.deleteById(id);
            }





//Eliminarcantidad hacia producto

        public Inventario eliminarCantidad(InventarioDTO dto) {
        // Buscar el inventario correspondiente al producto
        Inventario inventario = inventarioRepository.findAll().stream()
            .filter(i -> i.getProducto().getId().equals(dto.getProductoId()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Inventario no encontrado para el producto"));

        if (dto.getCantidad() <= 0) {
            throw new RuntimeException("Cantidad inválida");
        }

        if (inventario.getCantidad() < dto.getCantidad()) {
            throw new RuntimeException("Cantidad insuficiente en inventario");
        }

        // Reducir cantidad en inventario
        inventario.setCantidad(inventario.getCantidad() - dto.getCantidad());

        // Actualizar stock del producto
        Producto producto = inventario.getProducto();
        if (producto.getStockProducto() < dto.getCantidad()) {
            throw new RuntimeException("Stock insuficiente en producto");
        }
        producto.setStockProducto(producto.getStockProducto() - dto.getCantidad());
        productoRepository.save(producto);

        return inventarioRepository.save(inventario);
    }
}
