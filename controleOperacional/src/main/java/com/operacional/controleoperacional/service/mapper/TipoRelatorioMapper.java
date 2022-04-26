package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.TipoRelatorio;
import com.operacional.controleoperacional.domain.User;
import com.operacional.controleoperacional.service.dto.TipoRelatorioDTO;
import com.operacional.controleoperacional.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoRelatorio} and its DTO {@link TipoRelatorioDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoRelatorioMapper extends EntityMapper<TipoRelatorioDTO, TipoRelatorio> {
    @Mapping(target = "usuariosAuts", source = "usuariosAuts", qualifiedByName = "userLoginSet")
    TipoRelatorioDTO toDto(TipoRelatorio s);

    @Mapping(target = "removeUsuariosAut", ignore = true)
    TipoRelatorio toEntity(TipoRelatorioDTO tipoRelatorioDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("userLoginSet")
    default Set<UserDTO> toDtoUserLoginSet(Set<User> user) {
        return user.stream().map(this::toDtoUserLogin).collect(Collectors.toSet());
    }
}
