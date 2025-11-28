package com.pancomido.auth.controllerAuth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pancomido.auth.modelAuth.Usuario;
import com.pancomido.auth.serviceAuth.UsuarioService;

import java.util.Optional;

@CrossOrigin(origins = "*") // para dev: acepta cualquier origen (5173 incluido)
@RestController
@RequestMapping("/api/auth")
public class Authcontroller {

    @Autowired
    private UsuarioService usuarioService;

    // --------------------------
    // REGISTRO
    // --------------------------
    @Operation(summary = "Registro de usuario", description = "Registra un nuevo usuario con rol específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/registro")
    public ResponseEntity<Usuario> registro(@RequestBody Usuario usuario) {
        try {
            if (!"CLIENTE".equals(usuario.getRol()) &&
                !"GERENTE".equals(usuario.getRol()) &&
                !"ADMIN".equals(usuario.getRol())) {
                return ResponseEntity.badRequest().build();
            }

            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // --------------------------
    // LOGIN
    // --------------------------
    @Operation(summary = "Inicio de sesión", description = "Valida las credenciales del usuario y permite el acceso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
            @ApiResponse(responseCode = "400", description = "Parámetros faltantes"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam("correo") String correo,
            @RequestParam(value = "contrasena", required = false) String contrasena,
            @RequestParam(value = "contraseña", required = false) String contrasenaEnie
    ) {
        try {
            // Aceptar tanto "contrasena" como "contraseña"
            String pass = contrasena != null ? contrasena : contrasenaEnie;

            if (pass == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Falta parámetro contrasena/contraseña");
            }

            boolean valido = usuarioService.validarCredenciales(correo, pass);
            if (valido) {
                return ResponseEntity.ok("Login exitoso");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // --------------------------
    // OBTENER USUARIO POR ID (opcional)
    // --------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        return usuario
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
