package com.operacional.controleoperacional.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProblemaMapperTest {

    private ProblemaMapper problemaMapper;

    @BeforeEach
    public void setUp() {
        problemaMapper = new ProblemaMapperImpl();
    }
}
