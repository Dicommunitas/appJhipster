package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoFinalidadeAmostraMapperTest {

    private TipoFinalidadeAmostraMapper tipoFinalidadeAmostraMapper;

    @BeforeEach
    public void setUp() {
        tipoFinalidadeAmostraMapper = new TipoFinalidadeAmostraMapperImpl();
    }
}
