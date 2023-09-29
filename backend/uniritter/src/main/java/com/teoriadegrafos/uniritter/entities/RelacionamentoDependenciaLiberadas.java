package com.teoriadegrafos.uniritter.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "rel_ativ_dep_lib")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelacionamentoDependenciaLiberadas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer atividade;
    private Integer dependencia;
    private Integer projeto;
}
