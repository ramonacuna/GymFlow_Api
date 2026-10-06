package com.gymflow.services;

import com.gymflow.models.Membresia;

import java.util.List;
import java.util.Optional;

public interface MembresiaService {
    List<Membresia> findAll();
    Optional<Membresia> findById(Integer id);
    Membresia save(Membresia membresia);
    void deleteById(Integer id);
    List<Membresia> findBySocioId(Integer socioId);
    Optional<Membresia> findBySocioIdAndEstado(Integer socioId, String estado);
    List<Membresia> findByEstado(String estado);
}
