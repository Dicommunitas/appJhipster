package com.operacional.controleoperacional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RelatorioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RelatorioDTO.class);
        RelatorioDTO relatorioDTO1 = new RelatorioDTO();
        relatorioDTO1.setId(1L);
        RelatorioDTO relatorioDTO2 = new RelatorioDTO();
        assertThat(relatorioDTO1).isNotEqualTo(relatorioDTO2);
        relatorioDTO2.setId(relatorioDTO1.getId());
        assertThat(relatorioDTO1).isEqualTo(relatorioDTO2);
        relatorioDTO2.setId(2L);
        assertThat(relatorioDTO1).isNotEqualTo(relatorioDTO2);
        relatorioDTO1.setId(null);
        assertThat(relatorioDTO1).isNotEqualTo(relatorioDTO2);
    }
}
