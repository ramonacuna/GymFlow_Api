package com.gymflow.repository;

import com.gymflow.models.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembresiaRepository extends JpaRepository<Membresia, Integer> {

    List<Membresia> findBySocio_Id(Integer socioId);

    default List<Membresia> findBySocioId(Integer socioId) {
        return findBySocio_Id(socioId);
    }

    Optional<Membresia> findBySocio_IdAndEstado(Integer socioId, String estado);

    default Optional<Membresia> findBySocioIdAndEstado(Integer socioId, String estado) {
        return findBySocio_IdAndEstado(socioId, estado);
    }

    List<Membresia> findByEstado(String estado);
}
