package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.TipoFinalidadeAmostra;
import com.operacional.controleoperacional.service.dto.TipoFinalidadeAmostraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoFinalidadeAmostra} and its DTO {@link TipoFinalidadeAmostraDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoFinalidadeAmostraMapper extends EntityMapper<TipoFinalidadeAmostraDTO, TipoFinalidadeAmostra> {
    @Named("descricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    TipoFinalidadeAmostraDTO toDtoDescricao(TipoFinalidadeAmostra tipoFinalidadeAmostra);
}
