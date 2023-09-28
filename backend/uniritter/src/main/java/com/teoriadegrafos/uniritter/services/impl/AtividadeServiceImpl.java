package com.teoriadegrafos.uniritter.services.impl;

import com.teoriadegrafos.uniritter.entities.Atividade;
import com.teoriadegrafos.uniritter.entities.Projeto;
import com.teoriadegrafos.uniritter.entities.RelacionamentoDependenciaLiberadas;
import com.teoriadegrafos.uniritter.entities.RelacionamentoDependenciaLiberadasID;
import com.teoriadegrafos.uniritter.entities.enums.StatusEnum;
import com.teoriadegrafos.uniritter.exceptions.ResourceNotFoundException;
import com.teoriadegrafos.uniritter.repositories.AtividadeRepository;
import com.teoriadegrafos.uniritter.repositories.ProjetoRepository;
import com.teoriadegrafos.uniritter.repositories.RelacionamentoDependenciaLiberadasRepository;
import com.teoriadegrafos.uniritter.repositories.converters.AtividadeConverter;
import com.teoriadegrafos.uniritter.repositories.converters.ProjetoConverter;
import com.teoriadegrafos.uniritter.services.impl.bos.AtividadeBO;
import com.teoriadegrafos.uniritter.services.impl.bos.ProjetoBO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class AtividadeServiceImpl{
    private static Integer ID = 1;
    private static final String ID_NOT_FOUND_MESSAGE = "Id não encontrado ";
    private final ProjetoRepository projetoRepository;
    private final AtividadeRepository atividadeRepository;
    private final ProjetoConverter projetoConverter;
    private final AtividadeConverter atividadeConverter;
    private final RelacionamentoDependenciaLiberadasRepository relacionamentoDependenciaLiberadasRepository;

    public ProjetoBO criarProjeto(String nome, LocalDate dataInicio, LocalDate dataFim){
        ProjetoBO build = ProjetoBO.builder()
                .dataFimProjeto(dataFim)
                .dataInicioProjeto(dataInicio)
                .nome(nome)
                .build();
        Projeto save = projetoRepository.save(projetoConverter.projetoToEntity(build));

        build.setId(save.getId());
        return build;
    }

    public List<ProjetoBO> buscarTodoProjetos(){
        List<ProjetoBO> all = projetoRepository.findAll().stream().map(projetoConverter::projetoToBo).collect(Collectors.toList());
        all.forEach(projeto -> {

            List<AtividadeBO> atividadesProjeto = new ArrayList<>(buscarAtividadeProjeto(projeto));

            projeto.setAtividades(atividadesProjeto);
        });

        return all;
    }

    public ProjetoBO buscarProjetoPorId(Integer idProjeto){
        ProjetoBO projetoBO = projetoRepository.findById(idProjeto).map(projetoConverter::projetoToBo).orElseThrow(()-> new ResourceNotFoundException("Projeto não encontrado"));
        List<AtividadeBO> atividadeBOS = buscarAtividadeProjeto(projetoBO);
        projetoBO.setAtividades(atividadeBOS);
        return projetoBO;
    }



    public AtividadeBO criarAtividade(String nome, Integer duracaoAtividade, Integer idProjeto, Integer idAtividadeDependecia) {
        ProjetoBO projeto = buscarProjetoPorId(idProjeto);

        if(isNull(projeto.getAtividades()) || isNull(idAtividadeDependecia)){
            AtividadeBO atividadePrincipal = AtividadeBO.builder()
                                                    .diasDuracaoAtividade(duracaoAtividade)
                                                    .nome(nome)
                                                    .status(StatusEnum.INATIVA)
                                                    .build();
            projeto.getAtividades().add(atividadePrincipal);

            Atividade save = atividadeRepository.save(atividadeConverter.atitivdadeToEntity(atividadePrincipal));

            relacionamentoDependenciaLiberadasRepository.save(
                    RelacionamentoDependenciaLiberadas.builder()
                                                        .id(RelacionamentoDependenciaLiberadasID.builder()
                                                                .atividade(save.getId())
                                                                .projeto(idProjeto)
                                                                .build())
                                                        .build()
            );
            return atividadePrincipal;
        }else{
            AtividadeBO dependecia = projeto
                    .getAtividades()
                    .stream()
                    .filter(
                            atividade -> atividade.getId().equals(idAtividadeDependecia)
                    )
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Atividade não encontrada"));

            AtividadeBO atividade = AtividadeBO.builder()
                    .diasDuracaoAtividade(duracaoAtividade)
                    .nome(nome)
                    .status(StatusEnum.DEPENDENTE)
                    .build();

            dependecia.getAtividadesLiberadas().add(atividade);
            atividade.getAtividadesDependentes().add(atividade);

            return atividade;
        }
    }

    public void atualizarStatusAtividade(Integer idAtividade){

    }


    private List<AtividadeBO> buscarAtividadeProjeto(ProjetoBO projeto) {
        List<Atividade> byIdProjeto = atividadeRepository.findByIdProjeto(projeto.getId());
        return montarAtividadeEmGrafos(byIdProjeto.stream().map(atividadeConverter::atitivdadeToBo).collect(Collectors.toList()), projeto.getId());
    }

    private List<AtividadeBO> montarAtividadeEmGrafos(List<AtividadeBO> atividades, Integer idProjeto) {
        List<RelacionamentoDependenciaLiberadas> relacionamentoDependencias = relacionamentoDependenciaLiberadasRepository
                .findByIdProjeto(idProjeto);

        atividades.forEach(atividade ->{
            List<RelacionamentoDependenciaLiberadas> listaDependenciasAtividade = relacionamentoDependencias
                    .stream()
                    .filter(relacionamento -> relacionamento.getId().getAtividade().equals(atividade.getId()))
                    .toList();
            listaDependenciasAtividade.forEach(depen ->{
                if(nonNull(depen.getId().getDependencia())){
                    Optional<AtividadeBO> first = atividades.stream().filter(at -> at.getId().equals(depen.getId().getDependencia())).findFirst();
                    first.ifPresent(at -> atividade.getAtividadesDependentes().add(at));
                }else if(nonNull(depen.getId().getLiberada())){
                    Optional<AtividadeBO> first = atividades.stream().filter(at -> at.getId().equals(depen.getId().getDependencia())).findFirst();
                    first.ifPresent(at -> atividade.getAtividadesLiberadas().add(at));
                }
            });

        });
        return atividades;
    }
}
