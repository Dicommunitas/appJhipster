package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoRelatorioMapperTest {

    private TipoRelatorioMapper tipoRelatorioMapper;

    @BeforeEach
    public void setUp() {
        tipoRelatorioMapper = new TipoRelatorioMapperImpl();
    }
}
