package com.gymflow.services;

import com.gymflow.models.Socio;

import java.util.List;
import java.util.Optional;

public interface SocioService {
    List<Socio> findAll();
    Optional<Socio> findById(Integer id);
    Socio save(Socio socio);
    void deleteById(Integer id);
    Optional<Socio> findByDni(String dni);
    Optional<Socio> findByDni(int dni);
    boolean existsByDni(String dni);
    boolean existsByDni(int dni);
    Optional<Socio> findByUsuarioId(Integer usuarioId);
}
