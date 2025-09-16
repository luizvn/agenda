package com.agenda.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContatoImplTest {

    @Test
    void setNome() {
        ContatoImpl contato = new ContatoImpl();
        contato.setNome("Luiza");
        assertEquals("Luiza", contato.getNome());
    }

    @Test
    void setNomeNull() {
        ContatoImpl contato = new ContatoImpl();
        Exception ex = assertThrows(IllegalArgumentException.class, () -> contato.setNome(null));
        assertEquals("Nome não pode ser nulo ou vazio", ex.getMessage());
    }

    @Test
    void setNomeVazio() {
        ContatoImpl contato = new ContatoImpl();
        Exception ex = assertThrows(IllegalArgumentException.class, () -> contato.setNome("   "));
        assertEquals("Nome não pode ser nulo ou vazio", ex.getMessage());
    }

    @Test
    void setTelefone() {
        ContatoImpl contato = new ContatoImpl();
        contato.setTelefone("11999999999");
        assertEquals("11999999999", contato.getTelefone());
    }

    @Test
    void setTelefoneNull() {
        ContatoImpl contato = new ContatoImpl();
        Exception ex = assertThrows(IllegalArgumentException.class, () -> contato.setTelefone(null));
        assertEquals("Telefone não pode ser nulo ou vazio", ex.getMessage());
    }

    @Test
    void setTelefoneVazio() {
        ContatoImpl contato = new ContatoImpl();
        Exception ex = assertThrows(IllegalArgumentException.class, () -> contato.setTelefone("   "));
        assertEquals("Telefone não pode ser nulo ou vazio", ex.getMessage());
    }

    @Test
    void setTelefoneNaoNumerico() {
        ContatoImpl contato = new ContatoImpl();
        Exception ex = assertThrows(IllegalArgumentException.class, () -> contato.setTelefone("11A9999999"));
        assertEquals("Telefone deve conter apenas números", ex.getMessage());
    }

    @Test
    void setTelefoneMuitoCurto() {
        ContatoImpl contato = new ContatoImpl();
        Exception ex = assertThrows(IllegalArgumentException.class, () -> contato.setTelefone("123456789"));
        assertEquals("Telefone deve conter entre 10 e 11 dígitos", ex.getMessage());
    }

    @Test
    void setTelefoneMuitoLongo() {
        ContatoImpl contato = new ContatoImpl();
        Exception ex = assertThrows(IllegalArgumentException.class, () -> contato.setTelefone("123456789012"));
        assertEquals("Telefone deve conter entre 10 e 11 dígitos", ex.getMessage());
    }

    @Test
    void setId() {
        ContatoImpl contato = new ContatoImpl();
        contato.setId(42L);
        assertEquals(42L, contato.getId());
    }
}