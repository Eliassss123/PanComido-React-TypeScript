package com.inventario.serviceInventario;

import com.inventario.dto.InventarioDTO;
import com.inventario.modelInventario.Inventario;
import com.inventario.repositoryInventario.InventarioRepository;
import com.producto.modelProducto.Producto;
import com.producto.repositoryProducto.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void agregarInventario_deberiaCrearInventarioYActualizarStockProducto() {
        // Arrange
        InventarioDTO dto = new InventarioDTO();
        dto.setProductoId(1L);
        dto.setCantidad(10);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStockProducto(5);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Inventario inventarioGuardado = new Inventario(1, producto, 10);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioGuardado);

        // Act
        Inventario resultado = inventarioService.agregarInventario(dto);

        // Assert
        assertEquals(inventarioGuardado, resultado);

        // Stock del producto se suma
        assertEquals(15, producto.getStockProducto());
        verify(productoRepository).save(producto);

        // Inventario que se envía al repo
        ArgumentCaptor<Inventario> inventarioCaptor = ArgumentCaptor.forClass(Inventario.class);
        verify(inventarioRepository).save(inventarioCaptor.capture());

        Inventario inventarioEnviado = inventarioCaptor.getValue();
        assertEquals(producto, inventarioEnviado.getProducto());
        assertEquals(10, inventarioEnviado.getCantidad());
    }

    @Test
    void eliminarCantidad_deberiaDisminuirInventarioYStockProducto() {
        // Arrange
        InventarioDTO dto = new InventarioDTO();
        dto.setProductoId(1L);
        dto.setCantidad(3);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStockProducto(10);

        Inventario inventario = new Inventario(1, producto, 5);

        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));
        when(inventarioRepository.save(any(Inventario.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // Act
        Inventario resultado = inventarioService.eliminarCantidad(dto);

        // Assert
        assertEquals(2, resultado.getCantidad());      // 5 - 3
        assertEquals(7, producto.getStockProducto());  // 10 - 3

        verify(productoRepository).save(producto);
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void eliminarCantidad_deberiaLanzarExcepcionSiInventarioInsuficiente() {
        // Arrange
        InventarioDTO dto = new InventarioDTO();
        dto.setProductoId(1L);
        dto.setCantidad(10);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStockProducto(100);

        Inventario inventario = new Inventario(1, producto, 5);

        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> inventarioService.eliminarCantidad(dto));

        assertTrue(ex.getMessage().toLowerCase().contains("cantidad insuficiente"));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void eliminarCantidad_deberiaLanzarExcepcionSiStockProductoInsuficiente() {
        // Arrange
        InventarioDTO dto = new InventarioDTO();
        dto.setProductoId(1L);
        dto.setCantidad(5);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStockProducto(3); // stock menor a la cantidad

        Inventario inventario = new Inventario(1, producto, 5);

        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> inventarioService.eliminarCantidad(dto));

        assertTrue(ex.getMessage().toLowerCase().contains("stock insuficiente"));
    }

    @Test
    void obtenerInventario_deberiaDelegarEnRepositorio() {
        List<Inventario> lista = Collections.singletonList(new Inventario());
        when(inventarioRepository.findAll()).thenReturn(lista);

        List<Inventario> resultado = inventarioService.obtenerInventario();

        assertEquals(lista, resultado);
        verify(inventarioRepository).findAll();
    }

    @Test
    void buscarPorId_deberiaDelegarEnRepositorio() {
        Inventario inventario = new Inventario();
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));

        var resultado = inventarioService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(inventario, resultado.get());
    }

    @Test
    void eliminarPorId_deberiaLlamarDeleteById() {
        inventarioService.eliminarPorId(1);
        verify(inventarioRepository).deleteById(1);
    }
}
