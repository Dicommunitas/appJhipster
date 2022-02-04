package com.operacional.controleoperacional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoFinalidadeAmostraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoFinalidadeAmostraDTO.class);
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO1 = new TipoFinalidadeAmostraDTO();
        tipoFinalidadeAmostraDTO1.setId(1L);
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO2 = new TipoFinalidadeAmostraDTO();
        assertThat(tipoFinalidadeAmostraDTO1).isNotEqualTo(tipoFinalidadeAmostraDTO2);
        tipoFinalidadeAmostraDTO2.setId(tipoFinalidadeAmostraDTO1.getId());
        assertThat(tipoFinalidadeAmostraDTO1).isEqualTo(tipoFinalidadeAmostraDTO2);
        tipoFinalidadeAmostraDTO2.setId(2L);
        assertThat(tipoFinalidadeAmostraDTO1).isNotEqualTo(tipoFinalidadeAmostraDTO2);
        tipoFinalidadeAmostraDTO1.setId(null);
        assertThat(tipoFinalidadeAmostraDTO1).isNotEqualTo(tipoFinalidadeAmostraDTO2);
    }
}
