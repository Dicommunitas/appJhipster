package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Problema;
import com.operacional.controleoperacional.service.dto.ProblemaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Problema} and its DTO {@link ProblemaDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ProblemaMapper extends EntityMapper<ProblemaDTO, Problema> {
    @Mapping(target = "relator", source = "relator", qualifiedByName = "login")
    ProblemaDTO toDto(Problema s);

    @Named("descricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    ProblemaDTO toDtoDescricao(Problema problema);
}
