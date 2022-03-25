package com.operacional.controleoperacional.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.operacional.controleoperacional.domain.Produto} entity. This class is used
 * in {@link com.operacional.controleoperacional.web.rest.ProdutoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /produtos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ProdutoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codigoBDEMQ;

    private StringFilter nomeCurto;

    private StringFilter nomeCompleto;

    private LongFilter alertasId;

    private Boolean distinct;

    public ProdutoCriteria() {}

    public ProdutoCriteria(ProdutoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codigoBDEMQ = other.codigoBDEMQ == null ? null : other.codigoBDEMQ.copy();
        this.nomeCurto = other.nomeCurto == null ? null : other.nomeCurto.copy();
        this.nomeCompleto = other.nomeCompleto == null ? null : other.nomeCompleto.copy();
        this.alertasId = other.alertasId == null ? null : other.alertasId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProdutoCriteria copy() {
        return new ProdutoCriteria(this);
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

    public StringFilter getCodigoBDEMQ() {
        return codigoBDEMQ;
    }

    public StringFilter codigoBDEMQ() {
        if (codigoBDEMQ == null) {
            codigoBDEMQ = new StringFilter();
        }
        return codigoBDEMQ;
    }

    public void setCodigoBDEMQ(StringFilter codigoBDEMQ) {
        this.codigoBDEMQ = codigoBDEMQ;
    }

    public StringFilter getNomeCurto() {
        return nomeCurto;
    }

    public StringFilter nomeCurto() {
        if (nomeCurto == null) {
            nomeCurto = new StringFilter();
        }
        return nomeCurto;
    }

    public void setNomeCurto(StringFilter nomeCurto) {
        this.nomeCurto = nomeCurto;
    }

    public StringFilter getNomeCompleto() {
        return nomeCompleto;
    }

    public StringFilter nomeCompleto() {
        if (nomeCompleto == null) {
            nomeCompleto = new StringFilter();
        }
        return nomeCompleto;
    }

    public void setNomeCompleto(StringFilter nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public LongFilter getAlertasId() {
        return alertasId;
    }

    public LongFilter alertasId() {
        if (alertasId == null) {
            alertasId = new LongFilter();
        }
        return alertasId;
    }

    public void setAlertasId(LongFilter alertasId) {
        this.alertasId = alertasId;
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
        final ProdutoCriteria that = (ProdutoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codigoBDEMQ, that.codigoBDEMQ) &&
            Objects.equals(nomeCurto, that.nomeCurto) &&
            Objects.equals(nomeCompleto, that.nomeCompleto) &&
            Objects.equals(alertasId, that.alertasId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigoBDEMQ, nomeCurto, nomeCompleto, alertasId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProdutoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (codigoBDEMQ != null ? "codigoBDEMQ=" + codigoBDEMQ + ", " : "") +
            (nomeCurto != null ? "nomeCurto=" + nomeCurto + ", " : "") +
            (nomeCompleto != null ? "nomeCompleto=" + nomeCompleto + ", " : "") +
            (alertasId != null ? "alertasId=" + alertasId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
