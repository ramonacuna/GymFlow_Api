package com.gymflow.controllers;

import com.gymflow.repository.RolRepository;
import com.gymflow.services.UsuarioService;
import com.gymflow.models.Rol;
import com.gymflow.models.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Usuarios", description = "Gestión de cuentas de usuario del sistema")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(
            UsuarioService usuarioService,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioService = usuarioService;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Listar usuarios", description = "Obtiene la lista de todos los usuarios registrados.")
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Operation(summary = "Obtener usuario por ID", description = "Obtiene la información de un usuario por su identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar usuario por email", description = "Obtiene los datos de un usuario por su correo electrónico.")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByEmail(@PathVariable String email) {
        return usuarioService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario con contraseña cifrada (BCrypt).")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Usuario usuario) {
        if (usuarioService.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El email ya está en uso"));
        }

        if (usuario.getRol() != null && usuario.getRol().getId() != null) {
            Rol rol = rolRepository.findById(usuario.getRol().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
            usuario.setRol(rol);
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario saved = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza el correo, contraseña o rol de un usuario existente.")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Usuario usuarioActualizado) {
        return usuarioService.findById(id).map(usuario -> {
            usuario.setEmail(usuarioActualizado.getEmail());

            if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isBlank()) {
                usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
            }

            if (usuarioActualizado.getRol() != null && usuarioActualizado.getRol().getId() != null) {
                Rol rol = rolRepository.findById(usuarioActualizado.getRol().getId()).orElse(null);
                if (rol != null) {
                    usuario.setRol(rol);
                }
            }

            Usuario updated = usuarioService.save(usuario);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return usuarioService.findById(id).map(usuario -> {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
