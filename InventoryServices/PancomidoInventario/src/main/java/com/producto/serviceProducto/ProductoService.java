package com.producto.serviceProducto;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;              // 👈 IMPORTANTE

import jakarta.persistence.EntityNotFoundException;   // 👈 IMPORTANTE

import com.producto.modelProducto.Producto;
import com.producto.repositoryProducto.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Lista todos los productos
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    // Búsqueda por nombre (parcial), o todos si viene null/vacío
    public List<Producto> buscarProductos(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return productoRepository.findAll();
        }
        return productoRepository.findByNombreContaining(nombre);
    }

    // Crear producto
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // Obtener producto por ID
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Actualizar producto
    public Optional<Producto> actualizarProducto(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(productoExistente -> {
                    productoExistente.setNombre(productoActualizado.getNombre());
                    productoExistente.setPrecio(productoActualizado.getPrecio());
                    productoExistente.setAvatarUrl(productoActualizado.getAvatarUrl());
                    productoExistente.setCategoria(productoActualizado.getCategoria());
                    productoExistente.setStockProducto(productoActualizado.getStockProducto());
                    return productoRepository.save(productoExistente);
                });
    }

    // Productos con stock disponible
    public List<Producto> buscarProductosDisponibles() {
        return productoRepository.findByStockProductoGreaterThan(0);
    }

    // Obtener productos para un pedido a partir de una lista de IDs
    public List<Producto> obtenerProductosParaPedido(List<Long> productosIds) {
        List<Producto> productos = productoRepository.findAllById(productosIds);

        // Validar que existan todos los IDs
        if (productos.size() != productosIds.size()) {
            List<Long> idsExistentes = productos.stream()
                    .map(Producto::getId)
                    .collect(Collectors.toList());

            List<Long> idsFaltantes = productosIds.stream()
                    .filter(id -> !idsExistentes.contains(id))
                    .collect(Collectors.toList());

            throw new EntityNotFoundException("Productos no encontrados: " + idsFaltantes);
        }

        // Validar stock
        productos.forEach(p -> {
            if (p.getStockProducto() == null || p.getStockProducto() <= 0) {
                throw new RuntimeException("Producto " + p.getNombre() + " sin stock disponible");
            }
        });

        return productos;
    }

    // Descontar 1 unidad de stock por producto (ej: al confirmar pedido)
    public void actualizarStockProductos(List<Producto> productos) {
        productos.forEach(p -> {
            if (p.getStockProducto() == null || p.getStockProducto() <= 0) {
                throw new RuntimeException("No se puede descontar stock de " + p.getNombre() + " porque es 0");
            }
            p.setStockProducto(p.getStockProducto() - 1);
            productoRepository.save(p);
        });
    }
}
