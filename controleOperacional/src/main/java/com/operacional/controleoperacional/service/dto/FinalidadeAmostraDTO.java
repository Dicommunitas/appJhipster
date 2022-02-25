package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.FinalidadeAmostra} entity.
 */
@ApiModel(description = "Entidade FinalidadeAmostra.\n@author Diego.")
public class FinalidadeAmostraDTO implements Serializable {

    private Long id;

    /**
     * O número do lacre do recipiente da amostra.\nAmostras que tenham alguma finalidade com\nobrigatoriedade de lacre porém estejam sem lacre\nnão poderão ser impressas, mas podem ser criadas.
     */

    @ApiModelProperty(
        value = "O número do lacre do recipiente da amostra.\nAmostras que tenham alguma finalidade com\nobrigatoriedade de lacre porém estejam sem lacre\nnão poderão ser impressas, mas podem ser criadas."
    )
    private Integer lacre;

    private TipoFinalidadeAmostraDTO tipoFinalidadeAmostra;

    private AmostraDTO amostra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLacre() {
        return lacre;
    }

    public void setLacre(Integer lacre) {
        this.lacre = lacre;
    }

    public TipoFinalidadeAmostraDTO getTipoFinalidadeAmostra() {
        return tipoFinalidadeAmostra;
    }

    public void setTipoFinalidadeAmostra(TipoFinalidadeAmostraDTO tipoFinalidadeAmostra) {
        this.tipoFinalidadeAmostra = tipoFinalidadeAmostra;
    }

    public AmostraDTO getAmostra() {
        return amostra;
    }

    public void setAmostra(AmostraDTO amostra) {
        this.amostra = amostra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinalidadeAmostraDTO)) {
            return false;
        }

        FinalidadeAmostraDTO finalidadeAmostraDTO = (FinalidadeAmostraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, finalidadeAmostraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinalidadeAmostraDTO{" +
            "id=" + getId() +
            ", lacre=" + getLacre() +
            ", tipoFinalidadeAmostra=" + getTipoFinalidadeAmostra() +
            ", amostra=" + getAmostra() +
            "}";
    }
}
