package com.pancomido.pancomido.controllerPedido;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.pancomido.pancomido.cliente.modelCliente.Cliente;
import com.pancomido.pancomido.cliente.repositoryCliente.ClienteRepository;
import com.pancomido.pancomido.pedido.assemblerPedido.PedidoModelAssembler;
import com.pancomido.pancomido.pedido.controllerPedido.PedidoController;
import com.pancomido.pancomido.pedido.modelPedido.Pedido;
import com.pancomido.pancomido.pedido.repositoryPedido.PedidoRepository;
import com.pancomido.pancomido.pedido.servicePedido.PedidoService;
import com.pancomido.pancomido.producto.modelProducto.Producto;
import com.pancomido.pancomido.producto.repositoryProducto.ProductoRepository;
import com.pancomido.pancomido.tienda.modelTienda.Tienda;
import com.pancomido.pancomido.tienda.repositoryTienda.TiendaRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PedidoControllerTest {

    @InjectMocks
    private PedidoController pedidoController;

    @Mock private PedidoService pedidoService;
    @Mock private ProductoRepository productoRepository;
    @Mock private PedidoRepository pedidoRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private TiendaRepository tiendaRepository;
    @Mock private PedidoModelAssembler pedidoAssembler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearPedido_Exitoso() {
        PedidoController.PedidoDTO dto = new PedidoController.PedidoDTO(1, 1, List.of(10, 20));

        Cliente cliente = new Cliente(); cliente.setId(1);
        Tienda tienda = new Tienda(); tienda.setId(1);
        Producto p1 = new Producto(); p1.setId(10L);
        Producto p2 = new Producto(); p2.setId(20L);
        Pedido pedido = new Pedido(); pedido.setId(99);

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(tiendaRepository.findById(1)).thenReturn(Optional.of(tienda));
        when(productoRepository.findAllById(dto.getProductosIds())).thenReturn(List.of(p1, p2));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoAssembler.toModel(pedido)).thenReturn(EntityModel.of(pedido));

        ResponseEntity<?> response = pedidoController.crearPedido(dto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(pedidoRepository).save(any());
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearPedido_ProductosFaltantes() {
        PedidoController.PedidoDTO dto = new PedidoController.PedidoDTO(1, 1, List.of(10, 20));

        when(clienteRepository.findById(1)).thenReturn(Optional.of(new Cliente()));
        when(tiendaRepository.findById(1)).thenReturn(Optional.of(new Tienda()));
        when(productoRepository.findAllById(List.of(10, 20))).thenReturn(List.of(new Producto())); // solo 1 producto encontrado

        ResponseEntity<?> response = pedidoController.crearPedido(dto);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody().toString()).contains("Productos no encontrados");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearPedido_ClienteNoExiste() {
        PedidoController.PedidoDTO dto = new PedidoController.PedidoDTO(99, 1, List.of(1));

        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<?> response = pedidoController.crearPedido(dto);

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody().toString()).contains("Cliente no encontrado");
    }

    @Test
    void testListarPedidos() {
        List<Pedido> pedidos = List.of(new Pedido(), new Pedido());

        when(pedidoService.listarPedidos()).thenReturn(pedidos);

        List<Pedido> result = pedidoController.listarPedidos();

        assertThat(result).hasSize(2);
        verify(pedidoService).listarPedidos();
    }

    @Test
    void testActualizarEstado() {
        Pedido actualizado = new Pedido();
        actualizado.setEstado("ENVIADO");

        when(pedidoService.actualizarEstado(1, "ENVIADO")).thenReturn(actualizado);

        Pedido response = pedidoController.actualizarEstado(1, "ENVIADO");

        assertThat(response.getEstado()).isEqualTo("ENVIADO");
        verify(pedidoService).actualizarEstado(1, "ENVIADO");
    }

    @Test
    void testObtenerPorId() {
        Pedido pedido = new Pedido();
        pedido.setId(42);

        when(pedidoService.obtenerPedidoPorId(42)).thenReturn(pedido);

        Pedido result = pedidoController.obtenerPorId(42);

        assertThat(result.getId()).isEqualTo(42);
    }
}
