package com.pancomido.pancomido.reporte.controllerReporte;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pancomido.pancomido.reporte.assemblerReporte.ReporteModelAssembler;
import com.pancomido.pancomido.reporte.modelReporte.Reporte;
import com.pancomido.pancomido.reporte.serviceReporte.ReporteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ReporteModelAssembler reporteAssembler;

    @Operation(summary = "Generar un nuevo reporte", description = "Genera y guarda un reporte en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/generar")
    public ResponseEntity<EntityModel<Reporte>> generarReporte() {
        try {
            Reporte reporte = reporteService.generarYGuardarReporte();
            return ResponseEntity.ok(reporteAssembler.toModel(reporte));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Obtener reporte por ID", description = "Obtiene un reporte específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Reporte>> obtenerReportePorId(@PathVariable Integer id) {
        return reporteService.obtenerReportePorId(id)
                .map(reporteAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar reporte", description = "Elimina un reporte del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReporte(@PathVariable Long id) {
        try {
            reporteService.eliminarReporte(id);
            return ResponseEntity.ok("Reporte eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar todos los reportes", description = "Obtiene la lista completa de reportes generados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reportes obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<EntityModel<Reporte>>> listarTodosLosReportes() {
        try {
            List<EntityModel<Reporte>> reportes = reporteService.listarTodosLosReportes().stream()
                .map(reporteAssembler::toModel)
                .collect(Collectors.toList());
            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}


