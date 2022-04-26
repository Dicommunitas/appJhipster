package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Operacao;
import com.operacional.controleoperacional.domain.Produto;
import com.operacional.controleoperacional.domain.TipoOperacao;
import com.operacional.controleoperacional.service.dto.OperacaoDTO;
import com.operacional.controleoperacional.service.dto.ProdutoDTO;
import com.operacional.controleoperacional.service.dto.TipoOperacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Operacao} and its DTO {@link OperacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperacaoMapper extends EntityMapper<OperacaoDTO, Operacao> {
    @Mapping(target = "produto", source = "produto", qualifiedByName = "produtoNomeCurto")
    @Mapping(target = "tipoOperacao", source = "tipoOperacao", qualifiedByName = "tipoOperacaoDescricao")
    OperacaoDTO toDto(Operacao s);

    @Named("produtoNomeCurto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomeCurto", source = "nomeCurto")
    ProdutoDTO toDtoProdutoNomeCurto(Produto produto);

    @Named("tipoOperacaoDescricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    TipoOperacaoDTO toDtoTipoOperacaoDescricao(TipoOperacao tipoOperacao);
}
