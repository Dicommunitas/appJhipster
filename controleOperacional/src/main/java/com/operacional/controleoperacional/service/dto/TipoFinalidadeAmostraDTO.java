package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.TipoFinalidadeAmostra} entity.
 */
@ApiModel(
    description = "Entidade TipoFinalidadeAmostra.\nIrá informar para qual finalidade a amostra\nserá utilizada, análise, arqivo...\n@author Diego."
)
public class TipoFinalidadeAmostraDTO implements Serializable {

    private Long id;

    /**
     * Atributo descrição.\nIrá informar a descrição da finalidade.\nA principal informação da finalidade.\nAnálise, arqivo...
     */
    @NotNull
    @ApiModelProperty(
        value = "Atributo descrição.\nIrá informar a descrição da finalidade.\nA principal informação da finalidade.\nAnálise, arqivo...",
        required = true
    )
    private String descricao;

    /**
     * Amostras que tenham alguma finalidade sem lacre\ncom obrigatoriedade de lacre não poderão ser impressas,\nporém podem ser criadas.
     */
    @NotNull
    @ApiModelProperty(
        value = "Amostras que tenham alguma finalidade sem lacre\ncom obrigatoriedade de lacre não poderão ser impressas,\nporém podem ser criadas.",
        required = true
    )
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
