package com.pancomido.envio.controllerEnvio;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pancomido.pancomido.envio.assemblerEnvio.EnvioModelAssembler;
import com.pancomido.pancomido.envio.modelEnvio.Envio;
import com.pancomido.pancomido.envio.serviceEnvio.EnvioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
@CrossOrigin
public class EnvioController {

    private final EnvioService envioService;
    private final EnvioModelAssembler envioAssembler;

    public EnvioController(EnvioService envioService, EnvioModelAssembler envioAssembler) {
        this.envioService = envioService;
        this.envioAssembler = envioAssembler;
    }

    @Operation(summary = "Crear nuevo envío", description = "Crea un nuevo registro de envío")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Envio>> crearEnvio(@RequestBody Envio envio) {
        Envio creado = envioService.crearEnvio(envio);
        return ResponseEntity.ok(envioAssembler.toModel(creado));
    }

    @Operation(summary = "Eliminar envío", description = "Elimina un envío existente por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarEnvio(@PathVariable Integer id) {
        try {
            envioService.eliminarEnvio(id);
            return ResponseEntity.ok("Envio eliminado con id: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar estado del envío", description = "Actualiza el estado de un envío por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/estado")
    public Envio actualizarEstado(@PathVariable Integer id, @RequestBody String nuevoEstado) {
        return envioService.actualizarEstado(id, nuevoEstado);
    }

    @Operation(summary = "Listar todos los envíos", description = "Retorna la lista de todos los envíos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envíos listados correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<Envio> listarEnvios() {
        return envioService.obtenerTodos();
    }
}
