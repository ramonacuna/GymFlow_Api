package com.gymflow.Services;

import com.gymflow.Repository.MembresiaRepository;
import com.gymflow.models.Membresia;

import java.util.List;

public class MembresiaServiceImpl implements MembresiaService {
    private final MembresiaRepository membresiaRepository;

    public MembresiaServiceImpl(MembresiaRepository membresiaRepository) {
        this.membresiaRepository = membresiaRepository;
    }

    @Override
    public List<Membresia> findAll() {
        return List.of();
    }

    @Override
    public Membresia findBySocioIdAndEstado(Integer socioId, Integer estado) {
        return null;
    }

    @Override
    public List<Membresia> findByEstado(String estado) {
        return List.of();
    }
}
