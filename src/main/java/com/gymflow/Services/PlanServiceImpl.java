package com.gymflow.Services;

import com.gymflow.Repository.PlanRepository;
import com.gymflow.models.Plan;

public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public Plan findByNombre(String nombre) {
        return null;
    }
}
