package com.gymflow.Services;

import com.gymflow.Repository.RolRepository;
import com.gymflow.models.Rol;

public class RolServiceImpl implements RolService {
    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public Rol findByNombre(String nombre) {
        return null;
    }
}
