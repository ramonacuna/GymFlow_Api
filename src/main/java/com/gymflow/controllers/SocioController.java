package com.gymflow.controllers;

import com.gymflow.repository.UsuarioRepository;
import com.gymflow.services.SocioService;
import com.gymflow.dto.SocioRequest;
import com.gymflow.models.Socio;
import com.gymflow.models.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Socios", description = "Gestión de los socios del gimnasio")
@RestController
@RequestMapping("/api/socios")
public class SocioController {

    private final SocioService socioService;
    private final UsuarioRepository usuarioRepository;

    public SocioController(SocioService socioService, UsuarioRepository usuarioRepository) {
        this.socioService = socioService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Listar socios", description = "Obtiene la lista de todos los socios registrados.")
    @GetMapping
    public ResponseEntity<List<Socio>> getAll() {
        return ResponseEntity.ok(socioService.findAll());
    }

    @Operation(summary = "Obtener socio por ID", description = "Obtiene la información de un socio por su identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return socioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar socio por DNI", description = "Obtiene la información de un socio mediante su número de DNI.")
    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> getByDni(@PathVariable String dni) {
        return socioService.findByDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener socio por ID de usuario", description = "Obtiene el perfil de socio asociado a una cuenta de usuario.")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getByUsuarioId(@PathVariable Integer usuarioId) {
        return socioService.findByUsuarioId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear socio", description = "Registra un nuevo socio y lo asocia a una cuenta de usuario existente.")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody SocioRequest request) {
        if (socioService.existsByDni(request.getDni())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Ya existe un socio con este DNI"));
        }

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + request.getUsuarioId()));

        Socio socio = new Socio();
        socio.setNombre(request.getNombre());
        socio.setDni(request.getDni());
        socio.setTelefono(request.getTelefono());
        socio.setUsuario(usuario);

        Socio saved = socioService.save(socio);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Actualizar socio", description = "Actualiza los datos personales de un socio.")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Socio socioActualizado) {
        return socioService.findById(id).map(socio -> {
            socio.setNombre(socioActualizado.getNombre());
            socio.setDni(socioActualizado.getDni());
            socio.setTelefono(socioActualizado.getTelefono());
            Socio updated = socioService.save(socio);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar socio", description = "Elimina un socio por su identificador.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return socioService.findById(id).map(socio -> {
            socioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
