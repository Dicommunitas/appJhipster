package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Produto;
import com.operacional.controleoperacional.service.dto.ProdutoDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Produto} and its DTO {@link ProdutoDTO}.
 */
@Mapper(componentModel = "spring", uses = { AlertaProdutoMapper.class })
public interface ProdutoMapper extends EntityMapper<ProdutoDTO, Produto> {
    @Mapping(target = "alertas", source = "alertas", qualifiedByName = "descricaoSet")
    ProdutoDTO toDto(Produto s);

    @Mapping(target = "removeAlertas", ignore = true)
    Produto toEntity(ProdutoDTO produtoDTO);

    @Named("nomeCurto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomeCurto", source = "nomeCurto")
    ProdutoDTO toDtoNomeCurto(Produto produto);
}
