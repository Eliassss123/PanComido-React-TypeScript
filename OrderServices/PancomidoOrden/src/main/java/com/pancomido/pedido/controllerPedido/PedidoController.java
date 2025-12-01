package com.pancomido.pedido.controllerPedido;

import com.pancomido.pedido.modelPedido.Pedido;
import com.pancomido.pedido.servicePedido.PedidoService;
import com.pancomido.pedido.servicePedido.PedidoService.PedidoFrontDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // 1) Crear un pedido desde el carrito del front
    @PostMapping("/front")
    public ResponseEntity<Pedido> crearDesdeFront(@RequestBody PedidoFrontDTO dto) {
        Pedido creado = pedidoService.crearDesdeCarrito(dto);
        return ResponseEntity.ok(creado);
    }

    // 2) Historial de pedidos por correo de usuario
    @GetMapping("/usuario/{correo}")
    public ResponseEntity<List<Pedido>> listarPorUsuario(@PathVariable String correo) {
        return ResponseEntity.ok(pedidoService.listarPedidosDeUsuario(correo));
    }

    // 3) Cambiar estado del pedido (para admin / envíos)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado
    ) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, estado));
    }
}
