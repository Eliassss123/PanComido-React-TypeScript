package com.reporte.controllerreporte;

import com.reporte.controllerReporte.ReporteController;
import com.reporte.modelReporte.Reporte;
import com.reporte.serviceReporte.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReporteController.class)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @Test
    void generarReporte_deberiaRetornarOkConReporte() throws Exception {
        Reporte reporte = new Reporte(
                1,
                LocalDateTime.now(),
                10,   // totalClientes
                3,    // totalTiendas
                5,    // totalVendedores
                50    // totalProductos
        );

        when(reporteService.generarYGuardarReporte()).thenReturn(reporte);

        mockMvc.perform(post("/api/reportes/generar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalClientes").value(10))
                .andExpect(jsonPath("$.totalProductos").value(50));

        verify(reporteService).generarYGuardarReporte();
    }

    @Test
    void generarReporteFront_deberiaRetornarOkConReporte() throws Exception {
        Reporte reporte = new Reporte(
                2,
                LocalDateTime.now(),
                5,
                1,
                2,
                20
        );

        when(reporteService.generarYGuardarReporte()).thenReturn(reporte);

        mockMvc.perform(get("/api/reportes/front/general")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.totalClientes").value(5));

        verify(reporteService).generarYGuardarReporte();
    }

    @Test
    void obtenerPorId_deberiaRetornarOkCuandoExiste() throws Exception {
        Reporte reporte = new Reporte(
                3,
                LocalDateTime.now(),
                7,
                2,
                4,
                30
        );

        when(reporteService.obtenerReportePorId(3)).thenReturn(Optional.of(reporte));

        mockMvc.perform(get("/api/reportes/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.totalClientes").value(7));

        verify(reporteService).obtenerReportePorId(3);
    }

    @Test
    void obtenerPorId_deberiaRetornarNotFoundCuandoNoExiste() throws Exception {
        when(reporteService.obtenerReportePorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reportes/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(reporteService).obtenerReportePorId(99);
    }

    @Test
    void eliminar_deberiaRetornarNoContentCuandoNoFalla() throws Exception {
        doNothing().when(reporteService).eliminarReporte(1);

        mockMvc.perform(delete("/api/reportes/1"))
                .andExpect(status().isNoContent());

        verify(reporteService).eliminarReporte(1);
    }

    @Test
    void eliminar_deberiaRetornarErrorInternoCuandoServicioLanzaExcepcion() throws Exception {
        doThrow(new RuntimeException("Error al eliminar"))
                .when(reporteService).eliminarReporte(1);

        mockMvc.perform(delete("/api/reportes/1"))
                .andExpect(status().isInternalServerError());

        verify(reporteService).eliminarReporte(1);
    }
}
