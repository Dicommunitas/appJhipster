package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LembreteMapperTest {

    private LembreteMapper lembreteMapper;

    @BeforeEach
    public void setUp() {
        lembreteMapper = new LembreteMapperImpl();
    }
}
