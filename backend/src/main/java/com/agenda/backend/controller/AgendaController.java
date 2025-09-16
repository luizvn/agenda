package com.agenda.backend.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.agenda.backend.dto.AgendaResponse;
import com.agenda.backend.dto.CreateAgendaDTO;
import com.agenda.backend.service.AgendaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/agendas")
public class AgendaController {
    
    @Autowired
    private AgendaService agendaService;

    @PostMapping
    public ResponseEntity<AgendaResponse> createAgenda(@RequestBody @Valid CreateAgendaDTO requestDto) {
        AgendaResponse savedAgenda = agendaService.createAgenda(requestDto);

         URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedAgenda.id())
            .toUri(); 
            
        return ResponseEntity.created(location).body(savedAgenda);
    }
}
