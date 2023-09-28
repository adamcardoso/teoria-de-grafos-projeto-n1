package com.teoriadegrafos.uniritter.repositories;

import com.teoriadegrafos.uniritter.entities.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Integer> {

    List<Atividade> findByIdProjeto(Integer idProjeto);
}
