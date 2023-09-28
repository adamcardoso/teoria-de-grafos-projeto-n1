package com.teoriadegrafos.uniritter.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class RelacionamentoDependenciaLiberadasID implements Serializable {
    private Integer atividade;
    private Integer dependencia;
    private Integer liberada;
    private Integer projeto;
}
