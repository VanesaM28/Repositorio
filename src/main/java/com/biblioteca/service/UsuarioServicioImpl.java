package com.biblioteca.service;



import com.biblioteca.model.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServicioImpl  {

    @Autowired
    private UsuarioRepository usuarioRepositorio;

    public List<Usuario> obtenerTodos() {
        return usuarioRepositorio.findAll();
    }
}
