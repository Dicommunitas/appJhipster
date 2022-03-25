package com.operacional.controleoperacional.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.TipoOperacao} entity.
 */
@Schema(description = "Entidade TipoOperacao.\n@author Diego.")
public class TipoOperacaoDTO implements Serializable {

    private Long id;

    /**
     * Descreve de forma simples um tipo de operação.
     */
    @NotNull
    @Schema(description = "Descreve de forma simples um tipo de operação.", required = true)
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
        if (!(o instanceof TipoOperacaoDTO)) {
            return false;
        }

        TipoOperacaoDTO tipoOperacaoDTO = (TipoOperacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoOperacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoOperacaoDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
