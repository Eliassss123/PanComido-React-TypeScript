package com.pancomido.pedido.servicePedido;

import com.pancomido.pedido.modelPedido.ItemPedido;
import com.pancomido.pedido.modelPedido.Pedido;
import com.pancomido.pedido.repositoryPedido.PedidoRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    // DTO que recibimos desde el front
    @Data
    public static class ItemCarritoDTO {
        private Long productoId;
        private String nombreProducto;
        private Integer cantidad;
        private Integer precioUnitario;
    }

    @Data
    public static class PedidoFrontDTO {
        private String correoUsuario;
        private List<ItemCarritoDTO> items;
    }

    // Crea el pedido a partir del carrito del front
    public Pedido crearDesdeCarrito(PedidoFrontDTO dto) {
        int total = dto.getItems().stream()
                .mapToInt(i -> i.getPrecioUnitario() * i.getCantidad())
                .sum();

        Pedido pedido = Pedido.builder()
                .correoUsuario(dto.getCorreoUsuario())
                .fechaCreacion(LocalDateTime.now())
                .estado("CARRITO")
                .total(total)
                .items(
                        dto.getItems().stream()
                                .map(i -> new ItemPedido(
                                        i.getProductoId(),
                                        i.getNombreProducto(),
                                        i.getCantidad(),
                                        i.getPrecioUnitario()
                                ))
                                .toList()
                )
                .build();

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidosDeUsuario(String correoUsuario) {
        return pedidoRepository.findByCorreoUsuarioOrderByFechaCreacionDesc(correoUsuario);
    }

    public Pedido cambiarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}
