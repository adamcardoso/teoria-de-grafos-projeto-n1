package com.teoriadegrafos.uniritter.services.interfaces;

import com.teoriadegrafos.uniritter.dto.AtividadeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AtividadeService {

    Page<AtividadeDTO> listarAtividades(Pageable pageable);

    AtividadeDTO criarAtividade(AtividadeDTO atividadeDTO);

    Optional<AtividadeDTO> consultarAtividade(Long id);

    AtividadeDTO atualizarAtividade(Long id, AtividadeDTO atividadeDTO);

    void removerAtividade(Long id);

    List<AtividadeDTO> calcularCaminhoCritico();
}
