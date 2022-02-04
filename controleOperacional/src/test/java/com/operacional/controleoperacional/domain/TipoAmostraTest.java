package com.operacional.controleoperacional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoAmostraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoAmostra.class);
        TipoAmostra tipoAmostra1 = new TipoAmostra();
        tipoAmostra1.setId(1L);
        TipoAmostra tipoAmostra2 = new TipoAmostra();
        tipoAmostra2.setId(tipoAmostra1.getId());
        assertThat(tipoAmostra1).isEqualTo(tipoAmostra2);
        tipoAmostra2.setId(2L);
        assertThat(tipoAmostra1).isNotEqualTo(tipoAmostra2);
        tipoAmostra1.setId(null);
        assertThat(tipoAmostra1).isNotEqualTo(tipoAmostra2);
    }
}
