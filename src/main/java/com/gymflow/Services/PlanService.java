package com.gymflow.services;

import com.gymflow.models.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanService {
    List<Plan> findAll();
    Optional<Plan> findById(Integer id);
    Plan save(Plan plan);
    void deleteById(Integer id);
    Optional<Plan> findByNombre(String nombre);
}
