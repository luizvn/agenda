package com.agenda.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContatoRequestDTO(
    @NotBlank(message = "O nome não pode ser vazio.")
    String nome,

    @NotBlank(message = "O telefone não pode ser vazio.")
    @Size(min = 10, max = 11, message = "O telefone deve ter entre 10 e 11 caracteres.")
    @Pattern(regexp = "^[0-9\\s()-]+$", message = "O telefone contém caracteres inválidos.")
    String telefone
) {}
