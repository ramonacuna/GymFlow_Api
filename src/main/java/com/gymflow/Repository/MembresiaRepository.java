package com.gymflow.Repository;

import com.gymflow.models.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembresiaRepository extends JpaRepository<Membresia, Integer> {

    List<Membresia> findBySocioId(Integer socioId);

    Optional<Membresia> findBySocioIdAndEstado(Integer socioId, Integer estado);

    List<Membresia> findByEstado(String estado);
}
