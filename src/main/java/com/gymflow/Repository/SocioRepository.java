package com.gymflow.repository;

import com.gymflow.models.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Integer> {
    Optional<Socio> findByDni(String dni);
    boolean existsByDni(String dni);

    // Compatibilidad si se pasa entero
    default Optional<Socio> findByDni(int dni) {
        return findByDni(String.valueOf(dni));
    }
    default boolean existsByDni(int dni) {
        return existsByDni(String.valueOf(dni));
    }

    //(1:1) Con usuario
    Optional<Socio> findByUsuario_Id(Integer usuarioId);

    default Optional<Socio> findByUsuarioId(Integer usuarioId) {
        return findByUsuario_Id(usuarioId);
    }
}
