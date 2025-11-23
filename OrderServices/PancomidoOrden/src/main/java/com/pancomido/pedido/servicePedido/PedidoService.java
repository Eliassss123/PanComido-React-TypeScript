package com.pancomido.pedido.servicePedido;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.pancomido.pedido.modelPedido.Pedido;
import com.pancomido.pedido.repositoryPedido.PedidoRepository;


import java.util.List;

@Service
public class PedidoService {

    @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }


    @Autowired
    private RestTemplate restTemplate;  // para llamadas HTTP

    public Pedido crearPedido(Pedido pedido, List<Integer> productosIds) {
        // Llamamos al microservicio de productos
        String url = "http://localhost:8082/api/productos/listaPorIds"; 
        // imaginemos que tu microservicio de producto expone este endpoint
        List<Producto> productos = restTemplate.postForObject(url, productosIds, List.class);

        if (productos == null || productos.size() != productosIds.size()) {
            throw new RuntimeException("Algunos productos no existen");
        }

        pedido.setProductos(productos);
        return pedidoRepository.save(pedido);
    }



    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido actualizarEstado(Integer id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public Pedido obtenerPedidoPorId(Integer id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }
    
    public Pedido crearPedido(Pedido pedido) {
    return pedidoRepository.save(pedido);
    }
}
