package com.reporte.controllerReporte;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reporte.modelReporte.Reporte;
import com.reporte.serviceReporte.ReporteService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Operation(summary = "Generar y guardar un nuevo reporte general")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error al generar el reporte")
    })
    @PostMapping("/generar")
    public ResponseEntity<Reporte> generarReporte() {
        try {
            Reporte reporte = reporteService.generarYGuardarReporte();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Endpoint específico para el frontend: genera y devuelve el reporte general
    @Operation(summary = "Generar y devolver un reporte general para el frontend")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte devuelto correctamente"),
            @ApiResponse(responseCode = "500", description = "Error al generar el reporte")
    })
    @GetMapping("/front/general")
    public ResponseEntity<Reporte> generarReporteFront() {
        try {
            Reporte reporte = reporteService.generarYGuardarReporte();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Obtener un reporte por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> obtenerPorId(@PathVariable Integer id) {
        Optional<Reporte> reporteOpt = reporteService.obtenerReportePorId(id);
        return reporteOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos los reportes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reportes devuelta correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Reporte>> listarTodos() {
        try {
            List<Reporte> reportes = reporteService.listarTodosLosReportes();
            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Eliminar un reporte por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reporte eliminado"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
            try {
                reporteService.eliminarReporte(id);
                return ResponseEntity.noContent().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }

}
