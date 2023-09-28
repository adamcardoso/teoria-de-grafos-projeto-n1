package com.teoriadegrafos.uniritter.services.impl.bos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjetoBO {
    private Integer id;
    private String nome;
    private LocalDate dataInicioProjeto;
    private LocalDate dataFimProjeto;
    private List<AtividadeBO> atividades = new ArrayList<>();

}
