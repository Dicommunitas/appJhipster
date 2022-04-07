package com.operacional.controleoperacional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoOperacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoOperacao.class);
        TipoOperacao tipoOperacao1 = new TipoOperacao();
        tipoOperacao1.setId(1L);
        TipoOperacao tipoOperacao2 = new TipoOperacao();
        tipoOperacao2.setId(tipoOperacao1.getId());
        assertThat(tipoOperacao1).isEqualTo(tipoOperacao2);
        tipoOperacao2.setId(2L);
        assertThat(tipoOperacao1).isNotEqualTo(tipoOperacao2);
        tipoOperacao1.setId(null);
        assertThat(tipoOperacao1).isNotEqualTo(tipoOperacao2);
    }
}
