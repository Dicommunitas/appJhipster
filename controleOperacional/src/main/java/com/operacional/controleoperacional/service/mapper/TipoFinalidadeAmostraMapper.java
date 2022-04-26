package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.TipoFinalidadeAmostra;
import com.operacional.controleoperacional.service.dto.TipoFinalidadeAmostraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoFinalidadeAmostra} and its DTO {@link TipoFinalidadeAmostraDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoFinalidadeAmostraMapper extends EntityMapper<TipoFinalidadeAmostraDTO, TipoFinalidadeAmostra> {}
