package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoOperacaoMapperTest {

    private TipoOperacaoMapper tipoOperacaoMapper;

    @BeforeEach
    public void setUp() {
        tipoOperacaoMapper = new TipoOperacaoMapperImpl();
    }
}
