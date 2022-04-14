package com.operacional.controleoperacional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AmostraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmostraDTO.class);
        AmostraDTO amostraDTO1 = new AmostraDTO();
        amostraDTO1.setId(1L);
        AmostraDTO amostraDTO2 = new AmostraDTO();
        assertThat(amostraDTO1).isNotEqualTo(amostraDTO2);
        amostraDTO2.setId(amostraDTO1.getId());
        assertThat(amostraDTO1).isEqualTo(amostraDTO2);
        amostraDTO2.setId(2L);
        assertThat(amostraDTO1).isNotEqualTo(amostraDTO2);
        amostraDTO1.setId(null);
        assertThat(amostraDTO1).isNotEqualTo(amostraDTO2);
    }
}
