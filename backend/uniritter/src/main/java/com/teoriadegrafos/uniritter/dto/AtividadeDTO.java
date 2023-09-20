package com.teoriadegrafos.uniritter.dto;

import com.teoriadegrafos.uniritter.entities.Atividade;

public record AtividadeDTO(Long id, String nome, String identificador, int duracaoEstimada) {
    public AtividadeDTO(Atividade atividade){
        this(
                atividade.getId(),
                atividade.getNome(),
                atividade.getIdentificador(),
                atividade.getDuracaoEstimada()
        );
    }
}

