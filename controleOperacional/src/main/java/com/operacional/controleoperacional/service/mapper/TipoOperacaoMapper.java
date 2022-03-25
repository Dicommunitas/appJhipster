package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.TipoOperacao;
import com.operacional.controleoperacional.service.dto.TipoOperacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoOperacao} and its DTO {@link TipoOperacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoOperacaoMapper extends EntityMapper<TipoOperacaoDTO, TipoOperacao> {
    @Named("descricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    TipoOperacaoDTO toDtoDescricao(TipoOperacao tipoOperacao);
}
