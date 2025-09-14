package com.agenda.backend.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

public class AgendaMap extends Agenda{

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<String, Contato> contatos = new HashMap<>();

    @Override
    public Collection<Contato> getContatos() {
        return contatos.values();
    }

    @Override
    public boolean addContato(Contato contato) {
        if (contatos.containsKey(contato.getTelefone())) {
            return false;
        }
        contatos.put(contato.getTelefone(), contato);
        return true;
    }

    @Override
    public boolean removeContato(Contato contato) {
        if(contato != null && contatos.containsKey(contato.getTelefone())){
            contatos.remove(contato.getTelefone());
            return true;
        }
        return false;
    }

    @Override
    public Contato getContato(String telefone) {
        return contatos.get(telefone);
    }
    
}
