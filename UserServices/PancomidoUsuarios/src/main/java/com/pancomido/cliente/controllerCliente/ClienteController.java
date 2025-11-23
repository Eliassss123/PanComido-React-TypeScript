package com.pancomido.cliente.controllerCliente;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pancomido.cliente.modelCliente.Cliente;
import com.pancomido.cliente.serviceCliente.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;


    @Operation(summary = "Crear cliente", description = "Crea un nuevo cliente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })


        @PostMapping
        public ResponseEntity<?> crearCliente(@RequestBody Cliente cliente) {
            try {
                Cliente creado = clienteService.crearCliente(cliente);
                return ResponseEntity.ok(creado);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al crear cliente y usuario: " + e.getMessage());
            }
        }

    @Operation(summary = "Actualizar cliente", description = "Actualiza un cliente existente por RUN")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

        @PutMapping("/actualizar/{run}")
        public ResponseEntity<?> actualizarCliente(
                @PathVariable String run,
                @RequestBody Cliente clienteActualizado) {
            try {
                Cliente actualizado = clienteService.actualizarCliente(run, clienteActualizado);
                return ResponseEntity.ok(actualizado); // ya no usa assembler
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Cliente no encontrado con run " + run);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
            }
        }

    @Operation(summary = "Listar todos los clientes", description = "Obtiene todos los clientes registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clientes listados correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.obtenerClientes();
        return ResponseEntity.ok(clientes);
    }




    @Operation(summary = "Eliminar cliente por RUN", description = "Elimina un cliente utilizando su RUN")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/eliminar/{run}")
    public ResponseEntity<String> eliminarCliente(@PathVariable String run) {
        try {
            clienteService.eliminarClientePorRun(run);
            return ResponseEntity.ok("Cliente eliminado con run: " + run);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    

        @Operation(summary = "Obtener cliente por RUN", description = "Devuelve un cliente usando su RUN")
        @GetMapping("/{run}")
        public ResponseEntity<Cliente> obtenerClientePorRun(@PathVariable String run) {
            return clienteService.buscarPorRun(run)
                    .map(ResponseEntity::ok) // devuelve directamente el Cliente
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
}
