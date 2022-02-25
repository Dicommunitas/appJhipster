package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Status} entity.
 */
@ApiModel(
    description = "Entidade Status.\n@author Diego.\nDescreve a situação do andamento para solução\nde um problema. Um problema pode ter vários\nstatus até sua finalização.\nUm status só pode ser alterado por quem for\no atual responsável em resolve-lo."
)
public class StatusDTO implements Serializable {

    private Long id;

    /**
     * Descreve o status, a situação atual que está sendo tratada.\nUm status só pode ser alterado por quem for\no atual responsável em resolve-lo.
     */

    @ApiModelProperty(
        value = "Descreve o status, a situação atual que está sendo tratada.\nUm status só pode ser alterado por quem for\no atual responsável em resolve-lo.",
        required = true
    )
    @Lob
    private String descricao;

    /**
     * O prazo em que o status deve ser resolvido
     */
    @NotNull
    @ApiModelProperty(value = "O prazo em que o status deve ser resolvido", required = true)
    private LocalDate prazo;

    /**
     * Indica se o status foi ou não resolvido.
     */
    @ApiModelProperty(value = "Indica se o status foi ou não resolvido.")
    private Boolean resolvido;

    private UsuarioDTO relator;

    private UsuarioDTO responsavel;

    private ProblemaDTO problema;

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

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public Boolean getResolvido() {
        return resolvido;
    }

    public void setResolvido(Boolean resolvido) {
        this.resolvido = resolvido;
    }

    public UsuarioDTO getRelator() {
        return relator;
    }

    public void setRelator(UsuarioDTO relator) {
        this.relator = relator;
    }

    public UsuarioDTO getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(UsuarioDTO responsavel) {
        this.responsavel = responsavel;
    }

    public ProblemaDTO getProblema() {
        return problema;
    }

    public void setProblema(ProblemaDTO problema) {
        this.problema = problema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusDTO)) {
            return false;
        }

        StatusDTO statusDTO = (StatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, statusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", prazo='" + getPrazo() + "'" +
            ", resolvido='" + getResolvido() + "'" +
            ", relator=" + getRelator() +
            ", responsavel=" + getResponsavel() +
            ", problema=" + getProblema() +
            "}";
    }
}
