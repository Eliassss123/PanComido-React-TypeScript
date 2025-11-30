package com.pancomido.pancomido.serviceCliente;

import com.pancomido.auth.modelAuth.Usuario;
import com.pancomido.auth.serviceAuth.UsuarioService;
import com.pancomido.cliente.modelCliente.Cliente;
import com.pancomido.cliente.repositoryCliente.ClienteRepository;
import com.pancomido.cliente.serviceCliente.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private UsuarioService usuarioService;

    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteService = new ClienteService();
        ReflectionTestUtils.setField(clienteService, "clienteRepository", clienteRepository);
        ReflectionTestUtils.setField(clienteService, "usuarioService", usuarioService);
    }

    @Test
    void crearCliente_conUsuario_creaUsuarioYCliente() {
        Usuario usuario = new Usuario();
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);

        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setId(1L);

        when(usuarioService.crearUsuario(usuario)).thenReturn(usuarioCreado);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.crearCliente(cliente);

        assertThat(resultado.getUsuario()).isEqualTo(usuarioCreado);
        verify(usuarioService).crearUsuario(usuario);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void actualizarCliente_clienteNoExiste_lanzaExcepcion() {
        when(clienteRepository.findByRun("123")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> clienteService.actualizarCliente("123", new Cliente()));

        assertThat(ex.getMessage()).contains("Cliente no encontrado con RUN");
    }

    @Test
    void eliminarClientePorRun_eliminaUsuarioYCliente() {
        Usuario usuario = new Usuario();
        usuario.setId(5L);

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);

        when(clienteRepository.findByRun("123")).thenReturn(Optional.of(cliente));

        clienteService.eliminarClientePorRun("123");

        verify(usuarioService).eliminarUsuario(5L);
        verify(clienteRepository).delete(cliente);
    }
}
