package com.reporte.repositoryReporte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reporte.modelReporte.Reporte;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Integer> {

    // Reportes entre dos fechas
    List<Reporte> findByFechaGeneracionBetween(LocalDateTime inicio, LocalDateTime fin);

    // Últimos 5 reportes generados
    List<Reporte> findTop5ByOrderByFechaGeneracionDesc();
}
