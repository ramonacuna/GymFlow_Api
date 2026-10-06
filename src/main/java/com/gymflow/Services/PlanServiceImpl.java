package com.gymflow.services;

import com.gymflow.repository.PlanRepository;
import com.gymflow.models.Plan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    @Override
    public Optional<Plan> findById(Integer id) {
        return planRepository.findById(id);
    }

    @Override
    public Plan save(Plan plan) {
        return planRepository.save(plan);
    }

    @Override
    public void deleteById(Integer id) {
        planRepository.deleteById(id);
    }

    @Override
    public Optional<Plan> findByNombre(String nombre) {
        return planRepository.findByNombre(nombre);
    }
}
