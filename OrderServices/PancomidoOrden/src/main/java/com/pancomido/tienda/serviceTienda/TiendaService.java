package com.pancomido.tienda.serviceTienda;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pancomido.tienda.modelTienda.Tienda;
import com.pancomido.tienda.repositoryTienda.TiendaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TiendaService {

    @Autowired
    private TiendaRepository tiendaRepository;

    public Tienda crearTienda(Tienda tienda) {
        return tiendaRepository.save(tienda);
    }

    public List<Tienda> obtenerTiendas() {
        return tiendaRepository.findAll();
    }
    public List<Tienda> obtenerTiendasDisponibles() {
    return tiendaRepository.findAll(); 
    }
    public Optional<Tienda> obtenerTiendaPorId(Integer id) {
    return tiendaRepository.findById(id);
    }
}

