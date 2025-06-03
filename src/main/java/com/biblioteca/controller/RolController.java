package com.biblioteca.controller;

import com.biblioteca.model.Usuario;
import com.biblioteca.model.Genero;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Reporte;
import com.biblioteca.model.Rol;
import com.biblioteca.service.UsuarioServicio;
import com.biblioteca.service.GeneroService;
import com.biblioteca.service.LibroService;
import com.biblioteca.service.ReporteService;
import com.biblioteca.service.RolService;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class RolController {

    @Autowired
    private UsuarioServicio usuarioService;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private RolService rolService;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // ✅ Solución al error

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("mensaje", "Bienvenido Administrador");
        return "registro";
    }

    @PostMapping("/guardar")
    public RedirectView guardarUsuario(
            @RequestParam("nombre") String nombre,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("rol") String rolNombre,
            HttpSession session
    ) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(password); // Recomendado: usar BCrypt

        Rol rol = rolService.buscarPorNombre(rolNombre.toUpperCase());
        if (rol == null) {
            return new RedirectView("/registro?error=rol_no_encontrado");
        }

        usuario.setRol(rol); // Usuario solo tiene un rol
        usuarioService.guardar(usuario);

        session.setAttribute("usuarioActual", usuario);

        return switch (rolNombre.toUpperCase()) {
            case "ADMIN" -> new RedirectView("/admin");
            case "MODERADOR" -> new RedirectView("/moderador");
            case "AUTOR" -> new RedirectView("/autor");
            case "LECTOR" -> new RedirectView("/lector");
            case "INVITADO" -> new RedirectView("/invitado");
            default -> new RedirectView("/");
        };
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        if (usuario == null || !tieneRol(usuario, "ADMIN")) {
            return "redirect:/";
        }

        List<Usuario> usuarios = usuarioService.obtenerTodos();
        List<Genero> generos = generoService.obtenerTodos();
        List<Reporte> reportes = reporteService.obtenerTodos();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("reportes", reportes);
        model.addAttribute("usuarioActual", usuario);

        return "panel";
    }

    @GetMapping("/moderador")
    public String moderador(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        if (usuario == null || !tieneRol(usuario, "MODERADOR")) {
            return "redirect:/";
        }
        model.addAttribute("mensaje", "Bienvenido Moderador");
        return "moderador";
    }
    @GetMapping("/autor")
    public String panelAutor(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        
        if (usuario == null || !tieneRol(usuario, "AUTOR")) {
            return "redirect:/";
        }

        model.addAttribute("usuarioActual", usuario);
        model.addAttribute("librosDelAutor", libroRepository.findByAutor(usuario));
        return "autor";
    }



    @GetMapping("/lector")
    public String panelLector(HttpSession session, Model model) {
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");
        if (usuarioActual != null && "LECTOR".equalsIgnoreCase(usuarioActual.getRol().getNombre())) {
            List<Libro> libros = libroRepository.findAll();
            model.addAttribute("libros", libros);
            return "lector";
        } else {
            return "redirect:/panel";
        }
    }

    @PostMapping("/reportarContenido")
    public String reportarContenido(@RequestParam Long contenidoId,
                                     @RequestParam String motivo,
                                     @RequestParam String descripcion,
                                     HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        if (usuario == null) {
            return "redirect:/";
        }

        Reporte reporte = new Reporte();
        reporte.setMotivo(motivo);
        reporte.setDescripcion(descripcion);
        reporte.setFecha(LocalDateTime.now());
        reporte.setUsuario(usuario);

        reporteService.saveReporte(reporte);

        return "redirect:/lector?reportado=true";
    }

    @GetMapping("/invitado")
    public String invitado(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        if (usuario == null || !tieneRol(usuario, "INVITADO")) {
            return "redirect:/";
        }

        List<Libro> libros = libroRepository.findAll();
        model.addAttribute("libros", libros);
        model.addAttribute("usuarioActual", usuario);
        
        return "invitado";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/crearLibro")
    public String crearLibroForm(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        if (usuario == null || !tieneRol(usuario, "AUTOR")) {
            return "redirect:/";
        }

        model.addAttribute("mensaje", "Crea un nuevo libro");
        return "crearLibro";
    }

    @PostMapping("/guardarLibro")
    public String guardarLibro(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        if (usuario == null || !tieneRol(usuario, "AUTOR")) {
            return "redirect:/";
        }

        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setDescripcion(descripcion);
        libro.setAutor(usuario);

        libroRepository.save(libro);

        return "redirect:/autor?libroCreado=true";
    }


    private boolean tieneRol(Usuario usuario, String rolNombre) {
        return usuario.getRol() != null &&
               usuario.getRol().getNombre().equalsIgnoreCase(rolNombre);
    }
}

