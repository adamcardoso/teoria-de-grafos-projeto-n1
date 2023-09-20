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

import java.util.Collections;
import java.util.List;
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

            // Se a lista de dependências não for nula, processe as dependências
            if (atividadeDTO.dependencias() != null) {
                // Certifique-se de que as dependências existem no banco de dados
                List<Long> dependenciasIds = atividadeDTO.dependencias();

                // Converta a lista de IDs em uma lista de entidades
                List<Atividade> dependencias = atividadeRepository.findAllById(dependenciasIds);

                // Crie uma lista de IDs a partir das entidades
                List<Long> dependenciasIdsFromEntities = dependencias.stream()
                        .map(Atividade::getId)
                        .toList(); // Alterado de collect(Collectors.toList()) para toList()

                // Associe as dependências à atividade
                entity.setDependencias(dependenciasIdsFromEntities);
            }

            entity = atividadeRepository.save(entity);
            return new AtividadeDTO(entity);
        } catch (Exception e) {
            throw new AtividadeNotFoundException("Erro ao inserir atividade no banco de dados");
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

                // Atualize os campos simples da atividade (nome, identificador, duracaoEstimada)
                copyDtoToEntity(atividadeDTO, entity);

                // Em seguida, atualize as dependências se elas estiverem presentes no DTO
                if (atividadeDTO.dependencias() != null) {
                    // Certifique-se de que as dependências existem no banco de dados
                    List<Long> dependenciasIds = atividadeDTO.dependencias();
                    List<Atividade> dependencias = atividadeRepository.findAllById(dependenciasIds);

                    // Converta a lista de dependências em uma lista de IDs
                    List<Long> dependenciasIdsFromEntities = dependencias.stream()
                            .map(Atividade::getId)
                            .toList(); // Alterado de collect(Collectors.toList()) para toList()

                    // Associe as dependências à atividade
                    entity.setDependencias(dependenciasIdsFromEntities);
                } else {
                    // Se não houver dependências no DTO, defina a lista de dependências como vazia
                    entity.setDependencias(Collections.emptyList());
                }

                // Salve a atividade atualizada no banco de dados
                entity = atividadeRepository.save(entity);

                // Retorne a versão atualizada da atividade como AtividadeDTO
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
