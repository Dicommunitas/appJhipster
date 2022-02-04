package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Produto} entity.
 */
@ApiModel(
    description = "Entidade OrigemAmostra, irá descrever de onde\na amostra foi retirada, linha 01, tanque 02...\nO local onde a amostra foi coletada.\n@author Diego."
)
public class ProdutoDTO implements Serializable {

    private Long id;

    /**
     * Atributo codigo deve ter no\nmáximo 3 aracteres
     */
    @NotNull
    @Size(max = 3)
    @ApiModelProperty(value = "Atributo codigo deve ter no\nmáximo 3 aracteres", required = true)
    private String codigo;

    /**
     * Atributo nomeCurto
     */
    @NotNull
    @ApiModelProperty(value = "Atributo nomeCurto", required = true)
    private String nomeCurto;

    /**
     * Atributo nomeCompleto
     */
    @NotNull
    @ApiModelProperty(value = "Atributo nomeCompleto", required = true)
    private String nomeCompleto;

    private Set<AlertaProdutoDTO> alertas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNomeCurto() {
        return nomeCurto;
    }

    public void setNomeCurto(String nomeCurto) {
        this.nomeCurto = nomeCurto;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public Set<AlertaProdutoDTO> getAlertas() {
        return alertas;
    }

    public void setAlertas(Set<AlertaProdutoDTO> alertas) {
        this.alertas = alertas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProdutoDTO)) {
            return false;
        }

        ProdutoDTO produtoDTO = (ProdutoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, produtoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProdutoDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nomeCurto='" + getNomeCurto() + "'" +
            ", nomeCompleto='" + getNomeCompleto() + "'" +
            ", alertas=" + getAlertas() +
            "}";
    }
}
