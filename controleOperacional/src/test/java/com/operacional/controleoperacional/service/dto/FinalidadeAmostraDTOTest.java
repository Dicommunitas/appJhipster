package com.operacional.controleoperacional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinalidadeAmostraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinalidadeAmostraDTO.class);
        FinalidadeAmostraDTO finalidadeAmostraDTO1 = new FinalidadeAmostraDTO();
        finalidadeAmostraDTO1.setId(1L);
        FinalidadeAmostraDTO finalidadeAmostraDTO2 = new FinalidadeAmostraDTO();
        assertThat(finalidadeAmostraDTO1).isNotEqualTo(finalidadeAmostraDTO2);
        finalidadeAmostraDTO2.setId(finalidadeAmostraDTO1.getId());
        assertThat(finalidadeAmostraDTO1).isEqualTo(finalidadeAmostraDTO2);
        finalidadeAmostraDTO2.setId(2L);
        assertThat(finalidadeAmostraDTO1).isNotEqualTo(finalidadeAmostraDTO2);
        finalidadeAmostraDTO1.setId(null);
        assertThat(finalidadeAmostraDTO1).isNotEqualTo(finalidadeAmostraDTO2);
    }
}
