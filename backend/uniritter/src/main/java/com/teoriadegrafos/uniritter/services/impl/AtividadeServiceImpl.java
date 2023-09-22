package com.teoriadegrafos.uniritter.services.impl;

import com.teoriadegrafos.uniritter.entities.Atividade;
import com.teoriadegrafos.uniritter.entities.Projeto;
import com.teoriadegrafos.uniritter.entities.StatusEnum;
import com.teoriadegrafos.uniritter.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class AtividadeServiceImpl{
    private static Integer ID = 1;
    private static final String ID_NOT_FOUND_MESSAGE = "Id não encontrado ";
    private final Projeto projeto;

    public Projeto criarProjeto(String nome, LocalDate dataInicio, LocalDate dataFim){
        projeto.setDataInicioProjeto(dataInicio);
        projeto.setDataFimProjeto(dataFim);
        projeto.setNome(nome);
        return projeto;
    }

    public Atividade criarAtividade(String nome, Integer duracaoAtividade, Projeto projeto, Integer idAtividadeDependecia) {
        if(isNull(projeto.getAtividades()) && isNull(idAtividadeDependecia)){
            Atividade atividadePrincipal = Atividade.builder()
                                                    .id(ID)
                                                    .diasDuracaoAtividade(duracaoAtividade)
                                                    .nome(nome)
                                                    .status(StatusEnum.INATIVA)
                                                    .build();
            projeto.getAtividades().add(atividadePrincipal);
            return atividadePrincipal;
        }else{
            if(isNull(idAtividadeDependecia)){
                Atividade atividadeSemDependencia = Atividade.builder()
                                                .id(++ID)
                                                .diasDuracaoAtividade(duracaoAtividade)
                                                .nome(nome)
                                                .status(StatusEnum.INATIVA)
                                                .build();
                projeto.getAtividades().add(atividadeSemDependencia);
                return atividadeSemDependencia;
            }else{
                Atividade dependecia = projeto
                        .getAtividades()
                        .stream()
                        .filter(
                                atividade -> atividade.getId().equals(idAtividadeDependecia)
                        )
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Atividade não encontrada"));
                Atividade atividade = Atividade.builder()
                        .id(++ID)
                        .diasDuracaoAtividade(duracaoAtividade)
                        .nome(nome)
                        .status(StatusEnum.DEPENDENTE)
                        .build();

                dependecia.getAtividadesLiberadas().add(atividade);
                atividade.getAtividadesDependentes().add(atividade);

                return atividade;
            }
        }
    }
}
