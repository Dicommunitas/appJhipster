package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Lembrete;
import com.operacional.controleoperacional.service.dto.LembreteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lembrete} and its DTO {@link LembreteDTO}.
 */
@Mapper(componentModel = "spring", uses = { TipoRelatorioMapper.class, TipoOperacaoMapper.class })
public interface LembreteMapper extends EntityMapper<LembreteDTO, Lembrete> {
    @Mapping(target = "tipoRelatorio", source = "tipoRelatorio", qualifiedByName = "nome")
    @Mapping(target = "tipoOperacao", source = "tipoOperacao", qualifiedByName = "descricao")
    LembreteDTO toDto(Lembrete s);
}
