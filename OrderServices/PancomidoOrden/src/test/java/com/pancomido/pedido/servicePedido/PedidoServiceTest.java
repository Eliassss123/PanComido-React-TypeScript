package com.pancomido.pedido.servicePedido;

import com.pancomido.pedido.modelPedido.ItemPedido;
import com.pancomido.pedido.modelPedido.Pedido;
import com.pancomido.pedido.repositoryPedido.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void crearDesdeCarrito_deberiaCalcularTotalMapearItemsYGuardar() {
        // Arrange
        PedidoService.ItemCarritoDTO item1 = new PedidoService.ItemCarritoDTO();
        item1.setProductoId(1L);
        item1.setNombreProducto("Marraqueta");
        item1.setCantidad(2);
        item1.setPrecioUnitario(500);

        PedidoService.ItemCarritoDTO item2 = new PedidoService.ItemCarritoDTO();
        item2.setProductoId(2L);
        item2.setNombreProducto("Hallulla");
        item2.setCantidad(1);
        item2.setPrecioUnitario(700);

        PedidoService.PedidoFrontDTO dto = new PedidoService.PedidoFrontDTO();
        dto.setCorreoUsuario("cliente@test.cl");
        dto.setItems(List.of(item1, item2));

        // El repositorio devuelve el mismo pedido que recibe
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pedido resultado = pedidoService.crearDesdeCarrito(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("cliente@test.cl", resultado.getCorreoUsuario());
        assertEquals("CARRITO", resultado.getEstado());
        assertEquals(2 * 500 + 1 * 700, resultado.getTotal());

        assertNotNull(resultado.getItems());
        assertEquals(2, resultado.getItems().size());

        ItemPedido primerItem = resultado.getItems().get(0);
        assertEquals(1L, primerItem.getProductoId());
        assertEquals("Marraqueta", primerItem.getNombreProducto());
        assertEquals(2, primerItem.getCantidad());
        assertEquals(500, primerItem.getPrecioUnitario());

        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void listarPedidosDeUsuario_deberiaDelegarEnRepositorio() {
        List<Pedido> pedidos = List.of(new Pedido(), new Pedido());
        when(pedidoRepository.findByCorreoUsuarioOrderByFechaCreacionDesc("user@mail.cl"))
                .thenReturn(pedidos);

        List<Pedido> resultado = pedidoService.listarPedidosDeUsuario("user@mail.cl");

        assertEquals(pedidos, resultado);
        verify(pedidoRepository).findByCorreoUsuarioOrderByFechaCreacionDesc("user@mail.cl");
    }

    @Test
    void cambiarEstado_deberiaActualizarEstadoYGuardar() {
        Pedido pedido = new Pedido();
        pedido.setId(10L);
        pedido.setEstado("CARRITO");

        when(pedidoRepository.findById(10L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado = pedidoService.cambiarEstado(10L, "PAGADO");

        assertEquals("PAGADO", resultado.getEstado());
        verify(pedidoRepository).findById(10L);
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void cambiarEstado_deberiaLanzarExcepcionCuandoNoExistePedido() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pedidoService.cambiarEstado(99L, "PAGADO"));

        assertTrue(ex.getMessage().contains("Pedido no encontrado"));
        verify(pedidoRepository).findById(99L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }
}
