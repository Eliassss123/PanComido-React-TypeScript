package com.pancomido.pancomido.controllerReporte;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.pancomido.pancomido.reporte.assemblerReporte.ReporteModelAssembler;
import com.pancomido.pancomido.reporte.controllerReporte.ReporteController;
import com.pancomido.pancomido.reporte.modelReporte.Reporte;
import com.pancomido.pancomido.reporte.serviceReporte.ReporteService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ReporteControllerTest {

    @InjectMocks
    private ReporteController reporteController;

    @Mock
    private ReporteService reporteService;

    @Mock
    private ReporteModelAssembler reporteAssembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testGenerarReporte() {
        Reporte reporte = new Reporte();
        reporte.setId(1);

        when(reporteService.generarYGuardarReporte()).thenReturn(reporte);
        when(reporteAssembler.toModel(reporte)).thenReturn(EntityModel.of(reporte));

        ResponseEntity<EntityModel<Reporte>> response = reporteController.generarReporte();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent().getId()).isEqualTo(1);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testObtenerReportePorId_Encontrado() {
        Reporte reporte = new Reporte();
        reporte.setId(2);

        when(reporteService.obtenerReportePorId(2)).thenReturn(Optional.of(reporte));
        when(reporteAssembler.toModel(reporte)).thenReturn(EntityModel.of(reporte));

        ResponseEntity<EntityModel<Reporte>> response = reporteController.obtenerReportePorId(2);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent().getId()).isEqualTo(2);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testObtenerReportePorId_NoEncontrado() {
        when(reporteService.obtenerReportePorId(99)).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<Reporte>> response = reporteController.obtenerReportePorId(99);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testEliminarReporte_Exitoso() {
        ResponseEntity<?> response = reporteController.eliminarReporte(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(reporteService).eliminarReporte(1L);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testEliminarReporte_NoEncontrado() {
        doThrow(new RuntimeException()).when(reporteService).eliminarReporte(9L);

        ResponseEntity<?> response = reporteController.eliminarReporte(9L);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testListarTodosLosReportes() {
        Reporte r1 = new Reporte(); r1.setId(1);
        Reporte r2 = new Reporte(); r2.setId(2);

        when(reporteService.listarTodosLosReportes()).thenReturn(List.of(r1, r2));
        when(reporteAssembler.toModel(r1)).thenReturn(EntityModel.of(r1));
        when(reporteAssembler.toModel(r2)).thenReturn(EntityModel.of(r2));

        ResponseEntity<List<EntityModel<Reporte>>> response = reporteController.listarTodosLosReportes();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(2);
    }
}
