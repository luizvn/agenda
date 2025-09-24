package com.agenda.backend.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agenda.backend.dto.AgendaListResponseDTO;
import com.agenda.backend.dto.AgendaMapResponseDTO;
import com.agenda.backend.dto.AgendaResponseDTO;
import com.agenda.backend.dto.AgendaTypedResponse;
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

import jakarta.persistence.EntityManager;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public AgendaTypedResponse createAgenda(CreateAgendaDTO requestDto) {
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
    public Collection<AgendaResponseDTO> getAgendas(String nome) {
        
        Collection<Agenda> agendasEncontradas;
        if(nome != null && !nome.isBlank()){
            agendasEncontradas = agendaRepository.findByNomeContaining(nome);
        }else{
            Collection<Agenda> allAgendas = agendaRepository.findAll();
            agendasEncontradas = allAgendas;
        }

        return agendasEncontradas.stream()
                .map(this::mapearAgendaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AgendaResponseDTO getAgendaById(Long id) {
        Agenda agenda = agendaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Agenda não encontrada."));

        return mapearAgendaResponseDTO(agenda);
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

    @Transactional
    public void removeContatoFromAgenda(Long agendaId, Long contatoId) {
        Agenda agenda = agendaRepository.findById(agendaId)
            .orElseThrow(() -> new RuntimeException("Agenda não encontrada."));

        Contato contato = agenda.getContatos().stream()
            .filter(c -> c.getId().equals(contatoId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Contato não encontrado na agenda."));

        boolean contatoFoiRemovido = agenda.removeContato(contato);
        if(!contatoFoiRemovido) {
            throw new IllegalStateException("Falha ao remover o contato da agenda.");
        }

        agendaRepository.save(agenda);
    }

    @Transactional(readOnly = true)
    public Collection<ContatoResponseDTO> getContatos(Long agendaId, String telefone, String nome) {
        Agenda agenda = agendaRepository.findById(agendaId)
            .orElseThrow(() -> new RuntimeException("Agenda não encontrada."));

        Collection<Contato> contatosEncontrados;

        if(telefone != null && !telefone.isBlank()){
            contatosEncontrados = agenda.getContatos().stream()
                .filter(contato -> contato.getTelefone().contains(telefone))
                .collect(Collectors.toList());
        }else if(nome != null && !nome.isBlank()){
            contatosEncontrados = agenda.getContatos().stream()
                .filter(contato -> contato.getNome().toLowerCase().startsWith(nome.toLowerCase()))
                .collect(Collectors.toList());
        }else{
            contatosEncontrados = agenda.getContatos();
        }

        return contatosEncontrados.stream()
            .map(this::mapearContatoResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public AgendaTypedResponse convertAgendaType(Long id) {
        Agenda agendaOriginal = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda não encontrada."));

        String novoTipo = (agendaOriginal instanceof AgendaList) ? "MAP" : "LIST";

        agendaRepository.updateAgendaType(id, novoTipo);
        entityManager.clear();

        Agenda agendaConvertida = agendaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Falha ao buscar agenda após conversão."));

        return mapearAgendaResponse(agendaConvertida);
    }

    @Transactional
    public void removeContatosByNome(Long agendaId, String nome) {
        Agenda agenda = agendaRepository.findById(agendaId)
            .orElseThrow(() -> new RuntimeException("Agenda não encontrada."));

        List<Contato> contatosParaRemover = agenda.getContatos().stream()
            .filter(contato -> contato.getNome().toLowerCase().startsWith(nome.toLowerCase()))
            .collect(Collectors.toList());

        if (contatosParaRemover.isEmpty()) {
            throw new RuntimeException("Nenhum contato encontrado com o nome especificado na agenda.");
        }

        contatosParaRemover.forEach(agenda::removeContato);
        agendaRepository.save(agenda);
    }


    private AgendaResponseDTO mapearAgendaResponseDTO(Agenda agenda) {
        return new AgendaResponseDTO(agenda.getId(), agenda.getNome());
    }

    private ContatoResponseDTO mapearContatoResponseDTO(Contato contato) {
        return new ContatoResponseDTO(contato.getId(), contato.getNome(), contato.getTelefone());
    }
        
    private AgendaTypedResponse mapearAgendaResponse(Agenda agenda) {
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
