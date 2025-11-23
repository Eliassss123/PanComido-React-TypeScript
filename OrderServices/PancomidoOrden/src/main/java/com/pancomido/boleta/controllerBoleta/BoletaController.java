package com.pancomido.boleta.controllerBoleta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pancomido.pancomido.boleta.assemblerBoleta.BoletaModelAssembler;
import com.pancomido.pancomido.boleta.modelBoleta.Boleta;
import com.pancomido.pancomido.boleta.serviceBoleta.BoletaService;
import com.pancomido.pancomido.pedido.modelPedido.Pedido;
import com.pancomido.pancomido.pedido.repositoryPedido.PedidoRepository;
import com.pancomido.pancomido.pedido.servicePedido.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/boletas")
public class BoletaController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private BoletaModelAssembler boletaAssembler;

    @Autowired
    private BoletaService boletaService;


    @GetMapping("/{id}")
    @Operation(summary = "Generar boleta por ID", description = "Obtiene y genera la boleta para un pedido específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boleta generada correctamente"),
        @ApiResponse(responseCode = "404", description = "Boleta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> generarBoleta(@PathVariable Integer id) {
        try {
            Boleta boleta = boletaService.generarBoleta(id);
            return ResponseEntity.ok(boletaAssembler.toModel(boleta));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Boleta no encontrada: " + e.getMessage());
        }
    }

    @Operation(summary = "Agregar detalle de boleta", description = "Agrega una línea de producto a una boleta ya existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle agregado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/agregar")
    public ResponseEntity<Boleta> agregarDetalle(@RequestBody Boleta detalle) {
        Boleta guardado = boletaService.guardarBoleta(detalle);
        return ResponseEntity.ok(guardado);
    }
}

