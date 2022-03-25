package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoAmostraMapperTest {

    private TipoAmostraMapper tipoAmostraMapper;

    @BeforeEach
    public void setUp() {
        tipoAmostraMapper = new TipoAmostraMapperImpl();
    }
}
