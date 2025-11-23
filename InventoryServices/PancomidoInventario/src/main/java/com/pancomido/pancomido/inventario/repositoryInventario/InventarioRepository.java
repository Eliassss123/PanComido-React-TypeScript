package com.pancomido.pancomido.inventario.repositoryInventario;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancomido.pancomido.inventario.modelInventario.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
}
