package com.teoriadegrafos.uniritter.services.impl;

import com.teoriadegrafos.uniritter.entities.Atividade;
import com.teoriadegrafos.uniritter.entities.RelacionamentoDependenciaLiberadas;
import com.teoriadegrafos.uniritter.entities.enums.StatusEnum;
import com.teoriadegrafos.uniritter.exceptions.ResourceNotFoundException;
import com.teoriadegrafos.uniritter.repositories.AtividadeRepository;
import com.teoriadegrafos.uniritter.repositories.RelacionamentoDependenciaLiberadasRepository;
import com.teoriadegrafos.uniritter.repositories.converters.AtividadeConverter;
import com.teoriadegrafos.uniritter.services.impl.bos.AtividadeBO;
import com.teoriadegrafos.uniritter.services.impl.bos.ProjetoBO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class AtividadeServiceImpl{
    private static final String ID_NOT_FOUND_MESSAGE = "Id não encontrado ";
    private final AtividadeRepository atividadeRepository;
    private final ProjetoServiceImpl projetoService;
    private final AtividadeConverter atividadeConverter;
    private final RelacionamentoDependenciaLiberadasRepository relacionamentoDependenciaLiberadasRepository;

    public AtividadeBO criarAtividade(String nome, Integer duracaoAtividade, Integer idProjeto, Integer idAtividadeDependecia) {
        ProjetoBO projeto = projetoService.buscarProjetoPorIdSemAtividades(idProjeto);

        if(isNull(idAtividadeDependecia)){
            AtividadeBO atividadePrincipal = AtividadeBO.builder()
                                                    .diasDuracaoAtividade(duracaoAtividade)
                                                    .nome(nome)
                                                    .status(StatusEnum.INATIVA)
                                                    .idProjeto(idProjeto)
                                                    .diasDuracaoAtividade(duracaoAtividade)
                                                    .build();

            if(nonNull(projeto.getAtividades())){
                projeto.getAtividades().add(atividadePrincipal);
            }else{
                projeto.setAtividades(Collections.singletonList(atividadePrincipal));
            }

            Atividade save = atividadeRepository.save(atividadeConverter.atitivdadeToEntity(atividadePrincipal));
            atividadePrincipal.setId(save.getId());
            relacionamentoDependenciaLiberadasRepository.save(
                    RelacionamentoDependenciaLiberadas.builder()
                                                        .atividade(save.getId())
                                                        .projeto(idProjeto)
                                                        .build()
            );
            return atividadePrincipal;
        }else{
            AtividadeBO dependecia = buscarAtividadePendencia(projeto.getAtividades(), idAtividadeDependecia);

            AtividadeBO atividade = AtividadeBO.builder()
                    .diasDuracaoAtividade(duracaoAtividade)
                    .nome(nome)
                    .status(StatusEnum.DEPENDENTE)
                    .idProjeto(idProjeto)
                    .diasDuracaoAtividade(duracaoAtividade)
                    .build();

            if (nonNull(dependecia.getAtividadesLiberadas())) {
                dependecia.getAtividadesLiberadas().add(atividade);
            } else {
                dependecia.setAtividadesLiberadas(Collections.singleton(atividade));
            }

            if (nonNull(atividade.getAtividadesDependentes())) {
                atividade.getAtividadesDependentes().add(atividade);
            } else {
                atividade.setAtividadesDependentes(Collections.singleton(atividade));
            }

            Atividade save = atividadeRepository.save(atividadeConverter.atitivdadeToEntity(atividade));

            relacionamentoDependenciaLiberadasRepository.save(
                    RelacionamentoDependenciaLiberadas.builder()
                            .atividade(save.getId())
                            .dependencia(dependecia.getId())
                            .projeto(idProjeto)
                            .build());
            return atividade;
        }
    }

    private AtividadeBO buscarAtividadePendencia(List<AtividadeBO> atividades, Integer idAtividadeDependecia) {
        List<AtividadeBO> atividadesFiltradas = atividades.stream().filter(atividade -> atividade.getId().equals(idAtividadeDependecia)).collect(Collectors.toList());
        if(atividadesFiltradas.isEmpty()){
            atividades.forEach(atividade ->{
                if(!atividade.getAtividadesLiberadas().isEmpty()){
                    buscarRecursivo(idAtividadeDependecia, atividade.getAtividadesLiberadas(), atividadesFiltradas);
                }
            });
        }
        return atividadesFiltradas.stream().findFirst().orElseThrow(()-> new ResourceNotFoundException("Dependencia não encontrada"));

    }

        private void buscarRecursivo(Integer idAtividadeDependecia,  Set<AtividadeBO> inferior, List<AtividadeBO> atividadesFiltradas) {
        inferior.forEach(atividadeInferior -> {
            if(atividadeInferior.getId().equals(idAtividadeDependecia)){
                atividadesFiltradas.add(atividadeInferior);
            }
            if (nonNull(atividadeInferior.getAtividadesLiberadas()) && !atividadeInferior.getAtividadesLiberadas().isEmpty()) {
                buscarRecursivo(idAtividadeDependecia, atividadeInferior.getAtividadesLiberadas(), atividadesFiltradas);
            }
        });
    }

    public void atualizarStatusAtividade(Integer idAtividade, StatusEnum novoStatus) {
        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new ResourceNotFoundException("Atividade não encontrada com o ID: " + idAtividade));

        atividade.setStatus(novoStatus);

        atividadeRepository.save(atividade);
    }

    public void deletarAtividade(Integer idAtividade){
        try{
            atividadeRepository.deleteById(idAtividade);
        }catch (Exception e){
            throw new ResourceNotFoundException(ID_NOT_FOUND_MESSAGE);
        }
    }

    public List<AtividadeBO> buscarAtividadeProjeto(ProjetoBO projeto) {
        List<Atividade> byIdProjeto = atividadeRepository.findByIdProjeto(projeto.getId());
        return montarAtividadeEmGrafos(byIdProjeto.stream().map(atividadeConverter::atitivdadeToBo).collect(Collectors.toList()), projeto.getId());
    }

    private List<AtividadeBO> montarAtividadeEmGrafos(List<AtividadeBO> atividades, Integer idProjeto) {
        List<RelacionamentoDependenciaLiberadas> relacionamentoDependencias = relacionamentoDependenciaLiberadasRepository
                .findByProjeto(idProjeto);

        atividades.forEach(atividade ->{
            List<RelacionamentoDependenciaLiberadas> listaDependenciasAtividade = relacionamentoDependencias
                    .stream()
                    .filter(relacionamento -> relacionamento.getDependencia().equals(atividade.getId()))
                    .toList();

            listaDependenciasAtividade.forEach(depen ->{
                Optional<AtividadeBO> atividadeFiltradaDepencia = atividades.stream()
                                                                    .filter(ativFiltrada -> ativFiltrada.getId().equals(depen.getDependencia()))
                                                                    .findFirst();
                if(atividadeFiltradaDepencia.isPresent()){
                    if (nonNull(atividade.getAtividadesLiberadas())) {
                        atividade.getAtividadesLiberadas().add(atividadeFiltradaDepencia.get());
                    } else {
                        atividade.setAtividadesLiberadas(Collections.singleton(atividadeFiltradaDepencia.get()));
                    }

                    if (nonNull(atividadeFiltradaDepencia.get().getAtividadesDependentes())) {
                        atividadeFiltradaDepencia.get().getAtividadesDependentes().add(atividade);
                    } else {
                        atividadeFiltradaDepencia.get().setAtividadesDependentes(Collections.singleton(atividade));
                    }
                }
            });
        });
        return atividades;
    }
}
