package com.operacional.controleoperacional.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.operacional.controleoperacional.domain.Relatorio} entity. This class is used
 * in {@link com.operacional.controleoperacional.web.rest.RelatorioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /relatorios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RelatorioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter linksExternos;

    private LongFilter tipoId;

    private LongFilter responsavelId;

    private Boolean distinct;

    public RelatorioCriteria() {}

    public RelatorioCriteria(RelatorioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.linksExternos = other.linksExternos == null ? null : other.linksExternos.copy();
        this.tipoId = other.tipoId == null ? null : other.tipoId.copy();
        this.responsavelId = other.responsavelId == null ? null : other.responsavelId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RelatorioCriteria copy() {
        return new RelatorioCriteria(this);
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

    public StringFilter getLinksExternos() {
        return linksExternos;
    }

    public StringFilter linksExternos() {
        if (linksExternos == null) {
            linksExternos = new StringFilter();
        }
        return linksExternos;
    }

    public void setLinksExternos(StringFilter linksExternos) {
        this.linksExternos = linksExternos;
    }

    public LongFilter getTipoId() {
        return tipoId;
    }

    public LongFilter tipoId() {
        if (tipoId == null) {
            tipoId = new LongFilter();
        }
        return tipoId;
    }

    public void setTipoId(LongFilter tipoId) {
        this.tipoId = tipoId;
    }

    public LongFilter getResponsavelId() {
        return responsavelId;
    }

    public LongFilter responsavelId() {
        if (responsavelId == null) {
            responsavelId = new LongFilter();
        }
        return responsavelId;
    }

    public void setResponsavelId(LongFilter responsavelId) {
        this.responsavelId = responsavelId;
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
        final RelatorioCriteria that = (RelatorioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(linksExternos, that.linksExternos) &&
            Objects.equals(tipoId, that.tipoId) &&
            Objects.equals(responsavelId, that.responsavelId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, linksExternos, tipoId, responsavelId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RelatorioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (linksExternos != null ? "linksExternos=" + linksExternos + ", " : "") +
            (tipoId != null ? "tipoId=" + tipoId + ", " : "") +
            (responsavelId != null ? "responsavelId=" + responsavelId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
