package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Relatorio;
import com.operacional.controleoperacional.service.dto.RelatorioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Relatorio} and its DTO {@link RelatorioDTO}.
 */
@Mapper(componentModel = "spring", uses = { TipoRelatorioMapper.class, UserMapper.class })
public interface RelatorioMapper extends EntityMapper<RelatorioDTO, Relatorio> {
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "nome")
    @Mapping(target = "responsavel", source = "responsavel", qualifiedByName = "login")
    RelatorioDTO toDto(Relatorio s);
}
