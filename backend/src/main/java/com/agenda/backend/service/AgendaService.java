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
import com.agenda.backend.dto.ContatoRequestDTO;
import com.agenda.backend.dto.ContatoResponseDTO;
import com.agenda.backend.dto.CreateAgendaDTO;
import com.agenda.backend.entity.Agenda;
import com.agenda.backend.entity.AgendaFactory;
import com.agenda.backend.entity.AgendaList;
import com.agenda.backend.entity.AgendaMap;
import com.agenda.backend.entity.Contato;
import com.agenda.backend.entity.ContatoImpl;
import com.agenda.backend.repository.AgendaRepository;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Transactional
    public AgendaResponse createAgenda(CreateAgendaDTO requestDto) {
        if(agendaRepository.findByNome(requestDto.nome()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma agenda com o nome: " + requestDto.nome());
        }

        AgendaFactory factory = AgendaFactory.getInstance();

        String tipoAgenda = mapearTipo(requestDto.tipo()); 
        Agenda novaAgenda = factory.createAgenda(tipoAgenda);
        novaAgenda.setNome(requestDto.nome());

        Agenda agendaSalva = agendaRepository.save(novaAgenda);
        return mapearAgendaResponse(agendaSalva);
    }

    @Transactional(readOnly = true)
    public List<AgendaResponse> getAllAgendas() {
        List<Agenda> agendas = agendaRepository.findAll();
        return agendas.stream()
                .map(this::mapearAgendaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AgendaResponse getAgendaById(Long id) {
        Agenda agenda = agendaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Agenda não encontrada."));

        return mapearAgendaResponse(agenda);
    }

    @Transactional
    public void deleteAgenda(Long id) {
        if(!agendaRepository.existsById(id)) {
            throw new RuntimeException("Agenda não encontrada.");
        }
        agendaRepository.deleteById(id);
    }

    @Transactional
    public ContatoResponseDTO addContatoToAgenda(Long agendaId, ContatoRequestDTO contatoRequest) {
        Agenda agenda = agendaRepository.findById(agendaId)
            .orElseThrow(() -> new RuntimeException("Agenda não encontrada."));

        boolean telefoneJaExiste = agenda.getContatos().stream()
            .anyMatch(contato -> contato.getTelefone().equals(contatoRequest.telefone()));

        if (telefoneJaExiste) {
            throw new IllegalArgumentException("Já existe um contato com este telefone na agenda.");
        }

        Contato novoContato = new ContatoImpl();
        novoContato.setNome(contatoRequest.nome());
        novoContato.setTelefone(contatoRequest.telefone());

        agenda.addContato(novoContato);

        Agenda agendaSalva = agendaRepository.save(agenda);

        Contato contatoSalvo = agendaSalva.getContato(novoContato.getTelefone());
        return mapearContatoResponseDTO(contatoSalvo);
    }

    private ContatoResponseDTO mapearContatoResponseDTO(Contato contato) {
        return new ContatoResponseDTO(contato.getId(), contato.getNome(), contato.getTelefone());
    }
        
    private AgendaResponse mapearAgendaResponse(Agenda agenda) {
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
