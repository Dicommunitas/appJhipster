package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlertaProdutoMapperTest {

    private AlertaProdutoMapper alertaProdutoMapper;

    @BeforeEach
    public void setUp() {
        alertaProdutoMapper = new AlertaProdutoMapperImpl();
    }
}
