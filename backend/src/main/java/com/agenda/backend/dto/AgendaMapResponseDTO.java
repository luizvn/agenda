package com.agenda.backend.dto;

import java.util.Map;

public record AgendaMapResponseDTO(
    Long id,
    String nome,
    Map<String, ContatoResponseDTO> contatos
) implements AgendaTypedResponse {}
