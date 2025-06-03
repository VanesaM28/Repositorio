package com.biblioteca.service;

//RolService.java

import com.biblioteca.model.Rol;

public interface RolService {
 Rol buscarPorNombre(String nombre);
 Rol guardar(Rol rol); // ← AGREGA ESTA LÍNEA
}
