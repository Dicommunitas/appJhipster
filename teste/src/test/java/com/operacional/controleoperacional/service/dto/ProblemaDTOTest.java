package com.operacional.controleoperacional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProblemaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProblemaDTO.class);
        ProblemaDTO problemaDTO1 = new ProblemaDTO();
        problemaDTO1.setId(1L);
        ProblemaDTO problemaDTO2 = new ProblemaDTO();
        assertThat(problemaDTO1).isNotEqualTo(problemaDTO2);
        problemaDTO2.setId(problemaDTO1.getId());
        assertThat(problemaDTO1).isEqualTo(problemaDTO2);
        problemaDTO2.setId(2L);
        assertThat(problemaDTO1).isNotEqualTo(problemaDTO2);
        problemaDTO1.setId(null);
        assertThat(problemaDTO1).isNotEqualTo(problemaDTO2);
    }
}
