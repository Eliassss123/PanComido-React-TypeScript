package com.pancomido.pedido.controllerPedido;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.pancomido.pancomido.pedido.assemblerPedido.PedidoModelAssembler;
import com.pancomido.pancomido.pedido.modelPedido.Pedido;
import com.pancomido.pancomido.pedido.repositoryPedido.PedidoRepository;
import com.pancomido.pancomido.pedido.servicePedido.PedidoService;

import com.pancomido.pancomido.tienda.modelTienda.Tienda;
import com.pancomido.pancomido.tienda.repositoryTienda.TiendaRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    @Autowired
    private PedidoModelAssembler pedidoAssembler;

    @Operation(summary = "Crear un nuevo pedido", description = "Crea un pedido con cliente, tienda y lista de productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Productos o entidades no encontradas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody PedidoDTO dto) {
        try {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            Tienda tienda = tiendaRepository.findById(dto.getTiendaId())
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));

            List<Producto> productos = productoRepository.findAllById(dto.getProductosIds());

            if (productos.size() != dto.getProductosIds().size()) {
                List<Long> idsExistentes = productos.stream()
                    .map(Producto::getId).collect(Collectors.toList());
                List<Long> idsSolicitados = dto.getProductosIds().stream()
                    .map(Integer::longValue).collect(Collectors.toList());
                List<Long> idsFaltantes = idsSolicitados.stream()
                    .filter(id -> !idsExistentes.contains(id)).toList();
                return ResponseEntity.status(404).body("Productos no encontrados: " + idsFaltantes);
            }

            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setTienda(tienda);
            pedido.setProductos(productos);
            pedido.setEstado("PENDIENTE");

            Pedido guardado = pedidoRepository.save(pedido);
            return ResponseEntity.ok(pedidoAssembler.toModel(guardado));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PedidoDTO {
        private Integer clienteId;
        private Integer tiendaId;
        private List<Integer> productosIds;
    }

    @Operation(summary = "Listar todos los pedidos", description = "Devuelve una lista de todos los pedidos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos listados correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @Operation(summary = "Actualizar estado del pedido", description = "Permite cambiar el estado de un pedido existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/estado")
    public Pedido actualizarEstado(@PathVariable Integer id, @RequestBody String nuevoEstado) {
        return pedidoService.actualizarEstado(id, nuevoEstado);
    }

    @Operation(summary = "Obtener pedido por ID", description = "Busca un pedido específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public Pedido obtenerPorId(@PathVariable Integer id) {
        return pedidoService.obtenerPedidoPorId(id);
    }
}
