package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Amostra;
import com.operacional.controleoperacional.service.dto.AmostraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Amostra} and its DTO {@link AmostraDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { OperacaoMapper.class, OrigemAmostraMapper.class, ProdutoMapper.class, TipoAmostraMapper.class, UsuarioMapper.class }
)
public interface AmostraMapper extends EntityMapper<AmostraDTO, Amostra> {
    @Mapping(target = "operacao", source = "operacao", qualifiedByName = "descricao")
    @Mapping(target = "origemAmostra", source = "origemAmostra", qualifiedByName = "descricao")
    @Mapping(target = "produto", source = "produto", qualifiedByName = "nomeCurto")
    @Mapping(target = "tipoAmostra", source = "tipoAmostra", qualifiedByName = "descricao")
    @Mapping(target = "amostradaPor", source = "amostradaPor", qualifiedByName = "nome")
    @Mapping(target = "recebidaPor", source = "recebidaPor", qualifiedByName = "nome")
    AmostraDTO toDto(Amostra s);

    @Named("dataHora")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dataHora", source = "dataHora")
    AmostraDTO toDtoDataHora(Amostra amostra);
}
