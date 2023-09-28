package com.teoriadegrafos.uniritter.repositories.converters;

import com.teoriadegrafos.uniritter.entities.Projeto;
import com.teoriadegrafos.uniritter.services.impl.bos.ProjetoBO;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Configuration;

@Configuration
@Mapper(componentModel= "spring")
public interface ProjetoConverter {
    Projeto projetoToEntity(ProjetoBO projeto);
    ProjetoBO projetoToBo(Projeto projeto);
}
