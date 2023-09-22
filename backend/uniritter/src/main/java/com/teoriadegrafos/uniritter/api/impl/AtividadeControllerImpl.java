package com.teoriadegrafos.uniritter.api.impl;

import com.teoriadegrafos.uniritter.api.interfaces.AtividadeController;
import com.teoriadegrafos.uniritter.dto.AtividadeDTO;
import com.teoriadegrafos.uniritter.services.interfaces.AtividadeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/atividade")
public class AtividadeControllerImpl implements AtividadeController {

    private static final Logger logger = LoggerFactory.getLogger(AtividadeControllerImpl.class);

    private final AtividadeService atividadeService;

    public AtividadeControllerImpl(AtividadeService atividadeService) {
        this.atividadeService = atividadeService;
    }

    @Override
    @GetMapping("/listar")
    public ResponseEntity<Page<AtividadeDTO>> listarAtividades(Pageable pageable) {
        Page<AtividadeDTO> page = atividadeService.listarAtividades(pageable);

        logger.info("Listando todas as atividades");

        return ResponseEntity.ok().body(page);
    }

    @Override
    @GetMapping("/buscar/{id}")
    public ResponseEntity<AtividadeDTO> consultarAtividade(@PathVariable Long id) {
        Optional<AtividadeDTO> atividadeDTO = atividadeService.consultarAtividade(id);
        if (atividadeDTO.isPresent()) {
            logger.info("Encontrada pessoa com ID: {}", id);
            return ResponseEntity.ok(atividadeDTO.get());
        } else {
            logger.error("Pessoa com ID {} não encontrada", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PostMapping("/criar")
    public ResponseEntity<AtividadeDTO> criarAtividade(@RequestBody AtividadeDTO atividadeDTO) {

        atividadeDTO = atividadeService.criarAtividade(atividadeDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(atividadeDTO.id()).toUri();

        logger.info("Inserindo uma nova pessoa");
        return ResponseEntity.created(uri).body(atividadeDTO);
    }

    @Override
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<AtividadeDTO> atualizarAtividade(@PathVariable Long id, @RequestBody AtividadeDTO atividadeDTO) {

        atividadeDTO = atividadeService.atualizarAtividade(id, atividadeDTO);

        logger.info("Atualizando uma pessoa com ID: {}", id);
        return ResponseEntity.ok().body(atividadeDTO);
    }

    @Override
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> removerAtividade(@PathVariable Long id) {

        atividadeService.removerAtividade(id);

        logger.info("Deletando uma pessoa com ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/caminho-critico")
    public ResponseEntity<List<AtividadeDTO>> obterCaminhoCritico() {
        logger.info("Chamada ao endpoint /api/atividade/caminho-critico");

        try {
            List<AtividadeDTO> caminhoCritico = atividadeService.calcularCaminhoCritico();
            logger.info("Caminho crítico calculado com sucesso.");
            return ResponseEntity.ok(caminhoCritico);
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao calcular o caminho crítico: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

