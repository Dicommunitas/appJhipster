package com.operacional.controleoperacional.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Status} entity.
 */
@Schema(
    description = "Entidade Status.\n@author Diego.\nDescreve a situação do andamento para solução\nde um problema. Um problema pode ter vários\nstatus até sua finalização.\nUm status só pode ser alterado por quem for\no atual responsável em resolve-lo.\nAo criar um novo Status usar como modelo o último\ncriado pelo usuário."
)
public class StatusDTO implements Serializable {

    private Long id;

    /**
     * Descreve o status, a situação que está sendo tratada\nque impede que o problema seja finalizado.
     */

    @Schema(
        description = "Descreve o status, a situação que está sendo tratada\nque impede que o problema seja finalizado.",
        required = true
    )
    @Lob
    private String descricao;

    /**
     * O prazo em que o status deve ser resolvido.
     */
    @NotNull
    @Schema(description = "O prazo em que o status deve ser resolvido.", required = true)
    private LocalDate prazo;

    /**
     * Indica em que data o status foi resolvido.
     */
    @Schema(description = "Indica em que data o status foi resolvido.")
    private LocalDate dataResolucao;

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", relator=" + getRelator() +
            ", responsavel=" + getResponsavel() +
            ", problema=" + getProblema() +
            "}";
    }
}
