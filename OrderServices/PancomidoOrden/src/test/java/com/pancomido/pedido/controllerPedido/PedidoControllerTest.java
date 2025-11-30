package com.pancomido.pedido.controllerPedido;

import com.pancomido.pedido.modelPedido.ItemPedido;
import com.pancomido.pedido.modelPedido.Pedido;
import com.pancomido.pedido.servicePedido.PedidoService;
import com.pancomido.pedido.servicePedido.PedidoService.PedidoFrontDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.ServletException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Test
    void crearDesdeFront_deberiaRetornarPedidoCreado() throws Exception {
        Pedido pedidoRespuesta = new Pedido();
        pedidoRespuesta.setId(1L);
        pedidoRespuesta.setCorreoUsuario("cliente@test.cl");
        pedidoRespuesta.setFechaCreacion(LocalDateTime.now());
        pedidoRespuesta.setEstado("CARRITO");
        pedidoRespuesta.setTotal(1700);
        pedidoRespuesta.setItems(List.of(
                new ItemPedido(1L, "Marraqueta", 2, 500),
                new ItemPedido(2L, "Hallulla", 1, 700)
        ));

        when(pedidoService.crearDesdeCarrito(any(PedidoFrontDTO.class))).thenReturn(pedidoRespuesta);

        String jsonBody = """
                {
                  "correoUsuario": "cliente@test.cl",
                  "items": [
                    { "productoId": 1, "nombreProducto": "Marraqueta", "cantidad": 2, "precioUnitario": 500 },
                    { "productoId": 2, "nombreProducto": "Hallulla", "cantidad": 1, "precioUnitario": 700 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/pedidos/front")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.correoUsuario").value("cliente@test.cl"))
                .andExpect(jsonPath("$.total").value(1700))
                .andExpect(jsonPath("$.items.length()").value(2));

        verify(pedidoService).crearDesdeCarrito(any(PedidoFrontDTO.class));
    }

    @Test
    void listarPorUsuario_deberiaRetornarListaDePedidos() throws Exception {
        Pedido p1 = new Pedido();
        p1.setId(1L);
        p1.setCorreoUsuario("user@mail.cl");
        p1.setFechaCreacion(LocalDateTime.now());
        p1.setEstado("PAGADO");
        p1.setTotal(1000);

        Pedido p2 = new Pedido();
        p2.setId(2L);
        p2.setCorreoUsuario("user@mail.cl");
        p2.setFechaCreacion(LocalDateTime.now());
        p2.setEstado("ENVIADO");
        p2.setTotal(2000);

        when(pedidoService.listarPedidosDeUsuario("user@mail.cl")).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/pedidos/usuario/{correo}", "user@mail.cl")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].correoUsuario").value("user@mail.cl"))
                .andExpect(jsonPath("$[1].estado").value("ENVIADO"));

        verify(pedidoService).listarPedidosDeUsuario("user@mail.cl");
    }

    @Test
    void cambiarEstado_deberiaRetornarPedidoActualizado() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setId(5L);
        pedido.setCorreoUsuario("user@mail.cl");
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setEstado("PAGADO");
        pedido.setTotal(1500);

        when(pedidoService.cambiarEstado(5L, "PAGADO")).thenReturn(pedido);

        mockMvc.perform(patch("/api/pedidos/{id}/estado", 5L)
                        .param("estado", "PAGADO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.estado").value("PAGADO"));

        verify(pedidoService).cambiarEstado(5L, "PAGADO");
    }

                @Test
                void cambiarEstado_deberiaPropagarExcepcionSiServicioLanzaExcepcion() throws Exception {
                // Arrange
                when(pedidoService.cambiarEstado(5L, "PAGADO"))
                        .thenThrow(new RuntimeException("Error en cambio de estado"));

                // Act + Assert
                ServletException ex = assertThrows(
                        ServletException.class,
                        () -> mockMvc.perform(
                                patch("/api/pedidos/{id}/estado", 5L)
                                        .param("estado", "PAGADO")
                        )
                );

                // Verificamos que adentro viene nuestra RuntimeException
                assertNotNull(ex.getCause());
                assertEquals("Error en cambio de estado", ex.getCause().getMessage());

                verify(pedidoService).cambiarEstado(5L, "PAGADO");
                }

}
