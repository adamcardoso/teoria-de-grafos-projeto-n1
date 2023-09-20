package com.teoriadegrafos.uniritter.services.impl;

import com.teoriadegrafos.uniritter.dto.AtividadeDTO;
import com.teoriadegrafos.uniritter.entities.Atividade;
import com.teoriadegrafos.uniritter.exceptions.AtividadeNotFoundException;
import com.teoriadegrafos.uniritter.exceptions.ResourceNotFoundException;
import com.teoriadegrafos.uniritter.repositories.AtividadeRepository;
import com.teoriadegrafos.uniritter.services.interfaces.AtividadeService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AtividadeServiceImpl implements AtividadeService {
    private final AtividadeRepository atividadeRepository;

    private static final String ID_NOT_FOUND_MESSAGE = "Id não encontrado ";

    public AtividadeServiceImpl(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AtividadeDTO> listarAtividades(Pageable pageable) {
        try{
            Page<Atividade> atividades = atividadeRepository.findAll(pageable);
            return atividades.map(AtividadeDTO::new);
        }catch (Exception e) {
            throw new AtividadeNotFoundException("Erro ao buscar atividades no banco de dados");
        }
    }

    @Override
    public AtividadeDTO criarAtividade(AtividadeDTO atividadeDTO) {
        try {
            Atividade entity = new Atividade();
            copyDtoToEntity(atividadeDTO, entity);
            entity = atividadeRepository.save(entity);
            return new AtividadeDTO(entity);
        } catch (Exception e) {
            throw new AtividadeNotFoundException("Erro ao inserir pessoa no banco de dados");
        }
    }

    @Override
    @Transactional
    public Optional<AtividadeDTO> consultarAtividade(Long id) {
        try {
            Optional<Atividade> obj = atividadeRepository.findById(id);
            return obj.map(AtividadeDTO::new);
        } catch (Exception e) {
            throw new AtividadeNotFoundException("Erro ao buscar pessoa por ID no banco de dados");
        }
    }

    @Override
    public AtividadeDTO atualizarAtividade(Long id, AtividadeDTO atividadeDTO) {
        try {
            Optional<Atividade> optionalEntity = atividadeRepository.findById(id);
            if (optionalEntity.isPresent()) {
                Atividade entity = optionalEntity.get();
                copyDtoToEntity(atividadeDTO, entity);
                entity = atividadeRepository.save(entity);
                return new AtividadeDTO(entity);
            } else {
                throw new AtividadeNotFoundException(ID_NOT_FOUND_MESSAGE + id);
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(ID_NOT_FOUND_MESSAGE + id);
        }
    }

    @Override
    public void removerAtividade(Long id) {
        try{
            atividadeRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(ID_NOT_FOUND_MESSAGE + id);
        } catch (DataIntegrityViolationException e) {
            throw new AtividadeNotFoundException("Violação de integridade no banco de dados");
        } catch (Exception e) {
            throw new AtividadeNotFoundException("Erro ao excluir pessoa do banco de dados");
        }
    }

    private void copyDtoToEntity(AtividadeDTO atividadeDTO, Atividade atividade){
        atividade.setNome(atividadeDTO.nome());
        atividade.setIdentificador(atividadeDTO.identificador());
        atividade.setDuracaoEstimada(atividadeDTO.duracaoEstimada());
    }
}
