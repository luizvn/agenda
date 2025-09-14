package com.agenda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agenda.backend.entity.Agenda;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long>{
    
}
