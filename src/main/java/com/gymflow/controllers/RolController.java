package com.gymflow.controllers;

import com.gymflow.services.RolService;
import com.gymflow.models.Rol;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Roles", description = "Gestión de roles de usuario (requiere rol ADMIN)")
@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @Operation(summary = "Listar roles", description = "Obtiene la lista de todos los roles del sistema.")
    @GetMapping
    public ResponseEntity<List<Rol>> getAll() {
        return ResponseEntity.ok(rolService.findAll());
    }

    @Operation(summary = "Obtener rol por ID", description = "Obtiene los detalles de un rol específico por ID.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return rolService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar rol por nombre", description = "Obtiene un rol por su nombre único (ej: ADMIN, SOCIO).")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getByNombre(@PathVariable String nombre) {
        return rolService.findByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear rol", description = "Crea un nuevo rol en el sistema.")
    @PostMapping
    public ResponseEntity<Rol> create(@RequestBody Rol rol) {
        Rol saved = rolService.save(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Eliminar rol", description = "Elimina un rol por su ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return rolService.findById(id).map(rol -> {
            rolService.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
