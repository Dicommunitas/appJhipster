package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Operacao;
import com.operacional.controleoperacional.service.dto.OperacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Operacao} and its DTO {@link OperacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = { TipoOperacaoMapper.class })
public interface OperacaoMapper extends EntityMapper<OperacaoDTO, Operacao> {
    @Mapping(target = "tipoOperacao", source = "tipoOperacao", qualifiedByName = "descricao")
    OperacaoDTO toDto(Operacao s);

    @Named("descricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    OperacaoDTO toDtoDescricao(Operacao operacao);
}
