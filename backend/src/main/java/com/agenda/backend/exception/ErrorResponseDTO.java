package com.agenda.backend.exception;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
    String message,
    int statusCode,
    LocalDateTime timestamp
) {}
