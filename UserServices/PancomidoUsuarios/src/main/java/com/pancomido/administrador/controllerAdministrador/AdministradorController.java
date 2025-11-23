package com.pancomido.administrador.controllerAdministrador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pancomido.administrador.modelAdministrador.Administrador;
import com.pancomido.administrador.serviceAdministrador.AdminService;
import com.pancomido.cliente.serviceCliente.ClienteService;
import com.pancomido.cliente.modelCliente.Cliente;
import com.pancomido.auth.serviceAuth.UsuarioService;
import com.pancomido.administrador.ProductoDTOAdmin.productoDTOAdmin;

@RestController
@RequestMapping("/api/admins/clientes")
public class AdministradorController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private ClienteService clienteService;

    @PostMapping("/productos/por-ids")
    public ResponseEntity<List<productoDTOAdmin>> obtenerProductos(@RequestBody List<Long> ids) {
    List<productoDTOAdmin> productos = adminService.obtenerProductosPorIds(ids);
    return ResponseEntity.ok(productos);
}

    @Operation(summary = "Crear administrador", description = "Crea un nuevo administrador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Administrador creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public Administrador crear(@RequestBody Administrador admin) {
        return adminService.crearAdmin(admin);
    }

    @Operation(summary = "Listar administradores", description = "Obtiene una lista de todos los administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Administradores listados correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<Administrador> listar() {
        return adminService.listarAdmins();
    }

    @Operation(summary = "Crear cliente y usuario", description = "Crea un cliente junto con su usuario asociado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente y usuario creados correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    
        @PostMapping("/crearClienteYUsuario")
        public ResponseEntity<String> crearClienteYUsuario(@RequestBody Cliente cliente) {
            try {
                // Delega toda la lógica al servicio
                clienteService.crearCliente(cliente);
                return ResponseEntity.ok("Cliente y usuario creados correctamente");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error al crear cliente y usuario: " + e.getMessage());
            }
        }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente según su RUN")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{run}")
    public ResponseEntity<String> eliminarCliente(@PathVariable String run) {
        try {
            adminService.eliminarCliente(run);
            return ResponseEntity.ok("Cliente eliminado con RUN " + run);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @Operation(summary = "Buscar cliente por RUN", description = "Busca un cliente específico usando su RUN")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
        @GetMapping("/buscarCliente/{run}")
        public ResponseEntity<Cliente> buscarCliente(@PathVariable String run) {
            return adminService.buscarCliente(run)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body(null));
        }

}
