package com.pancomido.pancomido.controllerCliente;

import com.pancomido.cliente.controllerCliente.ClienteController;
import com.pancomido.cliente.modelCliente.Cliente;
import com.pancomido.cliente.serviceCliente.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearCliente_exitoso_devuelve200() {
        Cliente cliente = new Cliente();

        when(clienteService.crearCliente(cliente)).thenReturn(cliente);

        ResponseEntity<?> response = clienteController.crearCliente(cliente);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(cliente);
    }

    @Test
    void crearCliente_error_devuelve500() {
        Cliente cliente = new Cliente();
        doThrow(new RuntimeException("Error"))
                .when(clienteService).crearCliente(cliente);

        ResponseEntity<?> response = clienteController.crearCliente(cliente);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().toString()).contains("Error al crear cliente y usuario");
    }

    @Test
    void actualizarCliente_exitoso_devuelve200() {
        Cliente clienteActualizado = new Cliente();

        when(clienteService.actualizarCliente("123", clienteActualizado))
                .thenReturn(clienteActualizado);

        ResponseEntity<?> response =
                clienteController.actualizarCliente("123", clienteActualizado);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(clienteActualizado);
    }

    @Test
    void actualizarCliente_noEncontrado_devuelve404() {
        Cliente clienteActualizado = new Cliente();

        when(clienteService.actualizarCliente("123", clienteActualizado))
                .thenThrow(new EntityNotFoundException());

        ResponseEntity<?> response =
                clienteController.actualizarCliente("123", clienteActualizado);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void listarClientes_devuelveLista() {
        List<Cliente> lista = List.of(new Cliente(), new Cliente());
        when(clienteService.obtenerClientes()).thenReturn(lista);

        ResponseEntity<List<Cliente>> response = clienteController.listarClientes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void eliminarCliente_exitoso_devuelve200() {
        ResponseEntity<String> response = clienteController.eliminarCliente("123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(clienteService).eliminarClientePorRun("123");
    }

    @Test
    void eliminarCliente_noEncontrado_devuelve404() {
        doThrow(new RuntimeException("No existe"))
                .when(clienteService).eliminarClientePorRun("123");

        ResponseEntity<String> response = clienteController.eliminarCliente("123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("No existe");
    }

    @Test
    void obtenerClientePorRun_encontrado_devuelve200() {
        Cliente cliente = new Cliente();
        when(clienteService.buscarPorRun("123")).thenReturn(Optional.of(cliente));

        ResponseEntity<Cliente> response =
                clienteController.obtenerClientePorRun("123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(cliente);
    }

    @Test
    void obtenerClientePorRun_noEncontrado_devuelve404() {
        when(clienteService.buscarPorRun("123")).thenReturn(Optional.empty());

        ResponseEntity<Cliente> response =
                clienteController.obtenerClientePorRun("123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
