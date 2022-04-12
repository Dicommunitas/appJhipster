package com.operacional.controleoperacional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade Operação.\n@author Diego.\nAo criar uma Operação deve existir\numa opção de usar operações passadas\ncomo modelo para sua criação, inclusive\ncopiando o plano de amostragem
 */
@Entity
@Table(name = "operacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Operacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Descreve de forma simples a operação.
     */
    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    /**
     * O volume ou peso total da operação. Em metros cúbicos ou toneladas
     */
    @NotNull
    @Column(name = "volume_peso", nullable = false)
    private Integer volumePeso;

    /**
     * O horário de início da operação.
     */
    @Column(name = "inicio")
    private Instant inicio;

    /**
     * O horário de término da operação.
     */
    @Column(name = "fim")
    private Instant fim;

    /**
     * Quantas amostras devem ter nessa operação.
     */
    @Column(name = "quantidade_amostras")
    private Integer quantidadeAmostras;

    /**
     * Observações que forem necessárias para melhorar\na descrição dos acontecimentos relativos da operação.
     */
    @Column(name = "observacao")
    private String observacao;

    @Size(max = 50)
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Size(max = 50)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    /**
     * Define qual o produto da amostra
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "alertas" }, allowSetters = true)
    private Produto produto;

    /**
     * Define de que tipo é a operação\n(Barcaça - Carga, Caminhão - Descarga...)
     */
    @ManyToOne(optional = false)
    @NotNull
    private TipoOperacao tipoOperacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Operacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Operacao descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getVolumePeso() {
        return this.volumePeso;
    }

    public Operacao volumePeso(Integer volumePeso) {
        this.setVolumePeso(volumePeso);
        return this;
    }

    public void setVolumePeso(Integer volumePeso) {
        this.volumePeso = volumePeso;
    }

    public Instant getInicio() {
        return this.inicio;
    }

    public Operacao inicio(Instant inicio) {
        this.setInicio(inicio);
        return this;
    }

    public void setInicio(Instant inicio) {
        this.inicio = inicio;
    }

    public Instant getFim() {
        return this.fim;
    }

    public Operacao fim(Instant fim) {
        this.setFim(fim);
        return this;
    }

    public void setFim(Instant fim) {
        this.fim = fim;
    }

    public Integer getQuantidadeAmostras() {
        return this.quantidadeAmostras;
    }

    public Operacao quantidadeAmostras(Integer quantidadeAmostras) {
        this.setQuantidadeAmostras(quantidadeAmostras);
        return this;
    }

    public void setQuantidadeAmostras(Integer quantidadeAmostras) {
        this.quantidadeAmostras = quantidadeAmostras;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public Operacao observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Operacao createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Operacao createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Operacao lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Operacao lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Operacao produto(Produto produto) {
        this.setProduto(produto);
        return this;
    }

    public TipoOperacao getTipoOperacao() {
        return this.tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public Operacao tipoOperacao(TipoOperacao tipoOperacao) {
        this.setTipoOperacao(tipoOperacao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operacao)) {
            return false;
        }
        return id != null && id.equals(((Operacao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operacao{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", volumePeso=" + getVolumePeso() +
            ", inicio='" + getInicio() + "'" +
            ", fim='" + getFim() + "'" +
            ", quantidadeAmostras=" + getQuantidadeAmostras() +
            ", observacao='" + getObservacao() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
