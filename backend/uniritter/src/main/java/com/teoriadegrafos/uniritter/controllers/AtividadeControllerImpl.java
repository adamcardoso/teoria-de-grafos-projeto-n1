package com.teoriadegrafos.uniritter.controllers;

import com.teoriadegrafos.uniritter.entities.enums.StatusEnum;
import com.teoriadegrafos.uniritter.services.impl.AtividadeServiceImpl;
import com.teoriadegrafos.uniritter.services.impl.ProjetoServiceImpl;
import com.teoriadegrafos.uniritter.services.impl.bos.AtividadeBO;
import com.teoriadegrafos.uniritter.services.impl.bos.ProjetoBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uniritter")
@RequiredArgsConstructor
@Slf4j
public class AtividadeControllerImpl {
    private final AtividadeServiceImpl atividadeService;
    private final ProjetoServiceImpl projetoService;

    @GetMapping("/projetos")
    public ResponseEntity<List<ProjetoBO>> buscarTodoProjetos() {
        List<ProjetoBO> projetos = projetoService.buscarTodosProjetos();

        log.info("Buscando todos os projetos!");

        return ResponseEntity.ok().body(projetos);
    }

    @GetMapping("/projetos/{id}")
    public ResponseEntity<ProjetoBO> buscarProjetoPorId(@PathVariable Integer id) {
        ProjetoBO projeto = projetoService.buscarProjetoPorId(id);

        if (projeto != null) {
            log.info("Buscando projeto por ID: {}", id);
            return ResponseEntity.ok().body(projeto);
        } else {
            log.error("Projeto não encontrado com ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/projetos")
    public ResponseEntity<ProjetoBO> criarProjeto(@RequestBody ProjetoBO projeto) {
        ProjetoBO novoProjeto = projetoService.criarProjeto(projeto.getNome(), projeto.getDataInicioProjeto(), projeto.getDataFimProjeto());

        if (novoProjeto != null) {
            log.info("Criando novo projeto: " + novoProjeto.getId());
            return ResponseEntity.ok().body(novoProjeto);
        } else {
            log.error("Falha ao criar novo projeto.");
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/projetos/{idProjeto}/atividades")
    public ResponseEntity<AtividadeBO> criarAtividade(
            @PathVariable Integer idProjeto,
            @RequestBody AtividadeBO atividade,
            @RequestParam(required = false) Integer idAtividadeDependente
    ) {
        AtividadeBO novaAtividade = atividadeService.criarAtividade(
                atividade.getNome(),
                atividade.getDiasDuracaoAtividade(),
                idProjeto,
                idAtividadeDependente
        );

        if (novaAtividade != null) {
            log.info("Criando nova atividade para o projeto " + idProjeto + ": " + novaAtividade.getId());
            return ResponseEntity.ok().body(novaAtividade);
        } else {
            log.error("Falha ao criar nova atividade.");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/atividades/{idAtividade}")
    public ResponseEntity<Void> atualizarStatusAtividade(
            @PathVariable Integer idAtividade,
            @RequestParam(name = "novoStatus") String novoStatus
    ) {
        StatusEnum statusEnum = StatusEnum.fromValor(novoStatus);

        atividadeService.atualizarStatusAtividade(idAtividade, statusEnum);

        log.info("Atualizando status da atividade com ID: " + idAtividade + " para " + novoStatus);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/atividades/{id}")
    public ResponseEntity<Void> deletarAtividade(@PathVariable Integer id) {
        atividadeService.deletarAtividade(id);

        log.info("Deletando uma atividade com ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}
