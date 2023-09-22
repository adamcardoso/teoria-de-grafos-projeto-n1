package com.teoriadegrafos.uniritter.dto;

import com.teoriadegrafos.uniritter.entities.Atividade;
import java.util.List;
import java.time.LocalDate; // Import LocalDate

public record AtividadeDTO(
        Long id,
        String nome,
        String identificador,
        LocalDate dataDeInicioDaAtividade,
        int duracaoEstimada,
        LocalDate dataDeFimDaAtividade, // Include dataDeFimDaAtividade field
        List<Long> dependencias,

        LocalDate dataDeInicioMaisCedo,
        LocalDate dataDeTerminoMaisCedo
) {
    public AtividadeDTO(Atividade atividade) {
        this(
                atividade.getId(),
                atividade.getNome(),
                atividade.getIdentificador(),
                atividade.getDataDeInicioDaAtividade(),
                atividade.getDuracaoEstimada(),
                atividade.getDataDeFimDaAtividade(), // Map the dataDeFimDaAtividade field
                atividade.getDependencias(),
                atividade.getDataDeTerminoMaisCedo(),
                atividade.getDataDeTerminoMaisCedo()
        );
    }
}
