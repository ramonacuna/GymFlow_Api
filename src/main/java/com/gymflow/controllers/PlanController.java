package com.gymflow.controllers;

import com.gymflow.services.PlanService;
import com.gymflow.models.Plan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Planes", description = "Gestión de planes de membresía del gimnasio")
@RestController
@RequestMapping("/api/planes")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @Operation(summary = "Listar planes", description = "Obtiene la lista de todos los planes disponibles (acceso público).")
    @GetMapping
    public ResponseEntity<List<Plan>> getAll() {
        return ResponseEntity.ok(planService.findAll());
    }

    @Operation(summary = "Obtener plan por ID", description = "Obtiene los detalles de un plan por su identificador (acceso público).")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return planService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar plan por nombre", description = "Busca un plan por su nombre exacto (acceso público).")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getByNombre(@PathVariable String nombre) {
        return planService.findByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear plan", description = "Crea un nuevo plan (requiere rol ADMIN).")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Plan> create(@RequestBody Plan plan) {
        Plan saved = planService.save(plan);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Actualizar plan", description = "Actualiza la información de un plan existente (requiere rol ADMIN).")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Plan planActualizado) {
        return planService.findById(id).map(plan -> {
            plan.setNombre(planActualizado.getNombre());
            plan.setPrecio(planActualizado.getPrecio());
            plan.setDuracionDias(planActualizado.getDuracionDias());
            Plan updated = planService.save(plan);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar plan", description = "Elimina un plan por su ID (requiere rol ADMIN).")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return planService.findById(id).map(plan -> {
            planService.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
