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

    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ContatoImpl.class)
    private List<Contato> contatos = new ArrayList<>();


    @Override
    public Collection<Contato> getContatos() {
        return contatos;
    }

    @Override
    public boolean addContato(Contato contato) {
        int index = localizaContato(contato.getTelefone());

        if (index != -1) return false;

        contatos.add(contato);
        contato.setAgenda(this);
        return true;
    }

    @Override
    public boolean removeContato(Contato contato) {
        return contatos.remove(contato);
    }

    @Override
    public Contato getContato(String telefone) {
        int index = localizaContato(telefone);

        if (index == -1) return null;
        return contatos.get(index);
    }

    public int localizaContato(String telefone) {
        for (int i = 0; i < contatos.size(); i++) {
            if (contatos.get(i).getTelefone().equals(telefone)) {
                return i;
            }
        }
        return -1;
    }
    
}
