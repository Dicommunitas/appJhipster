package com.operacional.controleoperacional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrigemAmostraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrigemAmostra.class);
        OrigemAmostra origemAmostra1 = new OrigemAmostra();
        origemAmostra1.setId(1L);
        OrigemAmostra origemAmostra2 = new OrigemAmostra();
        origemAmostra2.setId(origemAmostra1.getId());
        assertThat(origemAmostra1).isEqualTo(origemAmostra2);
        origemAmostra2.setId(2L);
        assertThat(origemAmostra1).isNotEqualTo(origemAmostra2);
        origemAmostra1.setId(null);
        assertThat(origemAmostra1).isNotEqualTo(origemAmostra2);
    }
}
