package com.gymflow.Services;

import com.gymflow.Repository.SocioRepository;
import com.gymflow.models.Socio;

public class SocioServiceImpl implements SocioService {

    private final SocioRepository socioRepository;

    public SocioServiceImpl(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }


    @Override
    public Socio findByDni(String dni) {
        return null;
    }

    @Override
    public boolean existsByDni(String dni) {
        return false;
    }

    @Override
    public Socio findByUsuarioId(Integer usuarioId) {
        return null;
    }
}
