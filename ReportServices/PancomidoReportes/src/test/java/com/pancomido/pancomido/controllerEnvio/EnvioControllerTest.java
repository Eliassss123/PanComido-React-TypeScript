package com.pancomido.pancomido.controllerEnvio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.pancomido.pancomido.envio.assemblerEnvio.EnvioModelAssembler;
import com.pancomido.pancomido.envio.controllerEnvio.EnvioController;
import com.pancomido.pancomido.envio.modelEnvio.Envio;
import com.pancomido.pancomido.envio.serviceEnvio.EnvioService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class EnvioControllerTest {

    @InjectMocks
    private EnvioController envioController;

    @Mock private EnvioService envioService;
    @Mock private EnvioModelAssembler envioAssembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearEnvio() {
        Envio envio = new Envio();
        envio.setEstado("En camino");

        when(envioService.crearEnvio(envio)).thenReturn(envio);
        when(envioAssembler.toModel(envio)).thenReturn(EntityModel.of(envio));

        ResponseEntity<EntityModel<Envio>> response = envioController.crearEnvio(envio);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent().getEstado()).isEqualTo("En camino");
        verify(envioService).crearEnvio(envio);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testEliminarEnvio_Exitoso() {
        ResponseEntity<?> response = envioController.eliminarEnvio(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().toString()).contains("Envio eliminado con id: 1");
        verify(envioService).eliminarEnvio(1);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testEliminarEnvio_NoEncontrado() {
        doThrow(new RuntimeException("No existe")).when(envioService).eliminarEnvio(99);

        ResponseEntity<?> response = envioController.eliminarEnvio(99);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("No existe");
    }

    @Test
    void testActualizarEstado() {
        Envio actualizado = new Envio();
        actualizado.setId(1);
        actualizado.setEstado("Entregado");

        when(envioService.actualizarEstado(1, "Entregado")).thenReturn(actualizado);

        Envio response = envioController.actualizarEstado(1, "Entregado");

        assertThat(response.getEstado()).isEqualTo("Entregado");
        verify(envioService).actualizarEstado(1, "Entregado");
    }

    @Test
    void testListarEnvios() {
        List<Envio> envios = List.of(new Envio(), new Envio());

        when(envioService.obtenerTodos()).thenReturn(envios);

        List<Envio> result = envioController.listarEnvios();

        assertThat(result).hasSize(2);
        verify(envioService).obtenerTodos();
    }
}
