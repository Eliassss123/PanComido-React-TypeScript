package com.pancomido.pancomido.controllerBoleta;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.pancomido.pancomido.boleta.assemblerBoleta.BoletaModelAssembler;
import com.pancomido.pancomido.boleta.controllerBoleta.BoletaController;
import com.pancomido.pancomido.boleta.modelBoleta.Boleta;
import com.pancomido.pancomido.boleta.serviceBoleta.BoletaService;
import com.pancomido.pancomido.pedido.repositoryPedido.PedidoRepository;
import com.pancomido.pancomido.pedido.servicePedido.PedidoService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BoletaControllerTest {

    @InjectMocks
    private BoletaController boletaController;

    @Mock private PedidoService pedidoService;
    @Mock private PedidoRepository pedidoRepository;
    @Mock private BoletaService boletaService;
    @Mock private BoletaModelAssembler boletaAssembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testGenerarBoleta_Exitosa() {
        Boleta boleta = new Boleta();
        boleta.setId(10);

        when(boletaService.generarBoleta(10)).thenReturn(boleta);
        when(boletaAssembler.toModel(boleta)).thenReturn(EntityModel.of(boleta));

        ResponseEntity<?> response = boletaController.generarBoleta(10);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(boletaService).generarBoleta(10);
        verify(boletaAssembler).toModel(boleta);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testGenerarBoleta_NoEncontrada() {
        when(boletaService.generarBoleta(99)).thenThrow(new RuntimeException("No existe"));

        ResponseEntity<?> response = boletaController.generarBoleta(99);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("Boleta no encontrada: No existe");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testAgregarDetalleBoleta() {
        Boleta detalle = new Boleta();
        detalle.setId(5);

        when(boletaService.guardarBoleta(detalle)).thenReturn(detalle);

        ResponseEntity<Boleta> response = boletaController.agregarDetalle(detalle);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getId()).isEqualTo(5);
        verify(boletaService).guardarBoleta(detalle);
    }
}
