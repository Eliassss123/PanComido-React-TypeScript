package com.pancomido.pancomido.controllerCliente;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.pancomido.pancomido.cliente.assemblerCliente.ClienteModelAssembler;
import com.pancomido.pancomido.cliente.controllerCliente.ClienteController;
import com.pancomido.pancomido.cliente.modelCliente.Cliente;
import com.pancomido.pancomido.cliente.repositoryCliente.ClienteRepository;
import com.pancomido.pancomido.cliente.serviceCliente.ClienteService;
import com.pancomido.pancomido.pedido.modelPedido.Pedido;
import com.pancomido.pancomido.pedido.servicePedido.PedidoService;
import com.pancomido.pancomido.producto.modelProducto.Producto;
import com.pancomido.pancomido.producto.serviceProducto.ProductoService;
import com.pancomido.pancomido.tienda.modelTienda.Tienda;
import com.pancomido.pancomido.tienda.serviceTienda.TiendaService;

import jakarta.persistence.EntityNotFoundException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock private ClienteService clienteService;
    @Mock private ClienteRepository clienteRepository;
    @Mock private ClienteModelAssembler clienteAssembler;
    @Mock private ProductoService productoService;
    @Mock private TiendaService tiendaService;
    @Mock private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Ana");

        when(clienteService.crearCliente(cliente)).thenReturn(cliente);
        when(clienteAssembler.toModel(cliente)).thenReturn(EntityModel.of(cliente));

        ResponseEntity<EntityModel<Cliente>> response = clienteController.crearcliente(cliente);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(clienteService).crearCliente(cliente);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testActualizarCliente_Existente() {
        Cliente cliente = new Cliente();
        cliente.setRun("12345678-9");

        when(clienteService.actualizarCliente("12345678-9", cliente)).thenReturn(cliente);

        ResponseEntity<?> response = clienteController.actualizarCliente("12345678-9", cliente);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(clienteService).actualizarCliente("12345678-9", cliente);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testActualizarCliente_NoEncontrado() {
        when(clienteService.actualizarCliente(eq("no-existe"), any())).thenThrow(new EntityNotFoundException());

        ResponseEntity<?> response = clienteController.actualizarCliente("no-existe", new Cliente());

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).toString().contains("Cliente no encontrado");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testListarTiendas() {
        List<Tienda> tiendas = List.of(new Tienda(), new Tienda());
        when(tiendaService.obtenerTiendasDisponibles()).thenReturn(tiendas);

        ResponseEntity<List<Tienda>> response = clienteController.listarTodasTiendas();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(2);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testListarProductos() {
        List<Producto> productos = List.of(new Producto(), new Producto(), new Producto());
        when(productoService.buscarProductosDisponibles()).thenReturn(productos);

        ResponseEntity<List<Producto>> response = clienteController.listarProductosDisponibles();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(3);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearPedido_Exitoso() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        Tienda tienda = new Tienda();
        tienda.setId(2);
        Producto p1 = new Producto(); p1.setId(1L);
        Producto p2 = new Producto(); p2.setId(2L);
        Pedido pedido = new Pedido();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(tiendaService.obtenerTiendaPorId(2)).thenReturn(Optional.of(tienda));
        when(productoService.obtenerProductosParaPedido(List.of(1, 2))).thenReturn(List.of(p1, p2));
        when(pedidoService.crearPedido(any())).thenReturn(pedido);

        ClienteController.CrearPedidoRequest request = new ClienteController.CrearPedidoRequest(2, List.of(1, 2));

        ResponseEntity<?> response = clienteController.crearPedido(1, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        verify(pedidoService).crearPedido(any());
        verify(productoService).actualizarStockProductos(List.of(p1, p2));
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearPedido_ClienteNoExiste() {
        when(clienteRepository.findById(999)).thenReturn(Optional.empty());

        ClienteController.CrearPedidoRequest request = new ClienteController.CrearPedidoRequest(1, List.of(1));

        ResponseEntity<?> response = clienteController.crearPedido(999, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).toString().contains("Cliente no encontrado");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearPedido_TiendaNoExiste() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(tiendaService.obtenerTiendaPorId(404)).thenReturn(Optional.empty());

        ClienteController.CrearPedidoRequest request = new ClienteController.CrearPedidoRequest(404, List.of(1));

        ResponseEntity<?> response = clienteController.crearPedido(1, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).toString().contains("Tienda no encontrada");
    }
}
