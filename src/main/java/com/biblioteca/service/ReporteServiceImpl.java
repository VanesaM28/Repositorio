package com.biblioteca.service;

import com.biblioteca.model.Reporte;
import com.biblioteca.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Override
    public List<Reporte> obtenerTodos() {
        return reporteRepository.findAll();
    }

    @Override
    public void saveReporte(Reporte reporte) {
        reporteRepository.save(reporte);  // Guarda el reporte en la base de datos
    }
}

