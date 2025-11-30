package com.producto.serviceProducto;

import com.producto.modelProducto.Producto;
import com.producto.repositoryProducto.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void listarProductos_deberiaRetornarTodosLosProductos() {
        List<Producto> productos = List.of(new Producto(), new Producto());
        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.listarProductos();

        assertEquals(productos, resultado);
        verify(productoRepository).findAll();
    }

    @Test
    void buscarProductos_deberiaRetornarTodosCuandoNombreEsNull() {
        List<Producto> productos = List.of(new Producto());
        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.buscarProductos(null);

        assertEquals(productos, resultado);
        verify(productoRepository).findAll();
        verify(productoRepository, never()).findByNombreContaining(anyString());
    }

    @Test
    void buscarProductos_deberiaRetornarTodosCuandoNombreEsVacio() {
        List<Producto> productos = List.of(new Producto());
        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.buscarProductos("");

        assertEquals(productos, resultado);
        verify(productoRepository).findAll();
        verify(productoRepository, never()).findByNombreContaining(anyString());
    }

    @Test
    void buscarProductos_deberiaFiltrarPorNombreCuandoNoEsVacio() {
        List<Producto> productos = List.of(new Producto());
        when(productoRepository.findByNombreContaining("pan")).thenReturn(productos);

        List<Producto> resultado = productoService.buscarProductos("pan");

        assertEquals(productos, resultado);
        verify(productoRepository).findByNombreContaining("pan");
        verify(productoRepository, never()).findAll();
    }

    @Test
    void obtenerProductoPorId_deberiaDelegarEnRepositorio() {
        Producto producto = new Producto();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        var resultado = productoService.obtenerProductoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(producto, resultado.get());
    }

    @Test
    void actualizarProducto_deberiaActualizarCamposYGuardar() {
        Producto existente = new Producto();
        existente.setId(1L);
        existente.setNombre("Viejo");
        existente.setPrecio(1000.0);
        existente.setAvatarUrl("viejo.png");
        existente.setCategoria("Vieja");
        existente.setStockProducto(5);

        Producto actualizado = new Producto();
        actualizado.setNombre("Nuevo");
        actualizado.setPrecio(2000.0);
        actualizado.setAvatarUrl("nuevo.png");
        actualizado.setCategoria("Nueva");
        actualizado.setStockProducto(10);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        var resultadoOpt = productoService.actualizarProducto(1L, actualizado);

        assertTrue(resultadoOpt.isPresent());
        Producto resultado = resultadoOpt.get();

        assertEquals("Nuevo", resultado.getNombre());
        assertEquals(2000.0, resultado.getPrecio());
        assertEquals("nuevo.png", resultado.getAvatarUrl());
        assertEquals("Nueva", resultado.getCategoria());
        assertEquals(10, resultado.getStockProducto());

        verify(productoRepository).save(existente);
    }

    @Test
    void buscarProductosDisponibles_deberiaLlamarFindByStockProductoGreaterThan() {
        List<Producto> productos = List.of(new Producto());
        when(productoRepository.findByStockProductoGreaterThan(0)).thenReturn(productos);

        List<Producto> resultado = productoService.buscarProductosDisponibles();

        assertEquals(productos, resultado);
        verify(productoRepository).findByStockProductoGreaterThan(0);
    }

    @Test
    void obtenerProductosParaPedido_deberiaLanzarEntityNotFoundSiFaltanIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        // Solo se encuentra 1 y 2, falta el 3
        Producto p1 = new Producto(); p1.setId(1L);
        Producto p2 = new Producto(); p2.setId(2L);
        when(productoRepository.findAllById(ids)).thenReturn(List.of(p1, p2));

        assertThrows(EntityNotFoundException.class,
                () -> productoService.obtenerProductosParaPedido(ids));
    }

    @Test
    void obtenerProductosParaPedido_deberiaLanzarExcepcionSiProductoSinStock() {
        List<Long> ids = List.of(1L);

        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Pan sin stock");
        p.setStockProducto(0); // sin stock

        when(productoRepository.findAllById(ids)).thenReturn(List.of(p));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoService.obtenerProductosParaPedido(ids));

        assertTrue(ex.getMessage().contains("sin stock"));
    }

    @Test
    void actualizarStockProductos_deberiaDescontarStockYGuardar() {
        Producto p1 = new Producto();
        p1.setNombre("Pan 1");
        p1.setStockProducto(3);

        Producto p2 = new Producto();
        p2.setNombre("Pan 2");
        p2.setStockProducto(1);

        List<Producto> productos = Arrays.asList(p1, p2);

        productoService.actualizarStockProductos(productos);

        assertEquals(2, p1.getStockProducto()); // 3 - 1
        assertEquals(0, p2.getStockProducto()); // 1 - 1

        verify(productoRepository, times(2)).save(any(Producto.class));
    }

    @Test
    void actualizarStockProductos_deberiaLanzarExcepcionSiStockNuloOCero() {
        Producto p = new Producto();
        p.setNombre("Pan malo");
        p.setStockProducto(0);

        List<Producto> productos = List.of(p);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoService.actualizarStockProductos(productos));

        assertTrue(ex.getMessage().contains("0"));
        verify(productoRepository, never()).save(any());
    }
}
