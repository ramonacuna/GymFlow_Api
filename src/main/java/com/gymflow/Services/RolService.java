package com.gymflow.services;

import com.gymflow.models.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {
    List<Rol> findAll();
    Optional<Rol> findById(Integer id);
    Rol save(Rol rol);
    void deleteById(Integer id);
    Optional<Rol> findByNombre(String nombre);
}
