package com.pancomido.pancomido.controllerAdministrador;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import com.pancomido.pancomido.administrador.controllerAdministrador.AdministradorController;
import com.pancomido.pancomido.administrador.modelAdministrador.Administrador;
import com.pancomido.pancomido.administrador.serviceAdministrador.AdminService;
import com.pancomido.pancomido.auth.modelAuth.Usuario;
import com.pancomido.pancomido.auth.serviceAuth.UsuarioService;
import com.pancomido.pancomido.cliente.modelCliente.Cliente;
import com.pancomido.pancomido.cliente.serviceCliente.ClienteService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AdministradorControllerTest {

    @InjectMocks
    private AdministradorController adminController;

    @Mock
    private AdminService adminService;
    @Mock
    private ClienteService clienteService;
    @Mock
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearAdministrador() {
        Administrador admin = new Administrador();
        admin.setNombre("Pedro");

        when(adminService.crearAdmin(admin)).thenReturn(admin);

        Administrador creado = adminController.crear(admin);

        assertThat(creado.getNombre()).isEqualTo("Pedro");
        verify(adminService).crearAdmin(admin);
    }

    @Test
    void testListarAdministradores() {
        List<Administrador> lista = List.of(new Administrador(), new Administrador());
        when(adminService.listarAdmins()).thenReturn(lista);

        List<Administrador> respuesta = adminController.listar();

        assertThat(respuesta).hasSize(2);
        verify(adminService).listarAdmins();
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearClienteYUsuario_conUsuario() {
        Usuario usuario = new Usuario();
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);

        ResponseEntity<String> response = adminController.crearClienteYUsuario(cliente);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(usuarioService).crearUsuario(usuario);
        verify(clienteService).crearCliente(cliente);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearClienteYUsuario_conError() {
        Cliente cliente = new Cliente();
        doThrow(new RuntimeException("Error inesperado")).when(clienteService).crearCliente(cliente);

        ResponseEntity<String> response = adminController.crearClienteYUsuario(cliente);

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody()).contains("Error al crear cliente y usuario");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testEliminarCliente_exitoso() {
        ResponseEntity<String> response = adminController.eliminarCliente("12345678-9");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Cliente eliminado");
        verify(adminService).eliminarCliente("12345678-9");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testEliminarCliente_noEncontrado() {
        doThrow(new RuntimeException("No existe")).when(adminService).eliminarCliente("99999999-9");

        ResponseEntity<String> response = adminController.eliminarCliente("99999999-9");

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).contains("No existe");
    }

    @Test
    void testBuscarCliente_existente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        when(adminService.buscarCliente("11111111-1")).thenReturn(Optional.of(cliente));

        Cliente encontrado = adminController.buscarCliente("11111111-1");

        assertThat(encontrado.getNombre()).isEqualTo("Juan");
    }

    @Test
    void testBuscarCliente_noEncontrado() {
        when(adminService.buscarCliente("00000000-0")).thenReturn(Optional.empty());

        try {
            adminController.buscarCliente("00000000-0");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("Cliente no encontrado");
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    void testActualizarCliente_exitoso() {
        Cliente actualizado = new Cliente();
        actualizado.setNombre("Nuevo nombre");

        when(adminService.actualizarCliente("12345678-9", actualizado)).thenReturn(actualizado);

        ResponseEntity<Cliente> response = adminController.actualizarCliente("12345678-9", actualizado);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getNombre()).isEqualTo("Nuevo nombre");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testActualizarCliente_noEncontrado() {
        when(adminService.actualizarCliente(eq("00000000-0"), any())).thenThrow(new RuntimeException());

        ResponseEntity<Cliente> response = adminController.actualizarCliente("00000000-0", new Cliente());

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }
}
