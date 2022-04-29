package com.operacional.controleoperacional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperacaoDTO.class);
        OperacaoDTO operacaoDTO1 = new OperacaoDTO();
        operacaoDTO1.setId(1L);
        OperacaoDTO operacaoDTO2 = new OperacaoDTO();
        assertThat(operacaoDTO1).isNotEqualTo(operacaoDTO2);
        operacaoDTO2.setId(operacaoDTO1.getId());
        assertThat(operacaoDTO1).isEqualTo(operacaoDTO2);
        operacaoDTO2.setId(2L);
        assertThat(operacaoDTO1).isNotEqualTo(operacaoDTO2);
        operacaoDTO1.setId(null);
        assertThat(operacaoDTO1).isNotEqualTo(operacaoDTO2);
    }
}
