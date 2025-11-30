package com.pancomido.boleta.serviceBoleta;

import com.pancomido.boleta.modelBoleta.Boleta;
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
class BoletaServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private BoletaService boletaService;

    @Test
    void generarResumen_deberiaConstruirBoletaConDatosDelPedido() {
        // Arrange
        Pedido pedido = new Pedido();
        pedido.setId(5L);
        pedido.setCorreoUsuario("cliente@test.cl");
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setTotal(3000);

        ItemPedido item1 = new ItemPedido(1L, "Marraqueta", 2, 500);
        ItemPedido item2 = new ItemPedido(2L, "Hallulla", 4, 500);
        pedido.setItems(List.of(item1, item2));

        when(pedidoRepository.findById(5L)).thenReturn(Optional.of(pedido));

        // Act
        Boleta boleta = boletaService.generarResumen(5L);

        // Assert
        assertNotNull(boleta);
        assertEquals(5L, boleta.getPedidoId());
        assertEquals("cliente@test.cl", boleta.getCorreoUsuario());
        assertEquals(pedido.getFechaCreacion(), boleta.getFecha());
        assertEquals(3000, boleta.getTotal());
        assertEquals(2 + 4, boleta.getCantidadTotal());
        assertEquals(pedido.getItems(), boleta.getItems());

        verify(pedidoRepository).findById(5L);
    }

    @Test
    void generarResumen_deberiaLanzarExcepcionSiPedidoNoExiste() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> boletaService.generarResumen(99L));

        assertTrue(ex.getMessage().contains("Pedido no encontrado"));
        verify(pedidoRepository).findById(99L);
    }
}
