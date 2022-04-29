package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Problema;
import com.operacional.controleoperacional.domain.Status;
import com.operacional.controleoperacional.domain.User;
import com.operacional.controleoperacional.service.dto.ProblemaDTO;
import com.operacional.controleoperacional.service.dto.StatusDTO;
import com.operacional.controleoperacional.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Status} and its DTO {@link StatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface StatusMapper extends EntityMapper<StatusDTO, Status> {
    @Mapping(target = "relator", source = "relator", qualifiedByName = "userLogin")
    @Mapping(target = "responsavel", source = "responsavel", qualifiedByName = "userLogin")
    @Mapping(target = "problema", source = "problema", qualifiedByName = "problemaDescricao")
    StatusDTO toDto(Status s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("problemaDescricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    ProblemaDTO toDtoProblemaDescricao(Problema problema);
}
