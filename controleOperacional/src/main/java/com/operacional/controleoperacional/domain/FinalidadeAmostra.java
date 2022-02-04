package com.operacional.controleoperacional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade FinalidadeAmostra.\n@author Diego.
 */
@Entity
@Table(name = "finalidade_amostra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FinalidadeAmostra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Atributo lacre.\nAmostras que tenham alguma finalidade sem lacre\ncom obrigatoriedade de lacre não poderão ser impressas,\nporém podem ser criadas.
     */
    @Column(name = "lacre", unique = true)
    private String lacre;

    /**
     * Define qual a finalidade da amostra (Arquivo, Análise...)
     */
    @ManyToOne(optional = false)
    @NotNull
    private TipoFinalidadeAmostra tipoFinalidadeAmostra;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "finalidades", "operacao", "origemAmostra", "produto", "tipoAmostra", "amostradaPor", "recebidaPor" },
        allowSetters = true
    )
    private Amostra amostra;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FinalidadeAmostra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLacre() {
        return this.lacre;
    }

    public FinalidadeAmostra lacre(String lacre) {
        this.setLacre(lacre);
        return this;
    }

    public void setLacre(String lacre) {
        this.lacre = lacre;
    }

    public TipoFinalidadeAmostra getTipoFinalidadeAmostra() {
        return this.tipoFinalidadeAmostra;
    }

    public void setTipoFinalidadeAmostra(TipoFinalidadeAmostra tipoFinalidadeAmostra) {
        this.tipoFinalidadeAmostra = tipoFinalidadeAmostra;
    }

    public FinalidadeAmostra tipoFinalidadeAmostra(TipoFinalidadeAmostra tipoFinalidadeAmostra) {
        this.setTipoFinalidadeAmostra(tipoFinalidadeAmostra);
        return this;
    }

    public Amostra getAmostra() {
        return this.amostra;
    }

    public void setAmostra(Amostra amostra) {
        this.amostra = amostra;
    }

    public FinalidadeAmostra amostra(Amostra amostra) {
        this.setAmostra(amostra);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinalidadeAmostra)) {
            return false;
        }
        return id != null && id.equals(((FinalidadeAmostra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinalidadeAmostra{" +
            "id=" + getId() +
            ", lacre='" + getLacre() + "'" +
            "}";
    }
}
