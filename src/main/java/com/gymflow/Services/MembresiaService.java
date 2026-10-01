package com.gymflow.Services;

import com.gymflow.models.Membresia;

import java.util.List;

public interface MembresiaService {
    List<Membresia> findAll();
    Membresia findBySocioIdAndEstado(Integer socioId, Integer estado);
    List<Membresia> findByEstado(String estado);
}
