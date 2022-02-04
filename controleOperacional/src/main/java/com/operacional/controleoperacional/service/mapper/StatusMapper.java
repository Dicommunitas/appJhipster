package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Status;
import com.operacional.controleoperacional.service.dto.StatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Status} and its DTO {@link StatusDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsuarioMapper.class, ProblemaMapper.class })
public interface StatusMapper extends EntityMapper<StatusDTO, Status> {
    @Mapping(target = "relator", source = "relator", qualifiedByName = "nome")
    @Mapping(target = "responsavel", source = "responsavel", qualifiedByName = "nome")
    @Mapping(target = "problema", source = "problema", qualifiedByName = "descricao")
    StatusDTO toDto(Status s);
}
