package com.teoriadegrafos.uniritter.services.impl;

import com.teoriadegrafos.uniritter.entities.Projeto;
import com.teoriadegrafos.uniritter.exceptions.ResourceNotFoundException;
import com.teoriadegrafos.uniritter.repositories.ProjetoRepository;
import com.teoriadegrafos.uniritter.repositories.converters.ProjetoConverter;
import com.teoriadegrafos.uniritter.services.impl.bos.AtividadeBO;
import com.teoriadegrafos.uniritter.services.impl.bos.ProjetoBO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ProjetoServiceImpl {
    private final ProjetoRepository projetoRepository;
    private final ProjetoConverter projetoConverter;
    private final AtividadeServiceImpl atividadeService;

    public ProjetoBO criarProjeto(String nome, LocalDate dataInicio, LocalDate dataFim){
        Projeto save = projetoRepository.save(Projeto.builder()
                .dataFimProjeto(dataFim)
                .dataInicioProjeto(dataInicio)
                .nome(nome)
                .build());

        return projetoConverter.projetoToBo(save);
    }

    public List<ProjetoBO> buscarTodosProjetos(){
        List<ProjetoBO> all = projetoRepository.findAll().stream().map(projetoConverter::projetoToBo).collect(Collectors.toList());
        all.forEach(projeto -> {
            List<AtividadeBO> atividadesProjeto = new ArrayList<>(atividadeService.buscarAtividadeProjeto(projeto));
            projeto.setAtividades(atividadesProjeto);
        });

        return all;
    }

    public ProjetoBO buscarProjetoPorId(Integer idProjeto){
        ProjetoBO projetoBO = projetoRepository.findById(idProjeto).map(projetoConverter::projetoToBo).orElseThrow(()-> new ResourceNotFoundException("Projeto não encontrado"));
        List<AtividadeBO> atividadeBOS = atividadeService.buscarAtividadeProjeto(projetoBO);
        projetoBO.setAtividades(atividadeBOS);
        return projetoBO;
    }

    public ProjetoBO buscarProjetoPorIdSemAtividades(Integer idProjeto) {
        return projetoRepository.findById(idProjeto).map(projetoConverter::projetoToBo).orElseThrow(()-> new ResourceNotFoundException("Projeto não encontrado"));

    }
}
