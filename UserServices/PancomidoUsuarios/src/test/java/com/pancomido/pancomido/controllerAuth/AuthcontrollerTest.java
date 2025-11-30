package com.pancomido.pancomido.controllerAuth;

import com.pancomido.auth.controllerAuth.Authcontroller;
import com.pancomido.auth.modelAuth.Usuario;
import com.pancomido.auth.serviceAuth.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AuthcontrollerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private Authcontroller authcontroller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registro_conRolValido_devuelve200() {
        Usuario usuario = new Usuario();
        usuario.setRol("CLIENTE");

        when(usuarioService.crearUsuario(usuario)).thenReturn(usuario);

        ResponseEntity<Usuario> response = authcontroller.registro(usuario);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(usuarioService).crearUsuario(usuario);
    }

    @Test
    void registro_conRolInvalido_devuelve400() {
        Usuario usuario = new Usuario();
        usuario.setRol("ROL_INVALIDO");

        ResponseEntity<Usuario> response = authcontroller.registro(usuario);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(usuarioService);
    }

    @Test
    void login_conCredencialesValidas_devuelve200() {
        when(usuarioService.validarCredenciales("test@correo.cl", "1234"))
                .thenReturn(true);

        ResponseEntity<String> response =
                authcontroller.login("test@correo.cl", "1234", null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Login exitoso");
    }

    @Test
    void login_conPasswordFaltante_devuelve400() {
        ResponseEntity<String> response =
                authcontroller.login("test@correo.cl", null, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void login_conCredencialesInvalidas_devuelve401() {
        when(usuarioService.validarCredenciales("test@correo.cl", "1234"))
                .thenReturn(false);

        ResponseEntity<String> response =
                authcontroller.login("test@correo.cl", "1234", null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void obtenerUsuario_encontrado_devuelve200() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioService.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> response = authcontroller.obtenerUsuario(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(usuario);
    }

    @Test
    void obtenerUsuario_noEncontrado_devuelve404() {
        when(usuarioService.buscarPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = authcontroller.obtenerUsuario(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
