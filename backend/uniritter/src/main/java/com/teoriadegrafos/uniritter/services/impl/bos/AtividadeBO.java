package com.teoriadegrafos.uniritter.services.impl.bos;

import com.teoriadegrafos.uniritter.entities.Atividade;
import com.teoriadegrafos.uniritter.entities.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtividadeBO {
    private Integer id;
    private String nome;
    private Integer diasDuracaoAtividade;
    private StatusEnum status;
    private Set<AtividadeBO> atividadesDependentes = new HashSet<>();
    private Set<AtividadeBO> atividadesLiberadas = new HashSet<>();
    private Integer idProjeto;
}
