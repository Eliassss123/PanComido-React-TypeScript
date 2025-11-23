package com.pancomido.tienda.controllerTienda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tiendas")
public class TiendaController {

    @Autowired
    private TiendaService tiendaService;

    @Autowired
    private TiendaModelAssembler tiendaAssembler;

    @Operation(summary = "Crear tienda", description = "Crea una nueva tienda en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tienda creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Tienda>> crearTienda(@RequestBody Tienda tienda) {
        Tienda creada = tiendaService.crearTienda(tienda);
        return ResponseEntity.ok(tiendaAssembler.toModel(creada));
    }

    @Operation(summary = "Listar tiendas disponibles", description = "Muestra solo las tiendas que tienen disponibilidad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tiendas disponibles listadas correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/disponibles")
    public ResponseEntity<List<EntityModel<Tienda>>> listarTiendasDisponibles() {
        List<EntityModel<Tienda>> disponibles = tiendaService.obtenerTiendasDisponibles()
            .stream().map(tiendaAssembler::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(disponibles);
    }

    @Operation(summary = "Listar todas las tiendas", description = "Devuelve todas las tiendas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tiendas listadas correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<EntityModel<Tienda>>> obtenerTiendas() {
        List<EntityModel<Tienda>> todas = tiendaService.obtenerTiendas()
            .stream().map(tiendaAssembler::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(todas);
    }
}
