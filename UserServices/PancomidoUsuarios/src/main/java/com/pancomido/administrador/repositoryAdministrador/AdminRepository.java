package com.pancomido.administrador.repositoryAdministrador;


import org.springframework.data.jpa.repository.JpaRepository;

import com.pancomido.administrador.modelAdministrador.Administrador;
import com.pancomido.cliente.modelCliente.*;

public interface AdminRepository extends JpaRepository<Administrador, Integer> {
}

