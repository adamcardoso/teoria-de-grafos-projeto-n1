package com.teoriadegrafos.uniritter.repositories;

import com.teoriadegrafos.uniritter.entities.RelacionamentoDependenciaLiberadas;
import com.teoriadegrafos.uniritter.entities.RelacionamentoDependenciaLiberadasID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelacionamentoDependenciaLiberadasRepository extends JpaRepository<RelacionamentoDependenciaLiberadas, RelacionamentoDependenciaLiberadasID> {

    List<RelacionamentoDependenciaLiberadas> findRelacionamentoDependenciaLiberadasByIdProjeto(Integer idProjeto);
}
