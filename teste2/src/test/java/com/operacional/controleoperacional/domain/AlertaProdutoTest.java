package com.operacional.controleoperacional.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlertaProdutoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlertaProduto.class);
        AlertaProduto alertaProduto1 = new AlertaProduto();
        alertaProduto1.setId(1L);
        AlertaProduto alertaProduto2 = new AlertaProduto();
        alertaProduto2.setId(alertaProduto1.getId());
        assertThat(alertaProduto1).isEqualTo(alertaProduto2);
        alertaProduto2.setId(2L);
        assertThat(alertaProduto1).isNotEqualTo(alertaProduto2);
        alertaProduto1.setId(null);
        assertThat(alertaProduto1).isNotEqualTo(alertaProduto2);
    }
}
