package com.inventario.repositoryInventario;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.modelInventario.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
}
