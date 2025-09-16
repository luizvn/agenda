package com.agenda.backend.entity;

public interface Contato {

    String getNome();
    String getTelefone();
    Long getId();
    void setNome(String nome);
    void setTelefone(String telefone);
    void setId(Long id);
    
}
