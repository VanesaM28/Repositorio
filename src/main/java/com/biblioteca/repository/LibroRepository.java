package com.biblioteca.repository;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByAutor(Usuario autor); // Ahora el autor es de tipo Usuario
}
