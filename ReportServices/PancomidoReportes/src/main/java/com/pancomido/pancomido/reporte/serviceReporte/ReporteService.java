package com.pancomido.pancomido.reporte.serviceReporte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pancomido.pancomido.cliente.repositoryCliente.ClienteRepository;
import com.pancomido.pancomido.producto.repositoryProducto.ProductoRepository;
import com.pancomido.pancomido.reporte.modelReporte.*;
import com.pancomido.pancomido.reporte.repositoryReporte.*;
import com.pancomido.pancomido.tienda.repositoryTienda.TiendaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ReporteRepository reporteRepository;

    public Reporte generarYGuardarReporte() {
        int totalClientes = clienteRepository.findAll().size();
        int totalTiendas = tiendaRepository.findAll().size();
        int totalProductos = productoRepository.findAll().size();

        Reporte reporte = new Reporte();
        reporte.setFechaGeneracion(LocalDateTime.now());
        reporte.setTotalClientes(totalClientes);
        reporte.setTotalTiendas(totalTiendas);
        reporte.setTotalProductos(totalProductos);

        return reporteRepository.save(reporte);
    }

    public Optional<Reporte> obtenerReportePorId(Integer id) {
        return reporteRepository.findById(id);
    }

    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }

    public List<Reporte> listarTodosLosReportes() {
        return reporteRepository.findAll();
    }

}