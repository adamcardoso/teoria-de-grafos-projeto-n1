package com.teoriadegrafos.uniritter.repositories;

import com.teoriadegrafos.uniritter.entities.RelacionamentoDependenciaLiberadas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelacionamentoDependenciaLiberadasRepository extends JpaRepository<RelacionamentoDependenciaLiberadas, Integer> {

    List<RelacionamentoDependenciaLiberadas> findByProjeto(Integer idProjeto);
}
