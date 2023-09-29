package com.teoriadegrafos.uniritter.services.impl.bos;

import com.teoriadegrafos.uniritter.entities.enums.StatusEnum;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AtividadeBO {
    private Integer id;
    private String nome;
    private Integer diasDuracaoAtividade;
    private StatusEnum status;
    private Set<AtividadeBO> atividadesDependentes;
    private Set<AtividadeBO> atividadesLiberadas;
    private Integer idProjeto;
}
