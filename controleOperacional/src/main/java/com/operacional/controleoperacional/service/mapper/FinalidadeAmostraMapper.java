package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.FinalidadeAmostra;
import com.operacional.controleoperacional.service.dto.FinalidadeAmostraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinalidadeAmostra} and its DTO {@link FinalidadeAmostraDTO}.
 */
@Mapper(componentModel = "spring", uses = { TipoFinalidadeAmostraMapper.class, AmostraMapper.class })
public interface FinalidadeAmostraMapper extends EntityMapper<FinalidadeAmostraDTO, FinalidadeAmostra> {
    @Mapping(target = "tipoFinalidadeAmostra", source = "tipoFinalidadeAmostra", qualifiedByName = "descricao")
    @Mapping(target = "amostra", source = "amostra", qualifiedByName = "dataHora")
    FinalidadeAmostraDTO toDto(FinalidadeAmostra s);
}
