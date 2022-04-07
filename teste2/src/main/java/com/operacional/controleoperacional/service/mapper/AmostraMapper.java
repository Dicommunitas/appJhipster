package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.Amostra;
import com.operacional.controleoperacional.domain.Operacao;
import com.operacional.controleoperacional.domain.OrigemAmostra;
import com.operacional.controleoperacional.domain.TipoAmostra;
import com.operacional.controleoperacional.domain.User;
import com.operacional.controleoperacional.service.dto.AmostraDTO;
import com.operacional.controleoperacional.service.dto.OperacaoDTO;
import com.operacional.controleoperacional.service.dto.OrigemAmostraDTO;
import com.operacional.controleoperacional.service.dto.TipoAmostraDTO;
import com.operacional.controleoperacional.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Amostra} and its DTO {@link AmostraDTO}.
 */
@Mapper(componentModel = "spring")
public interface AmostraMapper extends EntityMapper<AmostraDTO, Amostra> {
    @Mapping(target = "operacao", source = "operacao", qualifiedByName = "operacaoDescricao")
    @Mapping(target = "origemAmostra", source = "origemAmostra", qualifiedByName = "origemAmostraDescricao")
    @Mapping(target = "tipoAmostra", source = "tipoAmostra", qualifiedByName = "tipoAmostraDescricao")
    @Mapping(target = "amostradaPor", source = "amostradaPor", qualifiedByName = "userLogin")
    @Mapping(target = "recebidaPor", source = "recebidaPor", qualifiedByName = "userLogin")
    AmostraDTO toDto(Amostra s);

    @Named("operacaoDescricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    OperacaoDTO toDtoOperacaoDescricao(Operacao operacao);

    @Named("origemAmostraDescricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    OrigemAmostraDTO toDtoOrigemAmostraDescricao(OrigemAmostra origemAmostra);

    @Named("tipoAmostraDescricao")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "descricao", source = "descricao")
    TipoAmostraDTO toDtoTipoAmostraDescricao(TipoAmostra tipoAmostra);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
