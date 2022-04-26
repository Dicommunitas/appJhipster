package com.operacional.controleoperacional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade Status.\n@author Diego.\nDescreve a situação do andamento para solução\nde um problema. Um problema pode ter vários\nstatus até sua finalização.\nUm status só pode ser alterado por quem for\no atual responsável em resolve-lo.\nAo criar um novo Status usar como modelo o último\ncriado pelo usuário.
 */
@Entity
@Table(name = "status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Status implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Descreve o status, a situação que está sendo tratada\nque impede que o problema seja finalizado.
     */
    @Lob
    @Column(name = "descricao", nullable = false)
    private String descricao;

    /**
     * O prazo em que o status deve ser resolvido.
     */
    @NotNull
    @Column(name = "prazo", nullable = false)
    private LocalDate prazo;

    /**
     * Indica em que data o status foi resolvido.
     */
    @Column(name = "data_resolucao")
    private LocalDate dataResolucao;

    @Column(name = "created_date")
    private Instant createdDate;

    @Size(max = 50)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    /**
     * Quem é o relator do status,\no último usuário que atualizou.
     */
    @ManyToOne(optional = false)
    @NotNull
    private User relator;

    /**
     * Atributo responsável.\ndescreve qual será o usuário que deve dar\na tratativa para resolver o status
     */
    @ManyToOne(optional = false)
    @NotNull
    private User responsavel;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "statuses", "relator" }, allowSetters = true)
    private Problema problema;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Status id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Status descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getPrazo() {
        return this.prazo;
    }

    public Status prazo(LocalDate prazo) {
        this.setPrazo(prazo);
        return this;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public LocalDate getDataResolucao() {
        return this.dataResolucao;
    }

    public Status dataResolucao(LocalDate dataResolucao) {
        this.setDataResolucao(dataResolucao);
        return this;
    }

    public void setDataResolucao(LocalDate dataResolucao) {
        this.dataResolucao = dataResolucao;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Status createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Status lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Status lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User getRelator() {
        return this.relator;
    }

    public void setRelator(User user) {
        this.relator = user;
    }

    public Status relator(User user) {
        this.setRelator(user);
        return this;
    }

    public User getResponsavel() {
        return this.responsavel;
    }

    public void setResponsavel(User user) {
        this.responsavel = user;
    }

    public Status responsavel(User user) {
        this.setResponsavel(user);
        return this;
    }

    public Problema getProblema() {
        return this.problema;
    }

    public void setProblema(Problema problema) {
        this.problema = problema;
    }

    public Status problema(Problema problema) {
        this.setProblema(problema);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Status)) {
            return false;
        }
        return id != null && id.equals(((Status) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Status{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", prazo='" + getPrazo() + "'" +
            ", dataResolucao='" + getDataResolucao() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
