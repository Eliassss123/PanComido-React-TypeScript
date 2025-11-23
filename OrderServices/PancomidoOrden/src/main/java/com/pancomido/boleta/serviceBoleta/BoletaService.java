package com.pancomido.boleta.serviceBoleta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pancomido.pancomido.boleta.modelBoleta.Boleta;
import com.pancomido.pancomido.boleta.repositoryBoleta.BoletaRepository;
import com.pancomido.pancomido.pedido.modelPedido.Pedido;
import com.pancomido.pancomido.pedido.repositoryPedido.PedidoRepository;

import java.util.List;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Boleta> obtenerPorPedido(Pedido pedido) {
        return boletaRepository.findByPedido(pedido);
    }

    public Boleta guardarBoleta(Boleta boleta) {
        boleta.setSubtotal(boleta.getCantidad() * boleta.getPrecioUnitario());
        return boletaRepository.save(boleta);
    }
    public Boleta generarBoleta(Integer pedidoId) {
    Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + pedidoId));

    List<Boleta> boletas = boletaRepository.findByPedido(pedido);

    if (boletas.isEmpty()) {
        throw new RuntimeException("No hay boletas asociadas al pedido con ID: " + pedidoId);
    }

    return boletas.get(0); // puedes ajustar esto según tu lógica
}
}
