package com.pancomido.pancomido.serviceAdministrador;

import com.pancomido.administrador.ProductoDTOAdmin.productoDTOAdmin;
import com.pancomido.administrador.serviceAdministrador.AdminService;
import com.pancomido.cliente.modelCliente.Cliente;
import com.pancomido.cliente.serviceCliente.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ClienteService clienteService;

    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminService = new AdminService();
        ReflectionTestUtils.setField(adminService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(adminService, "clienteService", clienteService);
    }

    @Test
    void crearCliente_delegaEnClienteService() {
        Cliente cliente = new Cliente();

        adminService.crearCliente(cliente);

        verify(clienteService).crearCliente(cliente);
    }

    @Test
    void buscarCliente_delegaEnClienteServiceYRetornaResultado() {
        Cliente cliente = new Cliente();
        when(clienteService.buscarPorRun("123")).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = adminService.buscarCliente("123");

        assertThat(resultado).isPresent();
        assertThat(resultado.get()).isEqualTo(cliente);
    }

    @Test
    void eliminarCliente_delegaEnClienteService() {
        adminService.eliminarCliente("123");

        verify(clienteService).eliminarClientePorRun("123");
    }

    @Test
    void listarProductosDisponibles_convierteArrayEnLista() {
        productoDTOAdmin[] arr = {
                new productoDTOAdmin(1L, "Pan", 1000.0),
                new productoDTOAdmin(2L, "Kuchen", 2000.0)
        };

        when(restTemplate.getForObject(anyString(), eq(productoDTOAdmin[].class)))
                .thenReturn(arr);

        List<productoDTOAdmin> resultado = adminService.listarProductosDisponibles();

        assertThat(resultado).hasSize(2);
    }

    @Test
    void obtenerProductosPorId_convierteArrayEnLista() {
        List<Long> ids = Arrays.asList(1L, 2L);
        productoDTOAdmin[] arr = {
                new productoDTOAdmin(1L, "Pan", 1000.0),
                new productoDTOAdmin(2L, "Kuchen", 2000.0)
        };

        ResponseEntity<productoDTOAdmin[]> response =
                new ResponseEntity<>(arr, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), eq(ids), eq(productoDTOAdmin[].class)))
                .thenReturn(response);

        List<productoDTOAdmin> resultado = adminService.obtenerProductosPorId(ids);

        assertThat(resultado).hasSize(2);
    }

    @Test
    void obtenerProductosPorIds_convierteArrayEnLista() {
        List<Long> ids = Arrays.asList(1L, 2L);
        productoDTOAdmin[] arr = {
                new productoDTOAdmin(1L, "Pan", 1000.0),
                new productoDTOAdmin(2L, "Kuchen", 2000.0)
        };

        when(restTemplate.postForObject(anyString(), eq(ids), eq(productoDTOAdmin[].class)))
                .thenReturn(arr);

        List<productoDTOAdmin> resultado = adminService.obtenerProductosPorIds(ids);

        assertThat(resultado).hasSize(2);
    }
}
