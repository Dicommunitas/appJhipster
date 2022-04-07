package com.operacional.controleoperacional.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.operacional.controleoperacional.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoRelatorioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoRelatorioDTO.class);
        TipoRelatorioDTO tipoRelatorioDTO1 = new TipoRelatorioDTO();
        tipoRelatorioDTO1.setId(1L);
        TipoRelatorioDTO tipoRelatorioDTO2 = new TipoRelatorioDTO();
        assertThat(tipoRelatorioDTO1).isNotEqualTo(tipoRelatorioDTO2);
        tipoRelatorioDTO2.setId(tipoRelatorioDTO1.getId());
        assertThat(tipoRelatorioDTO1).isEqualTo(tipoRelatorioDTO2);
        tipoRelatorioDTO2.setId(2L);
        assertThat(tipoRelatorioDTO1).isNotEqualTo(tipoRelatorioDTO2);
        tipoRelatorioDTO1.setId(null);
        assertThat(tipoRelatorioDTO1).isNotEqualTo(tipoRelatorioDTO2);
    }
}
