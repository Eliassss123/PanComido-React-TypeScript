package com.pancomido.boleta.controllerBoleta;

import com.pancomido.boleta.modelBoleta.Boleta;
import com.pancomido.boleta.serviceBoleta.BoletaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boletas")
@RequiredArgsConstructor
public class BoletaController {

    private final BoletaService boletaService;

    /**
     * Devuelve el resumen de boleta para un pedido específico.
     * Endpoint pensado para ser consumido desde el front después de crear el pedido.
     *
     * Ejemplo: GET /api/boletas/resumen/5
     */
    @GetMapping("/resumen/{pedidoId}")
    public ResponseEntity<Boleta> obtenerResumen(@PathVariable Long pedidoId) {
        Boleta boleta = boletaService.generarResumen(pedidoId);
        return ResponseEntity.ok(boleta);
    }
}
