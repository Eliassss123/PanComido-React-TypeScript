package com.inventario.controllerInventario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inventario.assemblerInventario.InventarioModelAssembler;
import com.inventario.modelInventario.Inventario;
import com.inventario.serviceInventario.InventarioService;
import com.inventario.dto.InventarioDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private InventarioModelAssembler inventarioAssembler;




    @Operation(summary = "Agregar producto al inventario", description = "Permite ingresar un nuevo producto al inventario y actualizar stock del producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto agregado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Inventario>> agregarInventario(@RequestBody InventarioDTO dto) {
        Inventario nuevo = inventarioService.agregarInventario(dto);
        return ResponseEntity.ok(inventarioAssembler.toModel(nuevo));
    }

    


    @Operation(summary = "Eliminar cantidad de inventario", description = "Permite disminuir la cantidad de un producto en inventario y actualizar stock del producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cantidad eliminada correctamente"),
        @ApiResponse(responseCode = "400", description = "Cantidad inválida o insuficiente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/eliminar-cantidad")
    public ResponseEntity<EntityModel<Inventario>> eliminarCantidad(@RequestBody InventarioDTO dto) {
        Inventario actualizado = inventarioService.eliminarCantidad(dto);
        return ResponseEntity.ok(inventarioAssembler.toModel(actualizado));
    }


    @GetMapping
    public List<Inventario> listarInventario() {
        return inventarioService.obtenerInventario();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> buscarPorId(@PathVariable Integer id) {
        return inventarioService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarPorId(@PathVariable Integer id) {
        try {
            inventarioService.eliminarPorId(id);
            return ResponseEntity.ok("Producto eliminado del inventario con id: " + id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
