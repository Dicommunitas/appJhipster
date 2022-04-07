package com.operacional.controleoperacional.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Produto} entity.
 */
@Schema(description = "Entidade Produto.\n@author Diego.")
public class ProdutoDTO implements Serializable {

    private Long id;

    /**
     * O código deve ter máximo 3 aracteres.\nDeve ser igual ao do BDEMQ
     */
    @NotNull
    @Size(max = 3)
    @Schema(description = "O código deve ter máximo 3 aracteres.\nDeve ser igual ao do BDEMQ", required = true)
    private String codigoBDEMQ;

    /**
     * Informa o nome curto do produto.\nDeve ser igual ao do BDEMQ
     */
    @NotNull
    @Schema(description = "Informa o nome curto do produto.\nDeve ser igual ao do BDEMQ", required = true)
    private String nomeCurto;

    /**
     * Informa o nome completo do produto.\nDeve ser igual ao do BDEMQ
     */
    @NotNull
    @Schema(description = "Informa o nome completo do produto.\nDeve ser igual ao do BDEMQ", required = true)
    private String nomeCompleto;

    private Set<AlertaProdutoDTO> alertas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoBDEMQ() {
        return codigoBDEMQ;
    }

    public void setCodigoBDEMQ(String codigoBDEMQ) {
        this.codigoBDEMQ = codigoBDEMQ;
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
            ", codigoBDEMQ='" + getCodigoBDEMQ() + "'" +
            ", nomeCurto='" + getNomeCurto() + "'" +
            ", nomeCompleto='" + getNomeCompleto() + "'" +
            ", alertas=" + getAlertas() +
            "}";
    }
}
