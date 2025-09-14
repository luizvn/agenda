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
    private List<Contato> listaContato = new ArrayList<>();

    @Override
    public Collection<Contato> getListaAgenda() {
        return listaContato;
    }

    @Override
    public boolean addContato(Contato contato) {
        if (getContato(contato.getTelefone()) != null) {
            return false;
        }
        return listaContato.add(contato);
    }

    @Override
    public boolean removeContato(Contato contato) {
        return listaContato.remove(contato);
    }

    @Override
    public _Contato getContato(String telefone) {
        for (_Contato contato : listaContato) {
            if (contato.getTelefone().equals(telefone)) {
                return contato;
            }
        }
        return null;
    }
    
}
