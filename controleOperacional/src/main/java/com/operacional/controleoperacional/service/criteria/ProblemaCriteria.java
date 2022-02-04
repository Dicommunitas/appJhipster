package com.operacional.controleoperacional.service.criteria;

import com.operacional.controleoperacional.domain.enumeration.Criticidade;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.DurationFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

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

    private ZonedDateTimeFilter dataZonedDateTime;

    private LocalDateFilter dataLocalDate;

    private InstantFilter dataInstant;

    private DurationFilter dataDuration;

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
        this.dataZonedDateTime = other.dataZonedDateTime == null ? null : other.dataZonedDateTime.copy();
        this.dataLocalDate = other.dataLocalDate == null ? null : other.dataLocalDate.copy();
        this.dataInstant = other.dataInstant == null ? null : other.dataInstant.copy();
        this.dataDuration = other.dataDuration == null ? null : other.dataDuration.copy();
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

    public ZonedDateTimeFilter getDataZonedDateTime() {
        return dataZonedDateTime;
    }

    public ZonedDateTimeFilter dataZonedDateTime() {
        if (dataZonedDateTime == null) {
            dataZonedDateTime = new ZonedDateTimeFilter();
        }
        return dataZonedDateTime;
    }

    public void setDataZonedDateTime(ZonedDateTimeFilter dataZonedDateTime) {
        this.dataZonedDateTime = dataZonedDateTime;
    }

    public LocalDateFilter getDataLocalDate() {
        return dataLocalDate;
    }

    public LocalDateFilter dataLocalDate() {
        if (dataLocalDate == null) {
            dataLocalDate = new LocalDateFilter();
        }
        return dataLocalDate;
    }

    public void setDataLocalDate(LocalDateFilter dataLocalDate) {
        this.dataLocalDate = dataLocalDate;
    }

    public InstantFilter getDataInstant() {
        return dataInstant;
    }

    public InstantFilter dataInstant() {
        if (dataInstant == null) {
            dataInstant = new InstantFilter();
        }
        return dataInstant;
    }

    public void setDataInstant(InstantFilter dataInstant) {
        this.dataInstant = dataInstant;
    }

    public DurationFilter getDataDuration() {
        return dataDuration;
    }

    public DurationFilter dataDuration() {
        if (dataDuration == null) {
            dataDuration = new DurationFilter();
        }
        return dataDuration;
    }

    public void setDataDuration(DurationFilter dataDuration) {
        this.dataDuration = dataDuration;
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
            Objects.equals(dataZonedDateTime, that.dataZonedDateTime) &&
            Objects.equals(dataLocalDate, that.dataLocalDate) &&
            Objects.equals(dataInstant, that.dataInstant) &&
            Objects.equals(dataDuration, that.dataDuration) &&
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
        return Objects.hash(
            id,
            dataZonedDateTime,
            dataLocalDate,
            dataInstant,
            dataDuration,
            descricao,
            criticidade,
            aceitarFinalizacao,
            impacto,
            statusId,
            relatorId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProblemaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dataZonedDateTime != null ? "dataZonedDateTime=" + dataZonedDateTime + ", " : "") +
            (dataLocalDate != null ? "dataLocalDate=" + dataLocalDate + ", " : "") +
            (dataInstant != null ? "dataInstant=" + dataInstant + ", " : "") +
            (dataDuration != null ? "dataDuration=" + dataDuration + ", " : "") +
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
