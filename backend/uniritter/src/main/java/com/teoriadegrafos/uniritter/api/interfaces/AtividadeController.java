package com.teoriadegrafos.uniritter.api.interfaces;

import com.teoriadegrafos.uniritter.dto.AtividadeDTO;
import com.teoriadegrafos.uniritter.entities.Atividade;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

public interface AtividadeController {

    @GetMapping("/listar")
    ResponseEntity<Page<AtividadeDTO>> listarAtividades(Pageable pageable);

    @GetMapping("/buscar/{id}")
    ResponseEntity<AtividadeDTO> consultarAtividade(@PathVariable Long id);

    @PostMapping("/criar")
    ResponseEntity<AtividadeDTO> criarAtividade(@RequestBody AtividadeDTO atividadeDTO);

    @PutMapping("/atualizar/{id}")
    ResponseEntity<AtividadeDTO> atualizarAtividade(@PathVariable Long id, @RequestBody AtividadeDTO atividadeDTO);

    @DeleteMapping("/remover/{id}")
    ResponseEntity<Void> removerAtividade(@PathVariable Long id);
}
