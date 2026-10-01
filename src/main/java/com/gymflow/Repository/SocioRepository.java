package com.gymflow.Repository;

import com.gymflow.models.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Integer> {
    Optional<Socio> findByDni(int dni);

    boolean existsByDni(int dni);

    //(1:1)
    Optional<Socio> findByUsuarioId(Integer usuarioId);
}
