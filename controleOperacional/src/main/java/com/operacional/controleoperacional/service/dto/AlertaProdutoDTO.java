package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.AlertaProduto} entity.
 */
@ApiModel(description = "Entidade AlertasProduto.\n@author Diego.\nIrá descrever alertas para produtos (Benzeno, Inflamável...)")
public class AlertaProdutoDTO implements Serializable {

    private Long id;

    @NotNull
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
        if (!(o instanceof AlertaProdutoDTO)) {
            return false;
        }

        AlertaProdutoDTO alertaProdutoDTO = (AlertaProdutoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alertaProdutoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertaProdutoDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
