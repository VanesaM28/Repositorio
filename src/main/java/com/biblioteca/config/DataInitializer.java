package com.biblioteca.config;

import com.biblioteca.model.Rol;
import com.biblioteca.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolService rolService;

    @Override
    public void run(String... args) throws Exception {
    	String[] nombres = {"ADMIN", "MODERADOR", "LECTOR", "AUTOR", "INVITADO"};
    	for (String nombre : nombres) {
    	    if (rolService.buscarPorNombre(nombre) == null) {
    	        Rol rol = new Rol();
    	        rol.setNombre(nombre);
    	        rolService.guardar(rol); // ← Este método ahora está definido correctamente
    	    }
    	}
    }
}
