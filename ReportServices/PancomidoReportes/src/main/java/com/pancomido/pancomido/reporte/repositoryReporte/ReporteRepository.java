package com.pancomido.pancomido.reporte.repositoryReporte;

import jakarta.persistence.criteria.CriteriaBuilder.In;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.pancomido.pancomido.reporte.modelReporte.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByFechaGeneracionBetween(LocalDateTime inicio, LocalDateTime fin);
    
    List<Reporte> findTop5ByOrderByFechaGeneracionDesc();

    Optional<Reporte> findById(Integer id);
}

