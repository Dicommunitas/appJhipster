package com.operacional.controleoperacional.service.mapper;

import com.operacional.controleoperacional.domain.AlertaProduto;
import com.operacional.controleoperacional.service.dto.AlertaProdutoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlertaProduto} and its DTO {@link AlertaProdutoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlertaProdutoMapper extends EntityMapper<AlertaProdutoDTO, AlertaProduto> {}
