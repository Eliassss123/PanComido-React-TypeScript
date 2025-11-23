package com.pancomido.cliente.repositoryCliente;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancomido.cliente.modelCliente.*;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByRun(String run);
    Optional<Cliente> findById(Integer id);
}
