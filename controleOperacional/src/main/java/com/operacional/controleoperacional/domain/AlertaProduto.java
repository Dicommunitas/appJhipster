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
 * Entidade AlertasProduto.\n@author Diego.\nIrá descrever alertas para produtos (Benzeno, Inflamável...)
 */
@Entity
@Table(name = "alerta_produto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AlertaProduto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ManyToMany(mappedBy = "alertas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alertas" }, allowSetters = true)
    private Set<Produto> produtos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlertaProduto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public AlertaProduto descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Produto> getProdutos() {
        return this.produtos;
    }

    public void setProdutos(Set<Produto> produtos) {
        if (this.produtos != null) {
            this.produtos.forEach(i -> i.removeAlertas(this));
        }
        if (produtos != null) {
            produtos.forEach(i -> i.addAlertas(this));
        }
        this.produtos = produtos;
    }

    public AlertaProduto produtos(Set<Produto> produtos) {
        this.setProdutos(produtos);
        return this;
    }

    public AlertaProduto addProdutos(Produto produto) {
        this.produtos.add(produto);
        produto.getAlertas().add(this);
        return this;
    }

    public AlertaProduto removeProdutos(Produto produto) {
        this.produtos.remove(produto);
        produto.getAlertas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertaProduto)) {
            return false;
        }
        return id != null && id.equals(((AlertaProduto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertaProduto{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
