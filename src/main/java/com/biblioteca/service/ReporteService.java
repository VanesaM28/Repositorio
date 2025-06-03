package com.biblioteca.service;

import com.biblioteca.model.Reporte;
import java.util.List;

public interface ReporteService {
    // Obtener todos los reportes
    List<Reporte> obtenerTodos();

    // Guardar un reporte
    void saveReporte(Reporte reporte);
}
