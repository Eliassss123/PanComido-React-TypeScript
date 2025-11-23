package com.pancomido.pancomido.controllerAuth;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.pancomido.pancomido.auth.assemblerAuth.UsuarioModelAssembler;
import com.pancomido.pancomido.auth.controllerAuth.Authcontroller;
import com.pancomido.pancomido.auth.modelAuth.Usuario;
import com.pancomido.pancomido.auth.repositoryAuth.UsuarioRepository;
import com.pancomido.pancomido.auth.serviceAuth.UsuarioService;
import com.pancomido.pancomido.cliente.serviceCliente.ClienteService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private Authcontroller authcontroller;

    @Mock private ClienteService clienteService;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private UsuarioModelAssembler usuarioAssembler;
    @Mock private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testRegistroUsuario() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@example.com");
        usuario.setContraseña("1234");

        when(usuarioService.crearUsuario(usuario)).thenReturn(usuario);
        when(usuarioAssembler.toModel(usuario)).thenReturn(EntityModel.of(usuario));

        ResponseEntity<EntityModel<Usuario>> response = authcontroller.registro(usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent().getCorreo()).isEqualTo("test@example.com");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testLoginExitoso() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("correo@dominio.com");
        usuario.setContraseña("secreta");

        when(usuarioRepository.findByCorreo("correo@dominio.com")).thenReturn(Optional.of(usuario));

        ResponseEntity<String> response = authcontroller.login("correo@dominio.com", "secreta");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Login exitoso");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testLoginFallido() {
        when(usuarioRepository.findByCorreo("usuario@x.com")).thenReturn(Optional.empty());

        ResponseEntity<String> response = authcontroller.login("usuario@x.com", "malacontraseña");

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
        assertThat(response.getBody()).contains("Credenciales incorrectas");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testLoginContraseñaIncorrecta() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("correo@dominio.com");
        usuario.setContraseña("correcta");

        when(usuarioRepository.findByCorreo("correo@dominio.com")).thenReturn(Optional.of(usuario));

        ResponseEntity<String> response = authcontroller.login("correo@dominio.com", "incorrecta");

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }
}
