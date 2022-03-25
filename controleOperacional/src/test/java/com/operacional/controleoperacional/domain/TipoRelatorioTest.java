package com.operacional.controleoperacional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoRelatorioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoRelatorio.class);
        TipoRelatorio tipoRelatorio1 = new TipoRelatorio();
        tipoRelatorio1.setId(1L);
        TipoRelatorio tipoRelatorio2 = new TipoRelatorio();
        tipoRelatorio2.setId(tipoRelatorio1.getId());
        assertThat(tipoRelatorio1).isEqualTo(tipoRelatorio2);
        tipoRelatorio2.setId(2L);
        assertThat(tipoRelatorio1).isNotEqualTo(tipoRelatorio2);
        tipoRelatorio1.setId(null);
        assertThat(tipoRelatorio1).isNotEqualTo(tipoRelatorio2);
    }
}
