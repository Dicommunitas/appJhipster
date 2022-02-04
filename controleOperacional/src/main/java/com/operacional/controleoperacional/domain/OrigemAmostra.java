package com.operacional.controleoperacional.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade OrigemAmostra, irá descrever de onde\na amostra foi retirada, linha 01, tanque 02...\nO local onde a amostra foi coletada.\n@author Diego.
 */
@Entity
@Table(name = "origem_amostra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrigemAmostra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Atributo descrição, é o nome do local,\nlinha 01, tanque 02...\nA principal informação descritiva da origem.
     */
    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    /**
     * Atributo emUso, irá informar se esta\nOrigemAmostra ainda está em uso.\nCaso não esteja mais em uso não deverá\nser permitido sua seleção e nem deve ser\nmostrado no formulario de preenchimento\nda amostra.
     */
    @NotNull
    @Column(name = "em_uso", nullable = false)
    private Boolean emUso;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrigemAmostra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public OrigemAmostra descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getEmUso() {
        return this.emUso;
    }

    public OrigemAmostra emUso(Boolean emUso) {
        this.setEmUso(emUso);
        return this;
    }

    public void setEmUso(Boolean emUso) {
        this.emUso = emUso;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrigemAmostra)) {
            return false;
        }
        return id != null && id.equals(((OrigemAmostra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrigemAmostra{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", emUso='" + getEmUso() + "'" +
            "}";
    }
}
