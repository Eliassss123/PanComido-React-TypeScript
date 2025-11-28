package com.pancomido.pancomido.reporte.serviceReporte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pancomido.pancomido.reporte.modelReporte.Reporte;
import com.pancomido.pancomido.reporte.repositoryReporte.ReporteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    /**
     * Genera un reporte general y lo guarda en la base de datos.
     *
     * IMPORTANTE:
     *  Antes este servicio intentaba usar ClienteRepository, TiendaRepository y
     *  ProductoRepository que pertenecían a OTROS módulos (UserServices, etc.).
     *  Al haber eliminado o movido esos repositorios, esos imports ya no existen,
     *  por eso te daba error de compilación.
     *
     *  Aquí dejamos un ejemplo simple con valores "0" para que el microservicio
     *  compile y funcione con el front. Más adelante puedes reemplazar estos
     *  valores por llamadas HTTP a otros microservicios para calcular los totales
     *  reales.
     */
    public Reporte generarYGuardarReporte() {
        // Valores de ejemplo por ahora
        int totalClientes = 0;
        int totalTiendas = 0;
        int totalProductos = 0;

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

    public void eliminarReporte(Integer id) {
        reporteRepository.deleteById(id);
    }

    public List<Reporte> listarTodosLosReportes() {
        return reporteRepository.findAll();
    }
}
