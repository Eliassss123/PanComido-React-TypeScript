package com.producto.controllerProducto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.producto.ProductoDTO.productoDTO;
import com.producto.ProductoDTO.ProductoFrontDTO;
import com.producto.assemblerProducto.ProductoModelAssembler;
import com.producto.modelProducto.Producto;
import com.producto.serviceProducto.ProductoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler productoAssembler;

    // =================== CRUD básicos ===================

    @GetMapping
    public List<Producto> listarProductos(@RequestParam(required = false) String nombre) {
        return productoService.buscarProductos(nombre);
    }

    @GetMapping("/disponibles")
    public List<Producto> listarProductosDisponibles() {
        return productoService.buscarProductosDisponibles();
    }

    @PostMapping
    public EntityModel<Producto> crearProducto(@RequestBody Producto producto) {
        Producto creado = productoService.crearProducto(producto);
        return productoAssembler.toModel(creado);
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerProductoPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(productoAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id,
                                                       @RequestBody Producto productoActualizado) {
        return productoService.actualizarProducto(id, productoActualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =================== Endpoint para otros microservicios ===================

    @PostMapping("/listaPorIds")
    public List<productoDTO> obtenerProductosPorIds(@RequestBody List<Long> ids) {
        return productoService.obtenerProductosParaPedido(ids)
                .stream()
                .map(p -> new productoDTO(
                        p.getId(),
                        p.getNombre(),
                        // productoDTO espera un Double como tercer parámetro
                        p.getPrecio() != null ? p.getPrecio() : 0.0
                ))
                .collect(Collectors.toList());
    }

    // =================== Endpoint para Front (catálogo) ===================

    @GetMapping("/front/listar")
    public List<ProductoFrontDTO> listarProductosParaFront() {
        return productoService.listarProductos().stream()
                .map(p -> new ProductoFrontDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getAvatarUrl(),
                        // ProductoFrontDTO espera Integer como precio
                        p.getPrecio() != null ? p.getPrecio().intValue() : 0,
                        p.getCategoria() != null ? p.getCategoria() : "Blanco"
                ))
                .collect(Collectors.toList());
    }
}
