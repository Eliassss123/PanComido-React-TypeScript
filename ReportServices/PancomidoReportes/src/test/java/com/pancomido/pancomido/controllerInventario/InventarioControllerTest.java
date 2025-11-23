package com.pancomido.pancomido.controllerInventario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.pancomido.pancomido.inventario.assemblerInventario.InventarioModelAssembler;
import com.pancomido.pancomido.inventario.controllerInventario.InventarioController;
import com.pancomido.pancomido.inventario.modelInventario.Inventario;
import com.pancomido.pancomido.inventario.serviceInventario.InventarioService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class InventarioControllerTest {

    @InjectMocks
    private InventarioController inventarioController;

    @Mock
    private InventarioService inventarioService;

    @Mock
    private InventarioModelAssembler inventarioAssembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testAgregarInventario() {
        Inventario inventario = new Inventario();
        inventario.setId(1);

        when(inventarioService.agregarInventario(inventario)).thenReturn(inventario);
        when(inventarioAssembler.toModel(inventario)).thenReturn(EntityModel.of(inventario));

        ResponseEntity<EntityModel<Inventario>> response = inventarioController.agregarInventario(inventario);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent().getId()).isEqualTo(1);
    }

    @Test
    void testListarInventario() {
        List<Inventario> inventarioList = List.of(new Inventario(), new Inventario());
        when(inventarioService.obtenerInventario()).thenReturn(inventarioList);

        List<Inventario> result = inventarioController.listarInventario();

        assertThat(result).hasSize(2);
        verify(inventarioService).obtenerInventario();
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuscarPorId_Existente() {
        Inventario item = new Inventario();
        item.setId(5);

        when(inventarioService.buscarPorId(5)).thenReturn(Optional.of(item));

        ResponseEntity<Inventario> response = inventarioController.buscarPorId(5);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getId()).isEqualTo(5);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuscarPorId_NoExistente() {
        when(inventarioService.buscarPorId(999)).thenReturn(Optional.empty());

        ResponseEntity<Inventario> response = inventarioController.buscarPorId(999);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testEliminarPorId_Exitoso() {
        ResponseEntity<String> response = inventarioController.eliminarPorId(10);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Producto eliminado del inventario con id: 10");
        verify(inventarioService).eliminarPorId(10);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testEliminarPorId_Error() {
        doThrow(new RuntimeException()).when(inventarioService).eliminarPorId(11);

        ResponseEntity<String> response = inventarioController.eliminarPorId(11);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }
}
