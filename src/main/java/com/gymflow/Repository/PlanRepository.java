package com.gymflow.repository;

import com.gymflow.models.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Integer> {
    Optional<Plan> findByNombre(String nombre);
}
