package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.OrigemAmostra} entity.
 */
@ApiModel(
    description = "Entidade OrigemAmostra, irá descrever de onde\na amostra foi retirada, linha 01, tanque 02...\nO local onde a amostra foi coletada.\n@author Diego."
)
public class OrigemAmostraDTO implements Serializable {

    private Long id;

    /**
     * Descreve de forma simples a origem de uma amostra.\nO local da coleta, linha, tanque...\nA principal informação descritiva da origem do\nproduto coletado.
     */
    @NotNull
    @ApiModelProperty(
        value = "Descreve de forma simples a origem de uma amostra.\nO local da coleta, linha, tanque...\nA principal informação descritiva da origem do\nproduto coletado.",
        required = true
    )
    private String descricao;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrigemAmostraDTO)) {
            return false;
        }

        OrigemAmostraDTO origemAmostraDTO = (OrigemAmostraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, origemAmostraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrigemAmostraDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
