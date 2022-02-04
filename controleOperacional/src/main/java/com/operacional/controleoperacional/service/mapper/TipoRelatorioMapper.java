package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.TipoRelatorio;
import com.operacional.controleoperacional.service.dto.TipoRelatorioDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoRelatorio} and its DTO {@link TipoRelatorioDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsuarioMapper.class })
public interface TipoRelatorioMapper extends EntityMapper<TipoRelatorioDTO, TipoRelatorio> {
    @Mapping(target = "usuariosAuts", source = "usuariosAuts", qualifiedByName = "nomeSet")
    TipoRelatorioDTO toDto(TipoRelatorio s);

    @Mapping(target = "removeUsuariosAut", ignore = true)
    TipoRelatorio toEntity(TipoRelatorioDTO tipoRelatorioDTO);

    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    TipoRelatorioDTO toDtoNome(TipoRelatorio tipoRelatorio);
}
