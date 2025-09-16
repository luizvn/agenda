package com.agenda.backend.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agenda.backend.dto.AgendaListResponseDTO;
import com.agenda.backend.dto.AgendaMapResponseDTO;
import com.agenda.backend.dto.AgendaResponse;
import com.agenda.backend.dto.ContatoResponseDTO;
import com.agenda.backend.dto.CreateAgendaDTO;
import com.agenda.backend.entity.Agenda;
import com.agenda.backend.entity.AgendaFactory;
import com.agenda.backend.entity.AgendaList;
import com.agenda.backend.entity.AgendaMap;
import com.agenda.backend.entity.Contato;
import com.agenda.backend.repository.AgendaRepository;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Transactional
    public AgendaResponse createAgenda(CreateAgendaDTO requestDto) {
        AgendaFactory factory = AgendaFactory.getInstance();

        String tipoAgenda = mapearTipo(requestDto.tipo()); 
        Agenda novaAgenda = factory.createAgenda(tipoAgenda);

        novaAgenda.setNome(requestDto.nome());
        Agenda agendaSalva = agendaRepository.save(novaAgenda);
        return mapearResponse(agendaSalva);
    }

    private AgendaResponse mapearResponse(Agenda agenda) {
        if (agenda instanceof AgendaList) {
            return mapearParaAgendaListDTO((AgendaList) agenda);
        } else if (agenda instanceof AgendaMap) {
            return mapearParaAgendaMapDTO((AgendaMap) agenda);
        }
        throw new IllegalArgumentException("Tipo de entidade Agenda não suportado para mapeamento de resposta: " + agenda.getClass().getName());
    }

    private AgendaListResponseDTO mapearParaAgendaListDTO(AgendaList agendaList) {
        List<ContatoResponseDTO> contatosDTO = agendaList.getContatos().stream()
                .map(contato -> new ContatoResponseDTO(contato.getId(), contato.getNome(), contato.getTelefone()))
                .collect(Collectors.toList());

        return new AgendaListResponseDTO(agendaList.getId(), agendaList.getNome(), contatosDTO);
    }

    private AgendaMapResponseDTO mapearParaAgendaMapDTO(AgendaMap agendaMap) {
        Map<String, ContatoResponseDTO> contatosDTO = agendaMap.getContatos().stream()
                .collect(Collectors.toMap(
                    Contato::getTelefone,
                    contato -> new ContatoResponseDTO(contato.getId(), contato.getNome(), contato.getTelefone())
                ));

        return new AgendaMapResponseDTO(agendaMap.getId(), agendaMap.getNome(), contatosDTO);
    }

    private String mapearTipo(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "LIST" -> AgendaFactory.AGENDALIST;
            case "MAP" -> AgendaFactory.AGENDAMAP;
            default -> throw new IllegalArgumentException("Tipo de agenda inválido: " + tipo);
        };
    }
}
