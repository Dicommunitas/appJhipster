package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperacaoMapperTest {

    private OperacaoMapper operacaoMapper;

    @BeforeEach
    public void setUp() {
        operacaoMapper = new OperacaoMapperImpl();
    }
}
