package com.agenda.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.agenda.backend.entity.Agenda;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long>{
    
    Optional<Agenda> findByNome(String nome);

    @Modifying
    @Query(value = "UPDATE agendas SET tipo_agenda = :novoTipo WHERE id = :id", nativeQuery = true)
    void updateAgendaType(@Param("id") Long id, @Param("novoTipo") String novoTipo);
    
}
