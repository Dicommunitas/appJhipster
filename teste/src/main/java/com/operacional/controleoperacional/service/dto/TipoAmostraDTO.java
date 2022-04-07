package com.operacional.controleoperacional.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.TipoAmostra} entity.
 */
@Schema(description = "Entidade TipoAmostra.\n@author Diego.")
public class TipoAmostraDTO implements Serializable {

    private Long id;

    /**
     * Deve descrever de forma simples de qual tipo é a amostra.
     */
    @NotNull
    @Schema(description = "Deve descrever de forma simples de qual tipo é a amostra.", required = true)
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
        if (!(o instanceof TipoAmostraDTO)) {
            return false;
        }

        TipoAmostraDTO tipoAmostraDTO = (TipoAmostraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoAmostraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoAmostraDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
