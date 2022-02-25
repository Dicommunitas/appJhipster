package com.operacional.controleoperacional.service.criteria;

import com.operacional.controleoperacional.domain.enumeration.Criticidade;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.operacional.controleoperacional.domain.Problema} entity. This class is used
 * in {@link com.operacional.controleoperacional.web.rest.ProblemaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /problemas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProblemaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Criticidade
     */
    public static class CriticidadeFilter extends Filter<Criticidade> {

        public CriticidadeFilter() {}

        public CriticidadeFilter(CriticidadeFilter filter) {
            super(filter);
        }

        @Override
        public CriticidadeFilter copy() {
            return new CriticidadeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter data;

    private StringFilter descricao;

    private CriticidadeFilter criticidade;

    private BooleanFilter aceitarFinalizacao;

    private StringFilter impacto;

    private LongFilter statusId;

    private LongFilter relatorId;

    private Boolean distinct;

    public ProblemaCriteria() {}

    public ProblemaCriteria(ProblemaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.data = other.data == null ? null : other.data.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.criticidade = other.criticidade == null ? null : other.criticidade.copy();
        this.aceitarFinalizacao = other.aceitarFinalizacao == null ? null : other.aceitarFinalizacao.copy();
        this.impacto = other.impacto == null ? null : other.impacto.copy();
        this.statusId = other.statusId == null ? null : other.statusId.copy();
        this.relatorId = other.relatorId == null ? null : other.relatorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProblemaCriteria copy() {
        return new ProblemaCriteria(this);
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

    public LocalDateFilter getData() {
        return data;
    }

    public LocalDateFilter data() {
        if (data == null) {
            data = new LocalDateFilter();
        }
        return data;
    }

    public void setData(LocalDateFilter data) {
        this.data = data;
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

    public CriticidadeFilter getCriticidade() {
        return criticidade;
    }

    public CriticidadeFilter criticidade() {
        if (criticidade == null) {
            criticidade = new CriticidadeFilter();
        }
        return criticidade;
    }

    public void setCriticidade(CriticidadeFilter criticidade) {
        this.criticidade = criticidade;
    }

    public BooleanFilter getAceitarFinalizacao() {
        return aceitarFinalizacao;
    }

    public BooleanFilter aceitarFinalizacao() {
        if (aceitarFinalizacao == null) {
            aceitarFinalizacao = new BooleanFilter();
        }
        return aceitarFinalizacao;
    }

    public void setAceitarFinalizacao(BooleanFilter aceitarFinalizacao) {
        this.aceitarFinalizacao = aceitarFinalizacao;
    }

    public StringFilter getImpacto() {
        return impacto;
    }

    public StringFilter impacto() {
        if (impacto == null) {
            impacto = new StringFilter();
        }
        return impacto;
    }

    public void setImpacto(StringFilter impacto) {
        this.impacto = impacto;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public LongFilter statusId() {
        if (statusId == null) {
            statusId = new LongFilter();
        }
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
    }

    public LongFilter getRelatorId() {
        return relatorId;
    }

    public LongFilter relatorId() {
        if (relatorId == null) {
            relatorId = new LongFilter();
        }
        return relatorId;
    }

    public void setRelatorId(LongFilter relatorId) {
        this.relatorId = relatorId;
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
        final ProblemaCriteria that = (ProblemaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(data, that.data) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(criticidade, that.criticidade) &&
            Objects.equals(aceitarFinalizacao, that.aceitarFinalizacao) &&
            Objects.equals(impacto, that.impacto) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(relatorId, that.relatorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data, descricao, criticidade, aceitarFinalizacao, impacto, statusId, relatorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProblemaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (data != null ? "data=" + data + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (criticidade != null ? "criticidade=" + criticidade + ", " : "") +
            (aceitarFinalizacao != null ? "aceitarFinalizacao=" + aceitarFinalizacao + ", " : "") +
            (impacto != null ? "impacto=" + impacto + ", " : "") +
            (statusId != null ? "statusId=" + statusId + ", " : "") +
            (relatorId != null ? "relatorId=" + relatorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
