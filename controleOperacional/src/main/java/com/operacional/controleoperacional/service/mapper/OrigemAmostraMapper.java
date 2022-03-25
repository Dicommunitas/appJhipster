package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.OrigemAmostra;
import com.operacional.controleoperacional.service.dto.OrigemAmostraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrigemAmostra} and its DTO {@link OrigemAmostraDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrigemAmostraMapper extends EntityMapper<OrigemAmostraDTO, OrigemAmostra> {
    @Named("descricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    OrigemAmostraDTO toDtoDescricao(OrigemAmostra origemAmostra);
}
