package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Relatorio;
import com.operacional.controleoperacional.domain.TipoRelatorio;
import com.operacional.controleoperacional.domain.User;
import com.operacional.controleoperacional.service.dto.RelatorioDTO;
import com.operacional.controleoperacional.service.dto.TipoRelatorioDTO;
import com.operacional.controleoperacional.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Relatorio} and its DTO {@link RelatorioDTO}.
 */
@Mapper(componentModel = "spring")
public interface RelatorioMapper extends EntityMapper<RelatorioDTO, Relatorio> {
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoRelatorioNome")
    @Mapping(target = "responsavel", source = "responsavel", qualifiedByName = "userLogin")
    RelatorioDTO toDto(Relatorio s);

    @Named("tipoRelatorioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    TipoRelatorioDTO toDtoTipoRelatorioNome(TipoRelatorio tipoRelatorio);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
