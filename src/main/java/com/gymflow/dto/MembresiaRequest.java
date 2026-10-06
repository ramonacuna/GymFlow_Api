package com.gymflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembresiaRequest {
    @NotBlank(message = "La fecha de inicio es obligatoria")
    private String fechaInicio;

    @NotBlank(message = "La fecha de fin es obligatoria")
    private String fechaFin;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "El ID del socio es obligatorio")
    private Integer socioId;

    @NotNull(message = "El ID del plan es obligatorio")
    private Integer planId;
}
