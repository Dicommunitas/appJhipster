package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RelatorioMapperTest {

    private RelatorioMapper relatorioMapper;

    @BeforeEach
    public void setUp() {
        relatorioMapper = new RelatorioMapperImpl();
    }
}
