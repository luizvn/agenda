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
    private List<ContatoImpl> listaContato = new ArrayList<>();

    @Override
    public Collection<ContatoImpl> getListaAgenda() {
        return listaContato;
    }

    @Override
    public boolean addContato(ContatoImpl contato) {
        if (getContato(contato.getTelefone()) != null) {
            return false;
        }
        return listaContato.add(contato);
    }

    @Override
    public boolean removeContato(ContatoImpl contato) {
        return listaContato.remove(contato);
    }

    @Override
    public Contato getContato(String telefone) {
        for (Contato contato : listaContato) {
            if (contato.getTelefone().equals(telefone)) {
                return contato;
            }
        }
        return null;
    }
    
}
