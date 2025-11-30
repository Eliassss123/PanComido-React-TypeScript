package com.pancomido.pancomido.serviceAuth;

import com.pancomido.auth.modelAuth.Usuario;
import com.pancomido.auth.repositoryAuth.UsuarioRepository;
import com.pancomido.auth.serviceAuth.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioService = new UsuarioService();
        ReflectionTestUtils.setField(usuarioService, "usuarioRepository", usuarioRepository);
    }

    @Test
    void crearUsuario_conRolValido_guardaEnRepositorio() {
        Usuario usuario = new Usuario();
        usuario.setRol("CLIENTE");

        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.crearUsuario(usuario);

        assertThat(resultado).isEqualTo(usuario);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void crearUsuario_conRolInvalido_lanzaExcepcion() {
        Usuario usuario = new Usuario();
        usuario.setRol("ROL_INVALIDO");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.crearUsuario(usuario));

        assertThat(ex.getMessage()).contains("Rol inválido");
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void validarCredenciales_correctas_retornaTrue() {
        Usuario usuario = new Usuario();
        usuario.setContrasena("1234");

        when(usuarioRepository.findByCorreo("test@correo.cl")).thenReturn(Optional.of(usuario));

        boolean valido = usuarioService.validarCredenciales("test@correo.cl", "1234");

        assertThat(valido).isTrue();
    }

    @Test
    void validarCredenciales_incorrectas_retornaFalse() {
        Usuario usuario = new Usuario();
        usuario.setContrasena("1234");

        when(usuarioRepository.findByCorreo("test@correo.cl")).thenReturn(Optional.of(usuario));

        boolean valido = usuarioService.validarCredenciales("test@correo.cl", "9999");

        assertThat(valido).isFalse();
    }

    @Test
    void actualizarUsuario_usuarioNoExiste_lanzaExcepcion() {
        Usuario usuario = new Usuario();
        usuario.setId(10L);

        when(usuarioRepository.existsById(10L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.actualizarUsuario(usuario));

        assertThat(ex.getMessage()).contains("Usuario no existe para actualizar");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizarUsuario_usuarioExiste_guardaEnRepositorio() {
        Usuario usuario = new Usuario();
        usuario.setId(10L);

        when(usuarioRepository.existsById(10L)).thenReturn(true);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.actualizarUsuario(usuario);

        assertThat(resultado).isEqualTo(usuario);
        verify(usuarioRepository).save(usuario);
    }
}
