package com.agenda.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendaListTest {

    private AgendaList agendaList;
    private Contato contato1;
    private Contato contato2;

    @BeforeEach
    void setUp() {
        agendaList = new AgendaList();

        contato1 = mock(Contato.class);
        when(contato1.getTelefone()).thenReturn("71993193383");

        contato2 = mock(Contato.class);
        when(contato2.getTelefone()).thenReturn("71912345678");
    }

    @Test
    void addContato() {
        boolean added = agendaList.addContato(contato1);
        assertTrue(added);
        assertEquals(1, agendaList.getContatos().size());
        assertTrue(agendaList.getContatos().contains(contato1));
    }

    @Test
    void addContatoDuplicado() {
        agendaList.addContato(contato1);
        Contato duplicate = mock(Contato.class);
        when(duplicate.getTelefone()).thenReturn("71993193383");
        boolean added = agendaList.addContato(duplicate);
        assertFalse(added);
        assertEquals(1, agendaList.getContatos().size());
    }

    @Test
    void removeContato() {
        agendaList.addContato(contato1);
        boolean removed = agendaList.removeContato(contato1);
        assertTrue(removed);
        assertTrue(agendaList.getContatos().isEmpty());
    }

    @Test
    void removeContatoInexistente() {
        boolean removed = agendaList.removeContato(contato1);
        assertFalse(removed);
    }

    @Test
    void getContato() {
        agendaList.addContato(contato1);
        Contato found = agendaList.getContato("71993193383");
        assertNotNull(found);
        assertEquals(contato1, found);
    }

    @Test
    void getContatoInexistente() {
        Contato found = agendaList.getContato("99999");
        assertNull(found);
    }

    @Test
    void localizaContato() {
        agendaList.addContato(contato1);
        agendaList.addContato(contato2);
        int index = agendaList.localizaContato("71912345678");
        assertEquals(1, index);
    }

    @Test
    void localizaContatoInexistente() {
        int index = agendaList.localizaContato("00000");
        assertEquals(-1, index);
    }

    @Test
    void getContatos() {
        agendaList.addContato(contato1);
        agendaList.addContato(contato2);
        Collection<Contato> contatos = agendaList.getContatos();
        assertEquals(2, contatos.size());
        assertTrue(contatos.contains(contato1));
        assertTrue(contatos.contains(contato2));
    }
}