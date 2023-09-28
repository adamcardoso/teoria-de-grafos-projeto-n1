package com.teoriadegrafos.uniritter.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
    @EmbeddedId
    private RelacionamentoDependenciaLiberadasID id;
}
