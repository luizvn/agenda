package com.agenda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agenda.backend.entity.ContatoImpl;

@Repository
public interface ContatoRepository extends JpaRepository<ContatoImpl, Long>{

}
