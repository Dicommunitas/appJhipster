package com.operacional.controleoperacional.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade TipoFinalidadeAmostra.\nIrá informar para qual finalidade a amostra\nserá utilizada, análise, arqivo...\n@author Diego.
 */
@Entity
@Table(name = "tipo_finalidade_amostra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TipoFinalidadeAmostra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Atributo descrição.\nIrá informar a descrição da finalidade.\nA principal informação da finalidade.\nAnálise, arqivo...
     */
    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    /**
     * Amostras que tenham alguma finalidade sem lacre\ncom obrigatoriedade de lacre não poderão ser impressas,\nporém podem ser criadas.
     */
    @NotNull
    @Column(name = "obrigatorio_lacre", nullable = false)
    private Boolean obrigatorioLacre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoFinalidadeAmostra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public TipoFinalidadeAmostra descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getObrigatorioLacre() {
        return this.obrigatorioLacre;
    }

    public TipoFinalidadeAmostra obrigatorioLacre(Boolean obrigatorioLacre) {
        this.setObrigatorioLacre(obrigatorioLacre);
        return this;
    }

    public void setObrigatorioLacre(Boolean obrigatorioLacre) {
        this.obrigatorioLacre = obrigatorioLacre;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoFinalidadeAmostra)) {
            return false;
        }
        return id != null && id.equals(((TipoFinalidadeAmostra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoFinalidadeAmostra{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", obrigatorioLacre='" + getObrigatorioLacre() + "'" +
            "}";
    }
}
