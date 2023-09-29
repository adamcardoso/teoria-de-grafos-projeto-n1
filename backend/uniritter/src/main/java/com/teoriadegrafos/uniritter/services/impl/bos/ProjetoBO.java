package com.teoriadegrafos.uniritter.services.impl.bos;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class ProjetoBO {
    private Integer id;
    private String nome;
    private LocalDate dataInicioProjeto;
    private LocalDate dataFimProjeto;
    private List<AtividadeBO> atividades = new ArrayList<>();

}
