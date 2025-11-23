package com.pancomido.pancomido.inventario.assemblerInventario;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.pancomido.pancomido.inventario.controllerInventario.InventarioController;
import com.pancomido.pancomido.inventario.modelInventario.Inventario;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InventarioModelAssembler implements RepresentationModelAssembler<Inventario, EntityModel<Inventario>> {

    @Override
    public EntityModel<Inventario> toModel(Inventario inventario) {
        return EntityModel.of(inventario,
            linkTo(methodOn(InventarioController.class).buscarPorId(inventario.getId())).withSelfRel(),
            linkTo(methodOn(InventarioController.class).listarInventario()).withRel("inventario")
        );
    }
}
