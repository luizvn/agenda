package com.agenda.backend.service;

import com.agenda.backend.dto.AgendaListResponseDTO; 
import com.agenda.backend.dto.AgendaMapResponseDTO;
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
    private EntityManager entityManager;
    
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

            AgendaTypedResponse response = agendaService.createAgenda(dto); 

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

            AgendaTypedResponse response = agendaService.createAgenda(dto); 

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
    void converterAgendaParaListDTO() throws Exception { 
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
    void converterAgendaParaMapDTO() throws Exception { 
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
    void converterAgendaSemContatosParaListDTO() throws Exception {
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
    void converterAgendaSemContatosParaMapDTO() throws Exception {
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

    @Test
    void retornarTodasAgendas() {
        AgendaList agenda1 = new AgendaList();
        agenda1.setId(1L);
        agenda1.setNome("Agenda 1");

        AgendaMap agenda2 = new AgendaMap();
        agenda2.setId(2L);
        agenda2.setNome("Agenda 2");

        when(agendaRepository.findAll()).thenReturn(List.of(agenda1, agenda2));

        var agendas = agendaService.getAgendas(null);

        assertEquals(2, agendas.size());
        assertTrue(agendas.stream().anyMatch(a -> a.nome().equals("Agenda 1")));
        assertTrue(agendas.stream().anyMatch(a -> a.nome().equals("Agenda 2")));
    }

    @Test
    void retornarAgendaPorNome() {
        AgendaList agenda1 = new AgendaList();
        agenda1.setId(1L);
        agenda1.setNome("Agenda do Luiz");

        AgendaMap agenda2 = new AgendaMap();
        agenda2.setId(2L);
        agenda2.setNome("Agenda 2");

        when(agendaRepository.findByNomeContaining("Agenda do Luiz")).thenReturn(List.of(agenda1));

        var agendas = agendaService.getAgendas("Agenda do Luiz");

        assertEquals(1, agendas.size());
        assertTrue(agendas.stream().anyMatch(a -> a.nome().equals("Agenda do Luiz")));
    }

    @Test
    void retornarAgendaPorId() {
        AgendaList agenda = new AgendaList();
        agenda.setId(10L);
        agenda.setNome("Agenda Teste");

        when(agendaRepository.findById(10L)).thenReturn(Optional.of(agenda));

        var response = agendaService.getAgendaById(10L);

        assertEquals("Agenda Teste", response.nome());
        assertEquals(10L, response.id());
    }

    @Test
    void erroAgendaNaoEncontradaPorId() {
        when(agendaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            agendaService.getAgendaById(99L);
        });

        assertTrue(ex.getMessage().contains("Agenda não encontrada"));
    }

    @Test
    void deletarAgenda() {
        when(agendaRepository.existsById(5L)).thenReturn(true);

        agendaService.deleteAgenda(5L);

        verify(agendaRepository, times(1)).deleteById(5L);
    }

    @Test
    void erroDeletarAgendaNaoExistente() {
        when(agendaRepository.existsById(8L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            agendaService.deleteAgenda(8L);
        });

        assertTrue(ex.getMessage().contains("Agenda não encontrada"));
    }

    @Test
    void adicionarContatoNaAgenda() {
        AgendaList agenda = new AgendaList();
        agenda.setId(1L);

        ContatoRequestDTO request = new ContatoRequestDTO("Luiza", "71993193383");

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any(Agenda.class))).thenAnswer(inv -> inv.getArgument(0));

        var response = agendaService.addContatoToAgenda(1L, request);

        assertEquals("Luiza", response.nome());
        assertEquals("71993193383", response.telefone());
    }

    @Test
    void erroAdicionarContatoComTelefoneDuplicado() {
        Contato contatoExistente = new ContatoImpl();
        contatoExistente.setId(2L);
        contatoExistente.setNome("Luiz");
        contatoExistente.setTelefone("71993193383");

        AgendaList agenda = new AgendaList();
        agenda.setId(1L);
        agenda.addContato(contatoExistente);

        ContatoRequestDTO request = new ContatoRequestDTO("Luiz", "71993193383");

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            agendaService.addContatoToAgenda(1L, request);
        });

        assertTrue(ex.getMessage().contains("Já existe um contato com este telefone"));
    }

    @Test
    void removerContatoDaAgenda() {
        Contato contato = new ContatoImpl();
        contato.setId(3L);
        contato.setNome("Caio");
        contato.setTelefone("71993193383");

        AgendaList agenda = new AgendaList();
        agenda.setId(1L);
        agenda.addContato(contato);

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);

        agendaService.removeContatoFromAgenda(1L, 3L);

        assertTrue(agenda.getContatos().isEmpty());
    }

    @Test
    void erroRemoverContatoNaoExistente() {
        AgendaList agenda = new AgendaList();
        agenda.setId(1L);

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            agendaService.removeContatoFromAgenda(1L, 10L);
        });

        assertTrue(ex.getMessage().contains("Contato não encontrado"));
    }

    @Test
    void buscarContatosPorTelefone() {
        Contato contato1 = new ContatoImpl();
        contato1.setId(1L);
        contato1.setNome("Gabriel");
        contato1.setTelefone("71993193383");

        Contato contato2 = new ContatoImpl();
        contato2.setId(2L);
        contato2.setNome("Iuri");
        contato2.setTelefone("71993193384");

        AgendaList agenda = new AgendaList();
        agenda.setId(1L);
        agenda.addContato(contato1);
        agenda.addContato(contato2);

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        var contatos = agendaService.getContatos(1L, "71993193383", null);

        assertEquals(1, contatos.size());
        assertEquals("Gabriel", contatos.iterator().next().nome());
    }

    @Test
    void buscarTodosContatosSemFiltro() {
        Contato contato = new ContatoImpl();
        contato.setId(1L);
        contato.setNome("Tatiana");
        contato.setTelefone("71993193383");

        AgendaList agenda = new AgendaList();
        agenda.setId(1L);
        agenda.addContato(contato);

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        var contatos = agendaService.getContatos(1L, null, null);

        assertEquals(1, contatos.size());
        assertEquals("Tatiana", contatos.iterator().next().nome());
    }

    @Test
    void converterAgendaDeListParaMap() {
        AgendaList agendaOriginal = new AgendaList();
        agendaOriginal.setId(1L);

        AgendaMap agendaConvertida = new AgendaMap();
        agendaConvertida.setId(1L);

        when(agendaRepository.findById(1L))
                .thenReturn(Optional.of(agendaOriginal))
                .thenReturn(Optional.of(agendaConvertida));

        doNothing().when(agendaRepository).updateAgendaType(1L, "MAP");

        var response = agendaService.convertAgendaType(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
    }

    @Test
    void erroConverterAgendaNaoExistente() {
        when(agendaRepository.findById(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            agendaService.convertAgendaType(5L);
        });

        assertTrue(ex.getMessage().contains("Agenda não encontrada"));
    }
}
