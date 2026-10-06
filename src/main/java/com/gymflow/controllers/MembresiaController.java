package com.gymflow.controllers;

import com.gymflow.services.MembresiaService;
import com.gymflow.services.PlanService;
import com.gymflow.services.SocioService;
import com.gymflow.dto.MembresiaRequest;
import com.gymflow.models.Membresia;
import com.gymflow.models.Plan;
import com.gymflow.models.Socio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Membresías", description = "Gestión de las suscripciones y membresías de los socios")
@RestController
@RequestMapping("/api/membresias")
public class MembresiaController {

    private final MembresiaService membresiaService;
    private final SocioService socioService;
    private final PlanService planService;

    public MembresiaController(
            MembresiaService membresiaService,
            SocioService socioService,
            PlanService planService
    ) {
        this.membresiaService = membresiaService;
        this.socioService = socioService;
        this.planService = planService;
    }

    @Operation(summary = "Listar membresías", description = "Obtiene todas las membresías registradas.")
    @GetMapping
    public ResponseEntity<List<Membresia>> getAll() {
        return ResponseEntity.ok(membresiaService.findAll());
    }

    @Operation(summary = "Obtener membresía por ID", description = "Obtiene la información de una membresía por su identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return membresiaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar membresías de un socio", description = "Obtiene el historial de membresías asociadas a un socio específico.")
    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<Membresia>> getBySocioId(@PathVariable Integer socioId) {
        return ResponseEntity.ok(membresiaService.findBySocioId(socioId));
    }

    @Operation(summary = "Listar membresías por estado", description = "Filtra membresías por su estado actual (ej: ACTIVA, VENCIDA, CANCELADA).")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Membresia>> getByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(membresiaService.findByEstado(estado));
    }

    @Operation(summary = "Obtener membresía por socio y estado", description = "Consulta una membresía específica filtrando por el ID de socio y el estado.")
    @GetMapping("/socio/{socioId}/estado/{estado}")
    public ResponseEntity<?> getBySocioIdAndEstado(@PathVariable Integer socioId, @PathVariable String estado) {
        return membresiaService.findBySocioIdAndEstado(socioId, estado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear membresía", description = "Registra una nueva membresía vinculando un socio y un plan.")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MembresiaRequest request) {
        Socio socio = socioService.findById(request.getSocioId())
                .orElseThrow(() -> new IllegalArgumentException("Socio no encontrado con ID: " + request.getSocioId()));

        Plan plan = planService.findById(request.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado con ID: " + request.getPlanId()));

        Membresia membresia = new Membresia();
        membresia.setFechaInicio(request.getFechaInicio());
        membresia.setFechaFin(request.getFechaFin());
        membresia.setEstado(request.getEstado());
        membresia.setSocio(socio);
        membresia.setPlan(plan);

        Membresia saved = membresiaService.save(membresia);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Actualizar membresía", description = "Actualiza las fechas, estado o plan de una membresía existente.")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody MembresiaRequest request) {
        return membresiaService.findById(id).map(membresia -> {
            if (request.getFechaInicio() != null) membresia.setFechaInicio(request.getFechaInicio());
            if (request.getFechaFin() != null) membresia.setFechaFin(request.getFechaFin());
            if (request.getEstado() != null) membresia.setEstado(request.getEstado());

            if (request.getPlanId() != null) {
                Plan plan = planService.findById(request.getPlanId()).orElse(null);
                if (plan != null) membresia.setPlan(plan);
            }

            Membresia updated = membresiaService.save(membresia);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar membresía", description = "Elimina una membresía por su ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return membresiaService.findById(id).map(membresia -> {
            membresiaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
