package com.operacional.controleoperacional.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.TipoFinalidadeAmostra} entity.
 */
@Schema(
    description = "Entidade TipoFinalidadeAmostra.\nIrá informar para qual finalidade a amostra\nserá utilizada, análise, arquivo...\n@author Diego."
)
public class TipoFinalidadeAmostraDTO implements Serializable {

    private Long id;

    /**
     * Descreve a finalidade.\nA principal informação da finalidade.\nPara o que a amostra será usada.
     */
    @NotNull
    @Schema(
        description = "Descreve a finalidade.\nA principal informação da finalidade.\nPara o que a amostra será usada.",
        required = true
    )
    private String descricao;

    /**
     * Indica se esse tipo de finalidade deve\nter obrigatoriedade de uso de lacre.
     */
    @NotNull
    @Schema(description = "Indica se esse tipo de finalidade deve\nter obrigatoriedade de uso de lacre.", required = true)
    private Boolean obrigatorioLacre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getObrigatorioLacre() {
        return obrigatorioLacre;
    }

    public void setObrigatorioLacre(Boolean obrigatorioLacre) {
        this.obrigatorioLacre = obrigatorioLacre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoFinalidadeAmostraDTO)) {
            return false;
        }

        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = (TipoFinalidadeAmostraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoFinalidadeAmostraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoFinalidadeAmostraDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", obrigatorioLacre='" + getObrigatorioLacre() + "'" +
            "}";
    }
}
