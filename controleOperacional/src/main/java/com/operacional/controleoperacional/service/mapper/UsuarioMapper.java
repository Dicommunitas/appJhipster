package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Usuario;
import com.operacional.controleoperacional.service.dto.UsuarioDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Usuario} and its DTO {@link UsuarioDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface UsuarioMapper extends EntityMapper<UsuarioDTO, Usuario> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    UsuarioDTO toDto(Usuario s);

    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    UsuarioDTO toDtoNome(Usuario usuario);

    @Named("nomeSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    Set<UsuarioDTO> toDtoNomeSet(Set<Usuario> usuario);
}
