package com.pancomido.administrador.serviceAdministrador;

import com.pancomido.administrador.modelAdministrador.Administrador;
import com.pancomido.cliente.modelCliente.Cliente;
import com.pancomido.cliente.serviceCliente.ClienteService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pancomido.administrador.ProductoDTOAdmin.productoDTOAdmin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {


    @Autowired
    private RestTemplate restTemplate;

    private final String PRODUCT_SERVICE_URL = "http://localhost:8082/api/productos/por-ids";

    public List<productoDTOAdmin> obtenerProductosPorIds(List<Long> ids) {
        ResponseEntity<productoDTOAdmin[]> response = restTemplate.postForEntity(
            PRODUCT_SERVICE_URL, ids, productoDTOAdmin[].class
        );

        productoDTOAdmin[] productosArray = response.getBody();
        if (productosArray == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(productosArray);
    }


    @Autowired
    private ClienteService clienteService;


    // Métodos de administrador (simples)
    public Administrador crearAdmin(Administrador admin) {
        // Guardar admin en DB (deberías inyectar AdminRepository)
        return admin; 
    }

    public List<Administrador> listarAdmins() {
        // Listar admins desde DB (deberías inyectar AdminRepository)
        return List.of(); 
    }

    // Métodos de cliente
    public void crearCliente(Cliente cliente) {
        clienteService.crearCliente(cliente);
    }

    // Métodos de cliente
    public Optional<Cliente> buscarCliente(String run) {
        return clienteService.buscarPorRun(run); // usar el método correcto
    }

    public void eliminarCliente(String run) {
        clienteService.eliminarClientePorRun(run); // usar el método correcto
    }

    // --------------------------
    // Integración con microservicio de productos
    // --------------------------
    public List<productoDTOAdmin> listarProductosDisponibles() {
        String url = "http://localhost:8082/api/productos/disponibles"; 
        return Arrays.asList(restTemplate.getForObject(url, productoDTOAdmin[].class));
    }

public List<productoDTOAdmin> obtenerProductosPorIds(List<Long> ids) {
    String url = "http://localhost:8082/api/productos/obtener/{id}";
    return Arrays.asList(restTemplate.postForObject(url, ids, productoDTOAdmin[].class));
}
}
