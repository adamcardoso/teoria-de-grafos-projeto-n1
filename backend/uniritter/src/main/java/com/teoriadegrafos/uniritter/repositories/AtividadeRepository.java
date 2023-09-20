package com.teoriadegrafos.uniritter.repositories;

import com.teoriadegrafos.uniritter.entities.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    // Você pode adicionar métodos de consulta personalizados aqui, se necessário
}