package com.operacional.controleoperacional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LembreteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LembreteDTO.class);
        LembreteDTO lembreteDTO1 = new LembreteDTO();
        lembreteDTO1.setId(1L);
        LembreteDTO lembreteDTO2 = new LembreteDTO();
        assertThat(lembreteDTO1).isNotEqualTo(lembreteDTO2);
        lembreteDTO2.setId(lembreteDTO1.getId());
        assertThat(lembreteDTO1).isEqualTo(lembreteDTO2);
        lembreteDTO2.setId(2L);
        assertThat(lembreteDTO1).isNotEqualTo(lembreteDTO2);
        lembreteDTO1.setId(null);
        assertThat(lembreteDTO1).isNotEqualTo(lembreteDTO2);
    }
}
