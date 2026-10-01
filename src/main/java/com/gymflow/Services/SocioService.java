package com.gymflow.Services;

import com.gymflow.models.Socio;

public interface SocioService {
    Socio findByDni(String dni);
    boolean existsByDni(String dni);
    Socio findByUsuarioId(Integer usuarioId);

}
