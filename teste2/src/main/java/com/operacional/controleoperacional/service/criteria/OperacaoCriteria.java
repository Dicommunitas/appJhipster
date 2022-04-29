package com.operacional.controleoperacional.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.operacional.controleoperacional.domain.Operacao} entity. This class is used
 * in {@link com.operacional.controleoperacional.web.rest.OperacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /operacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class OperacaoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter descricao;

    private IntegerFilter volumePeso;

    private InstantFilter inicio;

    private InstantFilter fim;

    private IntegerFilter quantidadeAmostras;

    private StringFilter observacao;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter produtoId;

    private LongFilter tipoOperacaoId;

    private Boolean distinct;

    public OperacaoCriteria() {}

    public OperacaoCriteria(OperacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.volumePeso = other.volumePeso == null ? null : other.volumePeso.copy();
        this.inicio = other.inicio == null ? null : other.inicio.copy();
        this.fim = other.fim == null ? null : other.fim.copy();
        this.quantidadeAmostras = other.quantidadeAmostras == null ? null : other.quantidadeAmostras.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.produtoId = other.produtoId == null ? null : other.produtoId.copy();
        this.tipoOperacaoId = other.tipoOperacaoId == null ? null : other.tipoOperacaoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OperacaoCriteria copy() {
        return new OperacaoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public StringFilter descricao() {
        if (descricao == null) {
            descricao = new StringFilter();
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public IntegerFilter getVolumePeso() {
        return volumePeso;
    }

    public IntegerFilter volumePeso() {
        if (volumePeso == null) {
            volumePeso = new IntegerFilter();
        }
        return volumePeso;
    }

    public void setVolumePeso(IntegerFilter volumePeso) {
        this.volumePeso = volumePeso;
    }

    public InstantFilter getInicio() {
        return inicio;
    }

    public InstantFilter inicio() {
        if (inicio == null) {
            inicio = new InstantFilter();
        }
        return inicio;
    }

    public void setInicio(InstantFilter inicio) {
        this.inicio = inicio;
    }

    public InstantFilter getFim() {
        return fim;
    }

    public InstantFilter fim() {
        if (fim == null) {
            fim = new InstantFilter();
        }
        return fim;
    }

    public void setFim(InstantFilter fim) {
        this.fim = fim;
    }

    public IntegerFilter getQuantidadeAmostras() {
        return quantidadeAmostras;
    }

    public IntegerFilter quantidadeAmostras() {
        if (quantidadeAmostras == null) {
            quantidadeAmostras = new IntegerFilter();
        }
        return quantidadeAmostras;
    }

    public void setQuantidadeAmostras(IntegerFilter quantidadeAmostras) {
        this.quantidadeAmostras = quantidadeAmostras;
    }

    public StringFilter getObservacao() {
        return observacao;
    }

    public StringFilter observacao() {
        if (observacao == null) {
            observacao = new StringFilter();
        }
        return observacao;
    }

    public void setObservacao(StringFilter observacao) {
        this.observacao = observacao;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getProdutoId() {
        return produtoId;
    }

    public LongFilter produtoId() {
        if (produtoId == null) {
            produtoId = new LongFilter();
        }
        return produtoId;
    }

    public void setProdutoId(LongFilter produtoId) {
        this.produtoId = produtoId;
    }

    public LongFilter getTipoOperacaoId() {
        return tipoOperacaoId;
    }

    public LongFilter tipoOperacaoId() {
        if (tipoOperacaoId == null) {
            tipoOperacaoId = new LongFilter();
        }
        return tipoOperacaoId;
    }

    public void setTipoOperacaoId(LongFilter tipoOperacaoId) {
        this.tipoOperacaoId = tipoOperacaoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OperacaoCriteria that = (OperacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(volumePeso, that.volumePeso) &&
            Objects.equals(inicio, that.inicio) &&
            Objects.equals(fim, that.fim) &&
            Objects.equals(quantidadeAmostras, that.quantidadeAmostras) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(produtoId, that.produtoId) &&
            Objects.equals(tipoOperacaoId, that.tipoOperacaoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            descricao,
            volumePeso,
            inicio,
            fim,
            quantidadeAmostras,
            observacao,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            produtoId,
            tipoOperacaoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperacaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (volumePeso != null ? "volumePeso=" + volumePeso + ", " : "") +
            (inicio != null ? "inicio=" + inicio + ", " : "") +
            (fim != null ? "fim=" + fim + ", " : "") +
            (quantidadeAmostras != null ? "quantidadeAmostras=" + quantidadeAmostras + ", " : "") +
            (observacao != null ? "observacao=" + observacao + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (produtoId != null ? "produtoId=" + produtoId + ", " : "") +
            (tipoOperacaoId != null ? "tipoOperacaoId=" + tipoOperacaoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
