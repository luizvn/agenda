package com.agenda.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendaMapTest {

    private AgendaMap agendaMap;
    private Contato contato1;
    private Contato contato2;

    @BeforeEach
    void setUp() {
        agendaMap = new AgendaMap();

        contato1 = mock(Contato.class);
        when(contato1.getTelefone()).thenReturn("71993193383");

        contato2 = mock(Contato.class);
        when(contato2.getTelefone()).thenReturn("71912345678");
    }

    @Test
    void addContato() {
        boolean result = agendaMap.addContato(contato1);
        assertTrue(result);
        assertEquals(1, agendaMap.getContatos().size());
        assertTrue(agendaMap.getContatos().contains(contato1));
    }

    @Test
    void addContatoDuplicado() {
        agendaMap.addContato(contato1);
        boolean result = agendaMap.addContato(contato1);
        assertFalse(result);
        assertEquals(1, agendaMap.getContatos().size());
    }

    @Test
    void removeContato() {
        agendaMap.addContato(contato1);
        boolean result = agendaMap.removeContato(contato1);
        assertTrue(result);
        assertEquals(0, agendaMap.getContatos().size());
    }

    @Test
    void removeContatoInexistente() {
        boolean result = agendaMap.removeContato(contato1);
        assertFalse(result);
    }

    @Test
    void getContato() {
        agendaMap.addContato(contato1);
        Contato found = agendaMap.getContato("71993193383");
        assertEquals(contato1, found);
    }

    @Test
    void getContatoInexistente() {
        Contato found = agendaMap.getContato("99999");
        assertNull(found);
    }

    @Test
    void getContatos() {
        agendaMap.addContato(contato1);
        agendaMap.addContato(contato2);
        Collection<Contato> contatos = agendaMap.getContatos();
        assertEquals(2, contatos.size());
        assertTrue(contatos.contains(contato1));
        assertTrue(contatos.contains(contato2));
    }
}