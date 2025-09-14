package com.agenda.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "contato")
@Data
public class Contato implements _Contato{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    private String nome;
    private String telefone;

}
