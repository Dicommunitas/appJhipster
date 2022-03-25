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
    description = "Entidade Status.\n@author Diego.\nDescreve a situação do andamento para solução\nde um problema. Um problema pode ter vários\nstatus até sua finalização.\nUm status só pode ser alterado por quem for\no atual responsável em resolve-lo.\nAo criar um novo Status usar como modelo o último\ncriado pelo usuário."
)
public class StatusDTO implements Serializable {

    private Long id;

    /**
     * Descreve o status, a situação que está sendo tratada\nque impede que o problema seja finalizado.
     */

    @ApiModelProperty(
        value = "Descreve o status, a situação que está sendo tratada\nque impede que o problema seja finalizado.",
        required = true
    )
    @Lob
    private String descricao;

    /**
     * O prazo em que o status deve ser resolvido.
     */
    @NotNull
    @ApiModelProperty(value = "O prazo em que o status deve ser resolvido.", required = true)
    private LocalDate prazo;

    /**
     * Indica em que data o status foi resolvido.
     */
    @ApiModelProperty(value = "Indica em que data o status foi resolvido.")
    private LocalDate dataResolucao;

    private UserDTO relator;

    private UserDTO responsavel;

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

    public LocalDate getDataResolucao() {
        return dataResolucao;
    }

    public void setDataResolucao(LocalDate dataResolucao) {
        this.dataResolucao = dataResolucao;
    }

    public UserDTO getRelator() {
        return relator;
    }

    public void setRelator(UserDTO relator) {
        this.relator = relator;
    }

    public UserDTO getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(UserDTO responsavel) {
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
            ", dataResolucao='" + getDataResolucao() + "'" +
            ", relator=" + getRelator() +
            ", responsavel=" + getResponsavel() +
            ", problema=" + getProblema() +
            "}";
    }
}
