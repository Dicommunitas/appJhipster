package com.operacional.controleoperacional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade Produto.\n@author Diego.
 */
@Entity
@Table(name = "produto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * O código deve ter máximo 3 aracteres.\nDeve ser igual ao do BDEMQ
     */
    @NotNull
    @Size(max = 3)
    @Column(name = "codigo_bdemq", length = 3, nullable = false, unique = true)
    private String codigoBDEMQ;

    /**
     * Informa o nome curto do produto.\nDeve ser igual ao do BDEMQ
     */
    @NotNull
    @Column(name = "nome_curto", nullable = false)
    private String nomeCurto;

    /**
     * Informa o nome completo do produto.\nDeve ser igual ao do BDEMQ
     */
    @NotNull
    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    /**
     * Irá informar uma\nlista de alertas pré definidos.
     */
    @ManyToMany
    @JoinTable(
        name = "rel_produto__alertas",
        joinColumns = @JoinColumn(name = "produto_id"),
        inverseJoinColumns = @JoinColumn(name = "alertas_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "produtos" }, allowSetters = true)
    private Set<AlertaProduto> alertas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Produto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoBDEMQ() {
        return this.codigoBDEMQ;
    }

    public Produto codigoBDEMQ(String codigoBDEMQ) {
        this.setCodigoBDEMQ(codigoBDEMQ);
        return this;
    }

    public void setCodigoBDEMQ(String codigoBDEMQ) {
        this.codigoBDEMQ = codigoBDEMQ;
    }

    public String getNomeCurto() {
        return this.nomeCurto;
    }

    public Produto nomeCurto(String nomeCurto) {
        this.setNomeCurto(nomeCurto);
        return this;
    }

    public void setNomeCurto(String nomeCurto) {
        this.nomeCurto = nomeCurto;
    }

    public String getNomeCompleto() {
        return this.nomeCompleto;
    }

    public Produto nomeCompleto(String nomeCompleto) {
        this.setNomeCompleto(nomeCompleto);
        return this;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public Set<AlertaProduto> getAlertas() {
        return this.alertas;
    }

    public void setAlertas(Set<AlertaProduto> alertaProdutos) {
        this.alertas = alertaProdutos;
    }

    public Produto alertas(Set<AlertaProduto> alertaProdutos) {
        this.setAlertas(alertaProdutos);
        return this;
    }

    public Produto addAlertas(AlertaProduto alertaProduto) {
        this.alertas.add(alertaProduto);
        alertaProduto.getProdutos().add(this);
        return this;
    }

    public Produto removeAlertas(AlertaProduto alertaProduto) {
        this.alertas.remove(alertaProduto);
        alertaProduto.getProdutos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Produto)) {
            return false;
        }
        return id != null && id.equals(((Produto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Produto{" +
            "id=" + getId() +
            ", codigoBDEMQ='" + getCodigoBDEMQ() + "'" +
            ", nomeCurto='" + getNomeCurto() + "'" +
            ", nomeCompleto='" + getNomeCompleto() + "'" +
            "}";
    }
}
