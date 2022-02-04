package com.operacional.controleoperacional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlertaProdutoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlertaProdutoDTO.class);
        AlertaProdutoDTO alertaProdutoDTO1 = new AlertaProdutoDTO();
        alertaProdutoDTO1.setId(1L);
        AlertaProdutoDTO alertaProdutoDTO2 = new AlertaProdutoDTO();
        assertThat(alertaProdutoDTO1).isNotEqualTo(alertaProdutoDTO2);
        alertaProdutoDTO2.setId(alertaProdutoDTO1.getId());
        assertThat(alertaProdutoDTO1).isEqualTo(alertaProdutoDTO2);
        alertaProdutoDTO2.setId(2L);
        assertThat(alertaProdutoDTO1).isNotEqualTo(alertaProdutoDTO2);
        alertaProdutoDTO1.setId(null);
        assertThat(alertaProdutoDTO1).isNotEqualTo(alertaProdutoDTO2);
    }
}
