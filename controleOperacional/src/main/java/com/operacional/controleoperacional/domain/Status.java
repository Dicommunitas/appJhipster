package com.operacional.controleoperacional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade Status.\n@author Diego.\nDescreve a situação do andamento para solução\nde um problema. Um problema pode ter vários\nstatus até sua finalização.\nUm status só pode ser alterado por quem for\no atual responsável em resolve-lo.
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
     * Atributo descrição.\nDescreve o status, a situação atual que está sendo tratada.
     */
    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    /**
     * Atributo prazo.\nO prazo em que o status deve ser resolvido
     */
    @NotNull
    @Column(name = "prazo", nullable = false)
    private ZonedDateTime prazo;

    /**
     * Atributo resolvido.\nIndica se o status foi ou não resolvido.
     */
    @Column(name = "resolvido")
    private Boolean resolvido;

    /**
     * Quem foi o relator do status,\no último usuário que atualizou
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "relAutorizados" }, allowSetters = true)
    private Usuario relator;

    /**
     * Atributo responsável.\ndescreve qual será o usuário que deve dar\na tratativa para resolver o status
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "relAutorizados" }, allowSetters = true)
    private Usuario responsavel;

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

    public ZonedDateTime getPrazo() {
        return this.prazo;
    }

    public Status prazo(ZonedDateTime prazo) {
        this.setPrazo(prazo);
        return this;
    }

    public void setPrazo(ZonedDateTime prazo) {
        this.prazo = prazo;
    }

    public Boolean getResolvido() {
        return this.resolvido;
    }

    public Status resolvido(Boolean resolvido) {
        this.setResolvido(resolvido);
        return this;
    }

    public void setResolvido(Boolean resolvido) {
        this.resolvido = resolvido;
    }

    public Usuario getRelator() {
        return this.relator;
    }

    public void setRelator(Usuario usuario) {
        this.relator = usuario;
    }

    public Status relator(Usuario usuario) {
        this.setRelator(usuario);
        return this;
    }

    public Usuario getResponsavel() {
        return this.responsavel;
    }

    public void setResponsavel(Usuario usuario) {
        this.responsavel = usuario;
    }

    public Status responsavel(Usuario usuario) {
        this.setResponsavel(usuario);
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
            ", resolvido='" + getResolvido() + "'" +
            "}";
    }
}
