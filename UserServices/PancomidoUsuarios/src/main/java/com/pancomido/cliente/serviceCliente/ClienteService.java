package com.pancomido.cliente.serviceCliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pancomido.cliente.modelCliente.Cliente;
import com.pancomido.cliente.repositoryCliente.ClienteRepository;
import com.pancomido.auth.modelAuth.Usuario;
import com.pancomido.auth.serviceAuth.UsuarioService;

import java.util.List;
import java.util.Optional;

@Service

public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioService usuarioService;


    
    // Crear cliente y usuario si no existe
        public Cliente crearCliente(Cliente cliente){
            Usuario usuario = cliente.getUsuario();
            if (usuario != null) {
                usuario.setRol("CLIENTE");
                Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
                cliente.setUsuario(usuarioCreado);
            }
            return clienteRepository.save(cliente);
        }

    // Actualizar cliente y usuario asociado
    public Cliente actualizarCliente(String run, Cliente clienteActualizado) {
        Cliente clienteExistente = clienteRepository.findByRun(run)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con RUN: " + run));

        // Actualizar usuario asociado
        if (clienteExistente.getUsuario() != null && clienteActualizado.getUsuario() != null) {
            Usuario usuarioExistente = clienteExistente.getUsuario();
            Usuario usuarioNuevo = clienteActualizado.getUsuario();

            usuarioExistente.setNombre(usuarioNuevo.getNombre());
            usuarioExistente.setCorreo(usuarioNuevo.getCorreo());
            usuarioExistente.setContraseña(usuarioNuevo.getContraseña());

            if (usuarioExistente.getId() == null) {
                Usuario creado = usuarioService.crearUsuario(usuarioExistente);
                clienteExistente.setUsuario(creado);
            } else {
                usuarioService.actualizarUsuario(usuarioExistente);
            }
        }

        return clienteRepository.save(clienteExistente);
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAll();
    }

    // Buscar cliente por RUN
    public Optional<Cliente> buscarPorRun(String run) {
        return clienteRepository.findByRun(run);
    }

    // Eliminar cliente y usuario asociado
    public void eliminarClientePorRun(String run) {
        Cliente cliente = clienteRepository.findByRun(run)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con RUN: " + run));

        if (cliente.getUsuario() != null && cliente.getUsuario().getId() != null) {
            usuarioService.eliminarUsuario(cliente.getUsuario().getId());
        }
        clienteRepository.delete(cliente);
    }
}
