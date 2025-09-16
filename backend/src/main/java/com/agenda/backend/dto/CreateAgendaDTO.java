package com.agenda.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAgendaDTO(
    @NotBlank String nome, 
    @NotNull String tipo
) {}