package com.pancomido.auth.repositoryAuth;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancomido.auth.modelAuth.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo); 
}
