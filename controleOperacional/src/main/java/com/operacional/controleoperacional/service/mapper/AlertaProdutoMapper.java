package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.AlertaProduto;
import com.operacional.controleoperacional.service.dto.AlertaProdutoDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlertaProduto} and its DTO {@link AlertaProdutoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlertaProdutoMapper extends EntityMapper<AlertaProdutoDTO, AlertaProduto> {
    @Named("descricaoSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    Set<AlertaProdutoDTO> toDtoDescricaoSet(Set<AlertaProduto> alertaProduto);
}
