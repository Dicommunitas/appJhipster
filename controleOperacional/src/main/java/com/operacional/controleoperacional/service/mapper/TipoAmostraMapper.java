package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.TipoAmostra;
import com.operacional.controleoperacional.service.dto.TipoAmostraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoAmostra} and its DTO {@link TipoAmostraDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoAmostraMapper extends EntityMapper<TipoAmostraDTO, TipoAmostra> {}
