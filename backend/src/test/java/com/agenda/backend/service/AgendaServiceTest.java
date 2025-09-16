package com.agenda.backend.service;

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
import com.agenda.backend.entity.ContatoImpl;
import com.agenda.backend.repository.AgendaRepository; 
import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test; 
import org.junit.jupiter.api.extension.ExtendWith; 
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension; 
import java.util.*; 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AgendaServiceTest {
    
    @Mock 
    private AgendaRepository agendaRepository;

    @InjectMocks 
    private AgendaService agendaService; 
    
    @BeforeEach 
    void setUp() { 
        MockitoAnnotations.openMocks(this); 
    } 

    private CreateAgendaDTO mockDto(String nome, String tipo) {
        CreateAgendaDTO dto = mock(CreateAgendaDTO.class);
        when(dto.nome()).thenReturn(nome);
        when(dto.tipo()).thenReturn(tipo);
        return dto;
    }
    
    @Test 
    void criarAgendaList() { 
        CreateAgendaDTO dto = mockDto("MinhaAgenda", "LIST");

        when(agendaRepository.findByNome("MinhaAgenda")).thenReturn(Optional.empty()); 

        AgendaList agendaList = new AgendaList(); 
        agendaList.setNome("MinhaAgenda"); 
        agendaList.setId(1L); 
     
        AgendaFactory factory = AgendaFactory.getInstance(); 
        AgendaFactory spyFactory = spy(factory); 

        try (MockedStatic<AgendaFactory> factoryMockedStatic = mockStatic(AgendaFactory.class)) { 
            factoryMockedStatic.when(AgendaFactory::getInstance).thenReturn(spyFactory); 

            doReturn(agendaList).when(spyFactory).createAgenda(AgendaFactory.AGENDALIST); 
            when(agendaRepository.save(any(Agenda.class))).thenReturn(agendaList); 

            AgendaResponse response = agendaService.createAgenda(dto); 

            assertTrue(response instanceof AgendaListResponseDTO); 

            AgendaListResponseDTO listResponse = (AgendaListResponseDTO) response; 

            assertEquals("MinhaAgenda", listResponse.nome()); 
            assertEquals(1L, listResponse.id()); 
            assertNotNull(listResponse.contatos()); 
        } 
    } 
    
    @Test 
    void criarAgendaMap() { 
        CreateAgendaDTO dto = mockDto("MinhaAgendaMap", "MAP");

        when(agendaRepository.findByNome("MinhaAgendaMap")).thenReturn(Optional.empty()); 

        AgendaMap agendaMap = new AgendaMap(); 
        agendaMap.setNome("MinhaAgendaMap"); 
        agendaMap.setId(2L); 

        AgendaFactory factory = AgendaFactory.getInstance(); 
        AgendaFactory spyFactory = spy(factory);

        try (MockedStatic<AgendaFactory> factoryMockedStatic = mockStatic(AgendaFactory.class)) { 
            factoryMockedStatic.when(AgendaFactory::getInstance).thenReturn(spyFactory); 

            doReturn(agendaMap).when(spyFactory).createAgenda(AgendaFactory.AGENDAMAP); 
            when(agendaRepository.save(any(Agenda.class))).thenReturn(agendaMap); 

            AgendaResponse response = agendaService.createAgenda(dto); 

            assertTrue(response instanceof AgendaMapResponseDTO); 

            AgendaMapResponseDTO mapResponse = (AgendaMapResponseDTO) response; 

            assertEquals("MinhaAgendaMap", mapResponse.nome()); 
            assertEquals(2L, mapResponse.id()); 
            assertNotNull(mapResponse.contatos()); 
        } 
    } 
    
    @Test 
    void erroCriarAgendaDuplicada() { 
        CreateAgendaDTO dto = mockDto("Agenda Duplicada", "LIST");

        when(agendaRepository.findByNome("Agenda Duplicada")).thenReturn(Optional.of(new AgendaList())); 

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> { 
            agendaService.createAgenda(dto); 
        }); 

        assertTrue(ex.getMessage().contains("Já existe uma agenda com o nome: Agenda Duplicada")); 
    } 

    @Test 
    void erroCriarAgendaTipoInvalido() { 
        CreateAgendaDTO dto = mockDto("Agenda Invalida", "INVALIDO");

        when(agendaRepository.findByNome("Agenda Invalida")).thenReturn(Optional.empty()); 

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> { 
            agendaService.createAgenda(dto); 
        }); 

        assertTrue(ex.getMessage().contains("Tipo de agenda inválido")); 
    } 

    @Test 
    void converterAgendaParaList() throws Exception { 
        Contato contato = new ContatoImpl(); 
        contato.setId(10L); 
        contato.setNome("Luiza"); 
        contato.setTelefone("71993193383");

        AgendaList agendaList = new AgendaList(); 
        agendaList.setId(5L); 
        agendaList.setNome("AgendaList"); 
        agendaList.addContato(contato);

        var method = AgendaService.class.getDeclaredMethod("mapearParaAgendaListDTO", AgendaList.class); 
        method.setAccessible(true); 

        AgendaListResponseDTO dto = (AgendaListResponseDTO) method.invoke(agendaService, agendaList);

        assertEquals(5L, dto.id()); 
        assertEquals("AgendaList", dto.nome()); 
        assertEquals(1, dto.contatos().size()); 

        ContatoResponseDTO contatoDTO = dto.contatos().get(0); 

        assertEquals(10L, contatoDTO.id()); 
        assertEquals("Luiza", contatoDTO.nome()); 
        assertEquals("71993193383", contatoDTO.telefone()); 
    } 
    
    @Test 
    void converterAgendaParaMap() throws Exception { 
        Contato contato = new ContatoImpl(); 
        contato.setId(20L); 
        contato.setNome("Luiz"); 
        contato.setTelefone("71912345678"); 

        AgendaMap agendaMap = new AgendaMap(); 
        agendaMap.setId(6L); 
        agendaMap.setNome("AgendaMap"); 
        agendaMap.addContato(contato); 

        var method = AgendaService.class.getDeclaredMethod("mapearParaAgendaMapDTO", AgendaMap.class); 
        method.setAccessible(true); 

        AgendaMapResponseDTO dto = (AgendaMapResponseDTO) method.invoke(agendaService, agendaMap); 

        assertEquals(6L, dto.id()); 
        assertEquals("AgendaMap", dto.nome()); 
        assertEquals(1, dto.contatos().size()); 

        ContatoResponseDTO contatoDTO = dto.contatos().get("71912345678"); 

        assertNotNull(contatoDTO); 
        assertEquals(20L, contatoDTO.id()); 
        assertEquals("Luiz", contatoDTO.nome()); 
        assertEquals("71912345678", contatoDTO.telefone()); 
    }

    @Test
    void converterAgendaSemContatosParaList() throws Exception {
        AgendaList agendaList = new AgendaList();
        agendaList.setId(99L);
        agendaList.setNome("Agenda Vazia");

        var method = AgendaService.class.getDeclaredMethod("mapearParaAgendaListDTO", AgendaList.class);
        method.setAccessible(true);

        AgendaListResponseDTO dto = (AgendaListResponseDTO) method.invoke(agendaService, agendaList);

        assertEquals(99L, dto.id());
        assertEquals("Agenda Vazia", dto.nome());
        assertTrue(dto.contatos().isEmpty());
    }

    @Test
    void converterAgendaSemContatosParaMap() throws Exception {
        AgendaMap agendaMap = new AgendaMap();
        agendaMap.setId(100L);
        agendaMap.setNome("Agenda Map Vazia");

        var method = AgendaService.class.getDeclaredMethod("mapearParaAgendaMapDTO", AgendaMap.class);
        method.setAccessible(true);

        AgendaMapResponseDTO dto = (AgendaMapResponseDTO) method.invoke(agendaService, agendaMap);

        assertEquals(100L, dto.id());
        assertEquals("Agenda Map Vazia", dto.nome());
        assertTrue(dto.contatos().isEmpty());
    }
}
