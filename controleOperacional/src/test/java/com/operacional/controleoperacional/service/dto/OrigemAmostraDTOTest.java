package com.operacional.controleoperacional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrigemAmostraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrigemAmostraDTO.class);
        OrigemAmostraDTO origemAmostraDTO1 = new OrigemAmostraDTO();
        origemAmostraDTO1.setId(1L);
        OrigemAmostraDTO origemAmostraDTO2 = new OrigemAmostraDTO();
        assertThat(origemAmostraDTO1).isNotEqualTo(origemAmostraDTO2);
        origemAmostraDTO2.setId(origemAmostraDTO1.getId());
        assertThat(origemAmostraDTO1).isEqualTo(origemAmostraDTO2);
        origemAmostraDTO2.setId(2L);
        assertThat(origemAmostraDTO1).isNotEqualTo(origemAmostraDTO2);
        origemAmostraDTO1.setId(null);
        assertThat(origemAmostraDTO1).isNotEqualTo(origemAmostraDTO2);
    }
}
