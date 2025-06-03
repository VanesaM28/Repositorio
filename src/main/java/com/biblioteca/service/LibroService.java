package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {

    @Autowired
    private static LibroRepository libroRepository;

    public static List<Libro> obtenerLibrosPorAutor(Usuario autor) {
        return libroRepository.findByAutor(autor);
    }
}
