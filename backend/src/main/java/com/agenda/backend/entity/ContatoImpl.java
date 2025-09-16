package com.agenda.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "contatos")
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ContatoImpl implements Contato{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    private String nome;
    
    @Column(unique=true)
    private String telefone;

    @Override
    public void setNome(String nome) {
        if(nome == null || nome.isBlank()){
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.nome = nome;
    }

    @Override
    public void setTelefone(String telefone) {
        if(telefone == null || telefone.isBlank()){
            throw new IllegalArgumentException("Telefone não pode ser nulo ou vazio");
        }

        if(!telefone.matches("\\d+")){
            throw new IllegalArgumentException("Telefone deve conter apenas números");
        }

        if(telefone.length() < 10 || telefone.length() > 11){
            throw new IllegalArgumentException("Telefone deve conter entre 10 e 11 dígitos");
        }

        this.telefone = telefone;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
