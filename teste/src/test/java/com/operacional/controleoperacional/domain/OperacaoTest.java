package com.operacional.controleoperacional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operacao.class);
        Operacao operacao1 = new Operacao();
        operacao1.setId(1L);
        Operacao operacao2 = new Operacao();
        operacao2.setId(operacao1.getId());
        assertThat(operacao1).isEqualTo(operacao2);
        operacao2.setId(2L);
        assertThat(operacao1).isNotEqualTo(operacao2);
        operacao1.setId(null);
        assertThat(operacao1).isNotEqualTo(operacao2);
    }
}
