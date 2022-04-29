package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AmostraMapperTest {

    private AmostraMapper amostraMapper;

    @BeforeEach
    public void setUp() {
        amostraMapper = new AmostraMapperImpl();
    }
}
