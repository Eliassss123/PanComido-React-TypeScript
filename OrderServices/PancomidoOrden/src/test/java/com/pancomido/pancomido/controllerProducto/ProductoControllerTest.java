package com.pancomido.pancomido.controllerProducto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.pancomido.pancomido.producto.assemblerProducto.ProductoModelAssembler;
import com.pancomido.pancomido.producto.controllerProducto.ProductoController;
import com.pancomido.pancomido.producto.modelProducto.Producto;
import com.pancomido.pancomido.producto.serviceProducto.ProductoService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ProductoControllerTest {

    @InjectMocks
    private ProductoController productoController;

    @Mock
    private ProductoService productoService;

    @Mock
    private ProductoModelAssembler productoAssembler;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarProductos_SinFiltro() {
        List<Producto> productos = List.of(new Producto(), new Producto());
        when(productoService.buscarProductos(null)).thenReturn(productos);

        List<Producto> resultado = productoController.listarProductos(null);

        assertThat(resultado).hasSize(2);
    }

    @Test
    void testListarProductosDisponibles() {
        List<Producto> productos = List.of(new Producto());
        when(productoService.buscarProductosDisponibles()).thenReturn(productos);

        List<Producto> resultado = productoController.listarProductosDisponibles();

        assertThat(resultado).hasSize(1);
        verify(productoService).buscarProductosDisponibles();
    }

    @Test
    void testCrearProducto() {
        Producto producto = new Producto();
        producto.setNombre("Leche");

        when(productoService.crearProducto(producto)).thenReturn(producto);
        when(productoAssembler.toModel(producto)).thenReturn(EntityModel.of(producto));

        EntityModel<Producto> respuesta = productoController.crearProducto(producto);

        assertThat(respuesta.getContent().getNombre()).isEqualTo("Leche");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testObtenerProductoPorId_Encontrado() {
        Producto producto = new Producto();
        producto.setId(10L);
        Optional<Producto> optionalProducto = Optional.of(producto);

        when(productoService.obtenerProductoPorId(10)).thenReturn(optionalProducto);
        when(productoAssembler.toModel(producto)).thenReturn(EntityModel.of(producto));

        ResponseEntity<EntityModel<Producto>> respuesta = productoController.obtenerProductoPorId(10, null);

        assertThat(respuesta.getStatusCodeValue()).isEqualTo(200);
        assertThat(respuesta.getBody().getContent().getId()).isEqualTo(10);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testObtenerProductoPorId_NoEncontrado() {
        when(productoService.obtenerProductoPorId(999)).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<Producto>> respuesta = productoController.obtenerProductoPorId(999, null);

        assertThat(respuesta.getStatusCodeValue()).isEqualTo(404);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testActualizarProducto_Existe() {
        Producto actualizado = new Producto();
        actualizado.setNombre("Pan");

        when(productoService.actualizarProducto(1, actualizado)).thenReturn(Optional.of(actualizado));

        ResponseEntity<Producto> response = productoController.actualizarProducto(1, actualizado);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getNombre()).isEqualTo("Pan");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testActualizarProducto_NoExiste() {
        when(productoService.actualizarProducto(eq(2), any())).thenReturn(Optional.empty());

        ResponseEntity<Producto> response = productoController.actualizarProducto(2, new Producto());

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }
}
