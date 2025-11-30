package com.pancomido.boleta.controllerBoleta;

import com.pancomido.boleta.modelBoleta.Boleta;
import com.pancomido.boleta.serviceBoleta.BoletaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;


import jakarta.servlet.ServletException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoletaController.class)
class BoletaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoletaService boletaService;

    @Test
    void obtenerResumen_deberiaRetornarBoleta() throws Exception {
        Boleta boleta = new Boleta();
        boleta.setPedidoId(10L);
        boleta.setCorreoUsuario("cliente@test.cl");
        boleta.setFecha(LocalDateTime.now());
        boleta.setTotal(2500);
        boleta.setCantidadTotal(3);

        when(boletaService.generarResumen(10L)).thenReturn(boleta);

        mockMvc.perform(get("/api/boletas/resumen/{pedidoId}", 10L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidoId").value(10L))
                .andExpect(jsonPath("$.correoUsuario").value("cliente@test.cl"))
                .andExpect(jsonPath("$.total").value(2500))
                .andExpect(jsonPath("$.cantidadTotal").value(3));

        verify(boletaService).generarResumen(10L);
    }

        @Test
        void obtenerResumen_deberiaPropagarExcepcionCuandoServicioLanzaExcepcion() throws Exception {
            // Arrange
            when(boletaService.generarResumen(10L))
                    .thenThrow(new RuntimeException("No existe pedido"));

            // Act + Assert
            ServletException ex = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(
                            get("/api/boletas/resumen/{pedidoId}", 10L)
                    )
            );

            assertNotNull(ex.getCause());
            assertEquals("No existe pedido", ex.getCause().getMessage());

            verify(boletaService).generarResumen(10L);
        }

}
