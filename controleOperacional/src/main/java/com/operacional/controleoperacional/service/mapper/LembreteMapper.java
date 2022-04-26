package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Lembrete;
import com.operacional.controleoperacional.domain.TipoOperacao;
import com.operacional.controleoperacional.domain.TipoRelatorio;
import com.operacional.controleoperacional.service.dto.LembreteDTO;
import com.operacional.controleoperacional.service.dto.TipoOperacaoDTO;
import com.operacional.controleoperacional.service.dto.TipoRelatorioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lembrete} and its DTO {@link LembreteDTO}.
 */
@Mapper(componentModel = "spring")
public interface LembreteMapper extends EntityMapper<LembreteDTO, Lembrete> {
    @Mapping(target = "tipoRelatorio", source = "tipoRelatorio", qualifiedByName = "tipoRelatorioNome")
    @Mapping(target = "tipoOperacao", source = "tipoOperacao", qualifiedByName = "tipoOperacaoDescricao")
    LembreteDTO toDto(Lembrete s);

    @Named("tipoRelatorioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    TipoRelatorioDTO toDtoTipoRelatorioNome(TipoRelatorio tipoRelatorio);

    @Named("tipoOperacaoDescricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    TipoOperacaoDTO toDtoTipoOperacaoDescricao(TipoOperacao tipoOperacao);
}
