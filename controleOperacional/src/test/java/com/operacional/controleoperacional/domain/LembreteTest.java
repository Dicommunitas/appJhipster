package com.operacional.controleoperacional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LembreteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lembrete.class);
        Lembrete lembrete1 = new Lembrete();
        lembrete1.setId(1L);
        Lembrete lembrete2 = new Lembrete();
        lembrete2.setId(lembrete1.getId());
        assertThat(lembrete1).isEqualTo(lembrete2);
        lembrete2.setId(2L);
        assertThat(lembrete1).isNotEqualTo(lembrete2);
        lembrete1.setId(null);
        assertThat(lembrete1).isNotEqualTo(lembrete2);
    }
}
