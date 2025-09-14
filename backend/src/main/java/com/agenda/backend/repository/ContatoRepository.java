package com.agenda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agenda.backend.entity.Contato;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long>{

}
