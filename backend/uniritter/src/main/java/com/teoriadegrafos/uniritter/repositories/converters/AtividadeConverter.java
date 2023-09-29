package com.teoriadegrafos.uniritter.repositories.converters;

import com.teoriadegrafos.uniritter.entities.Atividade;
import com.teoriadegrafos.uniritter.services.impl.bos.AtividadeBO;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Configuration;

@Configuration
@Mapper(componentModel= "spring")
public interface AtividadeConverter {

//    @Mapping(source = "garantiaAvalista.valor", target = "garantiaAvalista")
    Atividade atitivdadeToEntity(AtividadeBO atividade);
    AtividadeBO atitivdadeToBo(Atividade atividade);
}
