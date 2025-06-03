package com.biblioteca.service;

import com.biblioteca.model.Genero;
import com.biblioteca.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneroServiceImpl implements GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    @Override
    public List<Genero> obtenerTodos() {
        return generoRepository.findAll();
    }
}
