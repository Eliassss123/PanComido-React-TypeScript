package com.pancomido.pancomido.controllerTienda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.pancomido.pancomido.tienda.assemblerTienda.TiendaModelAssembler;
import com.pancomido.pancomido.tienda.controllerTienda.TiendaController;
import com.pancomido.pancomido.tienda.modelTienda.Tienda;
import com.pancomido.pancomido.tienda.serviceTienda.TiendaService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TiendaControllerTest {

    @InjectMocks
    private TiendaController tiendaController;

    @Mock
    private TiendaService tiendaService;

    @Mock
    private TiendaModelAssembler tiendaAssembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearTienda() {
        Tienda tienda = new Tienda();
        tienda.setNombre("Sucursal Centro");

        when(tiendaService.crearTienda(tienda)).thenReturn(tienda);
        when(tiendaAssembler.toModel(tienda)).thenReturn(EntityModel.of(tienda));

        ResponseEntity<EntityModel<Tienda>> response = tiendaController.crearTienda(tienda);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent().getNombre()).isEqualTo("Sucursal Centro");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testListarTiendasDisponibles() {
        Tienda t1 = new Tienda(); t1.setNombre("Tienda A");
        Tienda t2 = new Tienda(); t2.setNombre("Tienda B");

        when(tiendaService.obtenerTiendasDisponibles()).thenReturn(List.of(t1, t2));
        when(tiendaAssembler.toModel(t1)).thenReturn(EntityModel.of(t1));
        when(tiendaAssembler.toModel(t2)).thenReturn(EntityModel.of(t2));

        ResponseEntity<List<EntityModel<Tienda>>> response = tiendaController.listarTiendasDisponibles();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(2);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testObtenerTodasLasTiendas() {
        Tienda t1 = new Tienda(); t1.setId(1);
        Tienda t2 = new Tienda(); t2.setId(2);

        when(tiendaService.obtenerTiendas()).thenReturn(List.of(t1, t2));
        when(tiendaAssembler.toModel(t1)).thenReturn(EntityModel.of(t1));
        when(tiendaAssembler.toModel(t2)).thenReturn(EntityModel.of(t2));

        ResponseEntity<List<EntityModel<Tienda>>> response = tiendaController.obtenerTiendas();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(2);
        verify(tiendaService).obtenerTiendas();
    }
}
