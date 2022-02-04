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
     * Atributo descrição, é o nome do local,\nlinha 01, tanque 02...\nA principal informação descritiva da origem.
     */
    @NotNull
    @ApiModelProperty(
        value = "Atributo descrição, é o nome do local,\nlinha 01, tanque 02...\nA principal informação descritiva da origem.",
        required = true
    )
    private String descricao;

    /**
     * Atributo emUso, irá informar se esta\nOrigemAmostra ainda está em uso.\nCaso não esteja mais em uso não deverá\nser permitido sua seleção e nem deve ser\nmostrado no formulario de preenchimento\nda amostra.
     */
    @NotNull
    @ApiModelProperty(
        value = "Atributo emUso, irá informar se esta\nOrigemAmostra ainda está em uso.\nCaso não esteja mais em uso não deverá\nser permitido sua seleção e nem deve ser\nmostrado no formulario de preenchimento\nda amostra.",
        required = true
    )
    private Boolean emUso;

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

    public Boolean getEmUso() {
        return emUso;
    }

    public void setEmUso(Boolean emUso) {
        this.emUso = emUso;
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
            ", emUso='" + getEmUso() + "'" +
            "}";
    }
}
