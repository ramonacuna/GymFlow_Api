package com.gymflow.Services;

import com.gymflow.models.Plan;

public interface PlanService {
    Plan findByNombre(String nombre);
}
