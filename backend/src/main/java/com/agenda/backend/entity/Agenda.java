package com.agenda.backend.entity;

import java.util.Collection;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_AGENDA")
@Entity
public abstract class Agenda {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    public abstract Collection<ContatoImpl> getListaAgenda();
    public abstract boolean addContato(ContatoImpl contato);
    public abstract boolean removeContato(ContatoImpl contato);
    public abstract Contato getContato(String telefone);

}
