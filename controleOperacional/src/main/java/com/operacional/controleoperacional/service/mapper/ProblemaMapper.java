package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Problema;
import com.operacional.controleoperacional.domain.User;
import com.operacional.controleoperacional.service.dto.ProblemaDTO;
import com.operacional.controleoperacional.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Problema} and its DTO {@link ProblemaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProblemaMapper extends EntityMapper<ProblemaDTO, Problema> {
    @Mapping(target = "relator", source = "relator", qualifiedByName = "userLogin")
    ProblemaDTO toDto(Problema s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
