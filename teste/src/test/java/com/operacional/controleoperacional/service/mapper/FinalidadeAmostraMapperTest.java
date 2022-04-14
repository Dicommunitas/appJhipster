package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FinalidadeAmostraMapperTest {

    private FinalidadeAmostraMapper finalidadeAmostraMapper;

    @BeforeEach
    public void setUp() {
        finalidadeAmostraMapper = new FinalidadeAmostraMapperImpl();
    }
}
