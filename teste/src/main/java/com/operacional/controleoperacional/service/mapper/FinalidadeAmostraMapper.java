package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Amostra;
import com.operacional.controleoperacional.domain.FinalidadeAmostra;
import com.operacional.controleoperacional.domain.TipoFinalidadeAmostra;
import com.operacional.controleoperacional.service.dto.AmostraDTO;
import com.operacional.controleoperacional.service.dto.FinalidadeAmostraDTO;
import com.operacional.controleoperacional.service.dto.TipoFinalidadeAmostraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinalidadeAmostra} and its DTO {@link FinalidadeAmostraDTO}.
 */
@Mapper(componentModel = "spring")
public interface FinalidadeAmostraMapper extends EntityMapper<FinalidadeAmostraDTO, FinalidadeAmostra> {
    @Mapping(target = "tipoFinalidadeAmostra", source = "tipoFinalidadeAmostra", qualifiedByName = "tipoFinalidadeAmostraDescricao")
    @Mapping(target = "amostra", source = "amostra", qualifiedByName = "amostraDataHoraColeta")
    FinalidadeAmostraDTO toDto(FinalidadeAmostra s);

    @Named("tipoFinalidadeAmostraDescricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    TipoFinalidadeAmostraDTO toDtoTipoFinalidadeAmostraDescricao(TipoFinalidadeAmostra tipoFinalidadeAmostra);

    @Named("amostraDataHoraColeta")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dataHoraColeta", source = "dataHoraColeta")
    AmostraDTO toDtoAmostraDataHoraColeta(Amostra amostra);
}
