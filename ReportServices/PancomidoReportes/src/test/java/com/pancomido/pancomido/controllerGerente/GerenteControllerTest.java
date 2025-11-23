package com.pancomido.pancomido.controllerGerente;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.pancomido.pancomido.cliente.modelCliente.Cliente;
import com.pancomido.pancomido.gerente.assemblerGerente.GerenteModelAssembler;
import com.pancomido.pancomido.gerente.controllerGerente.GerenteController;
import com.pancomido.pancomido.gerente.modelGerente.Gerente;
import com.pancomido.pancomido.gerente.serviceGerente.GerenteService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GerenteControllerTest {

    @InjectMocks
    private GerenteController gerenteController;

    @Mock private GerenteService gerenteService;
    @Mock private GerenteModelAssembler gerenteAssembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCrearGerente() {
        Gerente gerente = new Gerente();
        gerente.setNombre("Camila");

        when(gerenteService.crearGerente(gerente)).thenReturn(gerente);
        when(gerenteAssembler.toModel(gerente)).thenReturn(EntityModel.of(gerente));

        ResponseEntity<EntityModel<Gerente>> response = gerenteController.crearGerente(gerente);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent().getNombre()).isEqualTo("Camila");
    }

    @Test
    void testListarGerentes() {
        List<Gerente> gerentes = List.of(new Gerente(), new Gerente());
        when(gerenteService.obtenerGerentes()).thenReturn(gerentes);

        List<Gerente> resultado = gerenteController.listarGerentes();

        assertThat(resultado).hasSize(2);
        verify(gerenteService).obtenerGerentes();
    }



    @Test
    void testBuscarClientePorRun_Existe() {
        Cliente cliente = new Cliente();
        cliente.setRun("22222222-2");

        when(gerenteService.buscarClientePorRun("22222222-2")).thenReturn(Optional.of(cliente));

        Cliente resultado = gerenteController.buscarClientePorRun("22222222-2");

        assertThat(resultado.getRun()).isEqualTo("22222222-2");
    }

    @Test
    void testBuscarClientePorRun_NoExiste() {
        when(gerenteService.buscarClientePorRun("00000000-0")).thenReturn(Optional.empty());

        try {
            gerenteController.buscarClientePorRun("00000000-0");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("Cliente no encontrado con RUN");
        }
    }
}

