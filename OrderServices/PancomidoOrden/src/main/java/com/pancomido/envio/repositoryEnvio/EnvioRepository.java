package com.pancomido.envio.repositoryEnvio;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.pancomido.pancomido.envio.modelEnvio.Envio;

public interface EnvioRepository extends JpaRepository<Envio, Integer> {
    Optional<Envio> findById(Integer Id);
    Optional<Envio> findByCliente_Run(String run);
}

