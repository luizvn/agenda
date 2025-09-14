package com.agenda.backend.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.ToString;

@DiscriminatorValue("LIST")
@Entity
@ToString
public class AgendaList extends Agenda {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> contatos = new ArrayList<>();

    @Override
    public Collection<Contato> getContatos() {
        return contatos;
    }

    @Override
    public boolean addContato(Contato contato) {
        if (getContato(contato.getTelefone()) != null) {
            return false;
        }
        return contatos.add(contato);
    }

    @Override
    public boolean removeContato(Contato contato) {
        return contatos.remove(contato);
    }

    @Override
    public Contato getContato(String telefone) {
        for (Contato contato : contatos) {
            if (contato.getTelefone().equals(telefone)) {
                return contato;
            }
        }
        return null;
    }
    
}
