package com.reporte.serviceReporte;

import com.reporte.modelReporte.Reporte;
import com.reporte.repositoryReporte.ReporteRepository;
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
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    void generarYGuardarReporte_deberiaCrearReporteConFechaYTotalesEnCero() {
        // Arrange: el save devuelve el mismo objeto que recibe
        when(reporteRepository.save(any(Reporte.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, Reporte.class));

        // Act
        Reporte resultado = reporteService.generarYGuardarReporte();

        // Assert
        assertNotNull(resultado, "El reporte generado no debe ser nulo");
        assertNotNull(resultado.getFechaGeneracion(), "La fecha de generación no debe ser nula");

        // Por ahora el servicio usa valores 0 como ejemplo
        assertEquals(0, resultado.getTotalClientes());
        assertEquals(0, resultado.getTotalTiendas());
        assertEquals(0, resultado.getTotalProductos());
        // totalVendedores queda 0 por defecto

        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    void obtenerReportePorId_deberiaRetornarOptionalConReporte() {
        Reporte reporte = new Reporte();
        reporte.setId(1);

        when(reporteRepository.findById(1)).thenReturn(Optional.of(reporte));

        Optional<Reporte> resultado = reporteService.obtenerReportePorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(reporte, resultado.get());
        verify(reporteRepository).findById(1);
    }

    @Test
    void eliminarReporte_deberiaLlamarDeleteById() {
        reporteService.eliminarReporte(5);

        verify(reporteRepository).deleteById(5);
    }

    @Test
    void listarTodosLosReportes_deberiaDelegarEnRepositorio() {
        List<Reporte> lista = List.of(new Reporte(), new Reporte());
        when(reporteRepository.findAll()).thenReturn(lista);

        List<Reporte> resultado = reporteService.listarTodosLosReportes();

        assertEquals(lista, resultado);
        verify(reporteRepository).findAll();
    }
}
