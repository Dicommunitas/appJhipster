package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.AlertaProduto;
import com.operacional.controleoperacional.domain.Produto;
import com.operacional.controleoperacional.service.dto.AlertaProdutoDTO;
import com.operacional.controleoperacional.service.dto.ProdutoDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Produto} and its DTO {@link ProdutoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProdutoMapper extends EntityMapper<ProdutoDTO, Produto> {
    @Mapping(target = "alertas", source = "alertas", qualifiedByName = "alertaProdutoDescricaoSet")
    ProdutoDTO toDto(Produto s);

    @Mapping(target = "removeAlertas", ignore = true)
    Produto toEntity(ProdutoDTO produtoDTO);

    @Named("alertaProdutoDescricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    AlertaProdutoDTO toDtoAlertaProdutoDescricao(AlertaProduto alertaProduto);

    @Named("alertaProdutoDescricaoSet")
    default Set<AlertaProdutoDTO> toDtoAlertaProdutoDescricaoSet(Set<AlertaProduto> alertaProduto) {
        return alertaProduto.stream().map(this::toDtoAlertaProdutoDescricao).collect(Collectors.toSet());
    }
}
