package com.teoriadegrafos.uniritter.dto;

import com.teoriadegrafos.uniritter.entities.Atividade;

import java.util.List;

public record AtividadeDTO(Long id, String nome, String identificador, int duracaoEstimada, List<Long> dependencias) {
    public AtividadeDTO(Atividade atividade){
        this(
                atividade.getId(),
                atividade.getNome(),
                atividade.getIdentificador(),
                atividade.getDuracaoEstimada(),
                atividade.getDependencias()
        );
    }
}
