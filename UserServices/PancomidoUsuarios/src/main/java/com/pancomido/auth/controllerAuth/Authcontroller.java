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

@RestController
@RequestMapping("/api/auth")
public class Authcontroller {

    @Autowired
    private UsuarioService usuarioService;


    @Operation(summary = "Registro de usuario", description = "Registra un nuevo usuario con rol específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })


        @PostMapping("/registro")
        public ResponseEntity<Usuario> registro(@RequestBody Usuario usuario) {
            try {
                // Validar rol
                if (!usuario.getRol().equals("CLIENTE") &&
                    !usuario.getRol().equals("GERENTE") &&
                    !usuario.getRol().equals("ADMIN")) {
                    return ResponseEntity.badRequest().build();
                }

                Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
                return ResponseEntity.ok(nuevoUsuario); // ya no usa assembler
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    @Operation(summary = "Inicio de sesión", description = "Valida las credenciales del usuario y permite el acceso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String correo, @RequestParam String contraseña) {
        try {
            boolean valido = usuarioService.validarCredenciales(correo, contraseña);
            if (valido) {
                return ResponseEntity.ok("Login exitoso");
            } else {
                return ResponseEntity.status(401).body("Credenciales incorrectas");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

        @GetMapping("/{id}")
        public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
            Optional<Usuario> usuario = usuarioService.buscarPorId(id);
            return usuario
                    .map(ResponseEntity::ok)   // devuelve directamente el Usuario
                    .orElse(ResponseEntity.notFound().build());
        }
}
