package com.agenda.backend.controller;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.agenda.backend.dto.AgendaResponseDTO;
import com.agenda.backend.dto.AgendaTypedResponse;
import com.agenda.backend.dto.ContatoRequestDTO;
import com.agenda.backend.dto.ContatoResponseDTO;
import com.agenda.backend.dto.CreateAgendaDTO;
import com.agenda.backend.service.AgendaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/agendas")
public class AgendaController {
    
    @Autowired
    private AgendaService agendaService;

    @PostMapping
    public ResponseEntity<AgendaTypedResponse> createAgenda(@RequestBody @Valid CreateAgendaDTO requestDto) {
        AgendaTypedResponse savedAgenda = agendaService.createAgenda(requestDto);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedAgenda.id())
            .toUri(); 

        return ResponseEntity.created(location).body(savedAgenda);
    }

    @GetMapping
    public ResponseEntity<Collection<AgendaResponseDTO>> getAllAgendas() {
        Collection<AgendaResponseDTO> agendas = agendaService.getAllAgendas();

        return ResponseEntity.ok(agendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> getAgendaById(@PathVariable @Valid Long id) {
        AgendaResponseDTO agenda = agendaService.getAgendaById(id);

        return ResponseEntity.ok(agenda);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgenda(@PathVariable Long id) {
        agendaService.deleteAgenda(id);
        
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{agendaId}/contatos")
    public ResponseEntity<ContatoResponseDTO> addContatoToAgenda(@PathVariable Long agendaId, @RequestBody @Valid ContatoRequestDTO contatoRequest) {
        ContatoResponseDTO savedContato = agendaService.addContatoToAgenda(agendaId, contatoRequest);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedContato.id())
            .toUri(); 

        return ResponseEntity.created(location).body(savedContato);
    }

    @DeleteMapping("/{agendaId}/contatos/{contatoId}")
    public ResponseEntity<Void> removeContatoFromAgenda(@PathVariable Long agendaId, @PathVariable Long contatoId) {
        agendaService.removeContatoFromAgenda(agendaId, contatoId);
        
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{agendaId}/contatos")
    public ResponseEntity<Collection<ContatoResponseDTO>> getContatos(@PathVariable Long agendaId, @RequestParam(required = false) String telefone) {
        Collection<ContatoResponseDTO> contatos = agendaService.getContatos(agendaId, telefone);

        return ResponseEntity.ok(contatos);
    }

}
