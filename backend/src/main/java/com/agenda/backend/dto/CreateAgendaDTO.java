package com.agenda.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAgendaDTO(
    @NotBlank(message = "O nome não pode ser vazio.")
    String nome,

    @NotNull(message = "O tipo não pode ser nulo.")
    String tipo
) {}