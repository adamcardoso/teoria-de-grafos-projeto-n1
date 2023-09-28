package com.teoriadegrafos.uniritter.repositories;

import com.teoriadegrafos.uniritter.entities.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {
}
