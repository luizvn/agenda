package com.agenda.backend.dto;

import java.util.List;

public record AgendaListResponseDTO(
    Long id,
    String nome,
    List<ContatoResponseDTO> contatos
) implements AgendaTypedResponse {}
