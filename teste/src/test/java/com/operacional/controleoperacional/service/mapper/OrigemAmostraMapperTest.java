package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrigemAmostraMapperTest {

    private OrigemAmostraMapper origemAmostraMapper;

    @BeforeEach
    public void setUp() {
        origemAmostraMapper = new OrigemAmostraMapperImpl();
    }
}
