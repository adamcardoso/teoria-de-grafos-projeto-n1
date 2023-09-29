package com.teoriadegrafos.uniritter.controllers;

import com.teoriadegrafos.uniritter.entities.enums.StatusEnum;
import com.teoriadegrafos.uniritter.services.impl.AtividadeServiceImpl;
import com.teoriadegrafos.uniritter.services.impl.bos.AtividadeBO;
import com.teoriadegrafos.uniritter.services.impl.bos.ProjetoBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uniritter")
public class AtividadeControllerImpl {

    private static final Logger logger = LoggerFactory.getLogger(AtividadeControllerImpl.class);

    private final AtividadeServiceImpl atividadeService;

    public AtividadeControllerImpl(AtividadeServiceImpl atividadeService) {
        this.atividadeService = atividadeService;
    }

    @GetMapping("/projetos")
    public ResponseEntity<List<ProjetoBO>> buscarTodoProjetos() {
        List<ProjetoBO> projetos = atividadeService.buscarTodoProjetos();

        logger.info("Buscando todos os projetos!");

        return ResponseEntity.ok().body(projetos);
    }

    @GetMapping("/projetos/{id}")
    public ResponseEntity<ProjetoBO> buscarProjetoPorId(@PathVariable Integer id) {
        ProjetoBO projeto = atividadeService.buscarProjetoPorId(id);

        if (projeto != null) {
            logger.info("Buscando projeto por ID: {}", id);
            return ResponseEntity.ok().body(projeto);
        } else {
            logger.error("Projeto não encontrado com ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/projetos")
    public ResponseEntity<ProjetoBO> criarProjeto(@RequestBody ProjetoBO projeto) {
        ProjetoBO novoProjeto = atividadeService.criarProjeto(projeto.getNome(), projeto.getDataInicioProjeto(), projeto.getDataFimProjeto());

        if (novoProjeto != null) {
            logger.info("Criando novo projeto: " + novoProjeto.getId());
            return ResponseEntity.ok().body(novoProjeto);
        } else {
            logger.error("Falha ao criar novo projeto.");
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/projetos/{idProjeto}/atividades")
    public ResponseEntity<AtividadeBO> criarAtividade(
            @PathVariable Integer idProjeto,
            @RequestBody AtividadeBO atividade
    ) {
        AtividadeBO novaAtividade = atividadeService.criarAtividade(
                atividade.getNome(),
                atividade.getDiasDuracaoAtividade(),
                idProjeto,
                atividade.getId()
        );

        if (novaAtividade != null) {
            logger.info("Criando nova atividade para o projeto " + idProjeto + ": " + novaAtividade.getId());
            return ResponseEntity.ok().body(novaAtividade);
        } else {
            logger.error("Falha ao criar nova atividade.");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/atividades/{idAtividade}")
    public ResponseEntity<Void> atualizarStatusAtividade(
            @PathVariable Integer idAtividade,
            @RequestParam(name = "novoStatus") String novoStatus // Parâmetro para o novo status
    ) {
        // Converta o valor do novoStatus para um enum StatusEnum
        StatusEnum statusEnum = StatusEnum.fromValor(novoStatus);

        // Chame o serviço para atualizar o status da atividade
        atividadeService.atualizarStatusAtividade(idAtividade, statusEnum);

        logger.info("Atualizando status da atividade com ID: " + idAtividade + " para " + novoStatus);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/atividades/{id}")
    public ResponseEntity<Void> deletarAtividade(@PathVariable Integer id) {
        atividadeService.deletarAtividade(id);

        logger.info("Deletando uma atividade com ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}
