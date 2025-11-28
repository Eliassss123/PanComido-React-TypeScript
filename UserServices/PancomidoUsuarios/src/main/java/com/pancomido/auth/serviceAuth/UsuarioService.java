package com.pancomido.auth.serviceAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.pancomido.auth.modelAuth.Usuario;
import com.pancomido.auth.repositoryAuth.UsuarioRepository;


import java.util.Optional;

@Service

public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;




    
    public Usuario crearUsuario(Usuario usuario) {
        // Validar rol
        if (!List.of("CLIENTE", "GERENTE", "ADMIN").contains(usuario.getRol())) {
            throw new RuntimeException("Rol inválido: " + usuario.getRol());
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean validarCredenciales(String correo, String contrasena) {
        return usuarioRepository.findByCorreo(correo)
                .map(u -> u.getContrasena().equals(contrasena))
                .orElse(false);
    }

    
        public Usuario actualizarUsuario(Usuario usuario) {
            if (usuario.getId() == null || !usuarioRepository.existsById(usuario.getId())) {
                throw new RuntimeException("Usuario no existe para actualizar");
            }
            return usuarioRepository.save(usuario);
        }
}
