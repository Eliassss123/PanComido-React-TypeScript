package com.pancomido.tienda.repositoryTienda;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancomido.tienda.modelTienda.Tienda;

public interface TiendaRepository extends JpaRepository<Tienda, Integer> {
        Optional<Tienda> findById(Integer id);
}
