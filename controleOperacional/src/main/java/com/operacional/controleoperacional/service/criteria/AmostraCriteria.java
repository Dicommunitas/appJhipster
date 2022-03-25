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
 * Criteria class for the {@link com.operacional.controleoperacional.domain.Amostra} entity. This class is used
 * in {@link com.operacional.controleoperacional.web.rest.AmostraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /amostras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class AmostraCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dataHoraColeta;

    private StringFilter observacao;

    private StringFilter identificadorExterno;

    private InstantFilter recebimentoNoLaboratorio;

    private LongFilter finalidadesId;

    private LongFilter operacaoId;

    private LongFilter origemAmostraId;

    private LongFilter produtoId;

    private LongFilter tipoAmostraId;

    private LongFilter amostradaPorId;

    private LongFilter recebidaPorId;

    private Boolean distinct;

    public AmostraCriteria() {}

    public AmostraCriteria(AmostraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dataHoraColeta = other.dataHoraColeta == null ? null : other.dataHoraColeta.copy();
        this.observacao = other.observacao == null ? null : other.observacao.copy();
        this.identificadorExterno = other.identificadorExterno == null ? null : other.identificadorExterno.copy();
        this.recebimentoNoLaboratorio = other.recebimentoNoLaboratorio == null ? null : other.recebimentoNoLaboratorio.copy();
        this.finalidadesId = other.finalidadesId == null ? null : other.finalidadesId.copy();
        this.operacaoId = other.operacaoId == null ? null : other.operacaoId.copy();
        this.origemAmostraId = other.origemAmostraId == null ? null : other.origemAmostraId.copy();
        this.produtoId = other.produtoId == null ? null : other.produtoId.copy();
        this.tipoAmostraId = other.tipoAmostraId == null ? null : other.tipoAmostraId.copy();
        this.amostradaPorId = other.amostradaPorId == null ? null : other.amostradaPorId.copy();
        this.recebidaPorId = other.recebidaPorId == null ? null : other.recebidaPorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AmostraCriteria copy() {
        return new AmostraCriteria(this);
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

    public InstantFilter getDataHoraColeta() {
        return dataHoraColeta;
    }

    public InstantFilter dataHoraColeta() {
        if (dataHoraColeta == null) {
            dataHoraColeta = new InstantFilter();
        }
        return dataHoraColeta;
    }

    public void setDataHoraColeta(InstantFilter dataHoraColeta) {
        this.dataHoraColeta = dataHoraColeta;
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

    public StringFilter getIdentificadorExterno() {
        return identificadorExterno;
    }

    public StringFilter identificadorExterno() {
        if (identificadorExterno == null) {
            identificadorExterno = new StringFilter();
        }
        return identificadorExterno;
    }

    public void setIdentificadorExterno(StringFilter identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public InstantFilter getRecebimentoNoLaboratorio() {
        return recebimentoNoLaboratorio;
    }

    public InstantFilter recebimentoNoLaboratorio() {
        if (recebimentoNoLaboratorio == null) {
            recebimentoNoLaboratorio = new InstantFilter();
        }
        return recebimentoNoLaboratorio;
    }

    public void setRecebimentoNoLaboratorio(InstantFilter recebimentoNoLaboratorio) {
        this.recebimentoNoLaboratorio = recebimentoNoLaboratorio;
    }

    public LongFilter getFinalidadesId() {
        return finalidadesId;
    }

    public LongFilter finalidadesId() {
        if (finalidadesId == null) {
            finalidadesId = new LongFilter();
        }
        return finalidadesId;
    }

    public void setFinalidadesId(LongFilter finalidadesId) {
        this.finalidadesId = finalidadesId;
    }

    public LongFilter getOperacaoId() {
        return operacaoId;
    }

    public LongFilter operacaoId() {
        if (operacaoId == null) {
            operacaoId = new LongFilter();
        }
        return operacaoId;
    }

    public void setOperacaoId(LongFilter operacaoId) {
        this.operacaoId = operacaoId;
    }

    public LongFilter getOrigemAmostraId() {
        return origemAmostraId;
    }

    public LongFilter origemAmostraId() {
        if (origemAmostraId == null) {
            origemAmostraId = new LongFilter();
        }
        return origemAmostraId;
    }

    public void setOrigemAmostraId(LongFilter origemAmostraId) {
        this.origemAmostraId = origemAmostraId;
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

    public LongFilter getTipoAmostraId() {
        return tipoAmostraId;
    }

    public LongFilter tipoAmostraId() {
        if (tipoAmostraId == null) {
            tipoAmostraId = new LongFilter();
        }
        return tipoAmostraId;
    }

    public void setTipoAmostraId(LongFilter tipoAmostraId) {
        this.tipoAmostraId = tipoAmostraId;
    }

    public LongFilter getAmostradaPorId() {
        return amostradaPorId;
    }

    public LongFilter amostradaPorId() {
        if (amostradaPorId == null) {
            amostradaPorId = new LongFilter();
        }
        return amostradaPorId;
    }

    public void setAmostradaPorId(LongFilter amostradaPorId) {
        this.amostradaPorId = amostradaPorId;
    }

    public LongFilter getRecebidaPorId() {
        return recebidaPorId;
    }

    public LongFilter recebidaPorId() {
        if (recebidaPorId == null) {
            recebidaPorId = new LongFilter();
        }
        return recebidaPorId;
    }

    public void setRecebidaPorId(LongFilter recebidaPorId) {
        this.recebidaPorId = recebidaPorId;
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
        final AmostraCriteria that = (AmostraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataHoraColeta, that.dataHoraColeta) &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(identificadorExterno, that.identificadorExterno) &&
            Objects.equals(recebimentoNoLaboratorio, that.recebimentoNoLaboratorio) &&
            Objects.equals(finalidadesId, that.finalidadesId) &&
            Objects.equals(operacaoId, that.operacaoId) &&
            Objects.equals(origemAmostraId, that.origemAmostraId) &&
            Objects.equals(produtoId, that.produtoId) &&
            Objects.equals(tipoAmostraId, that.tipoAmostraId) &&
            Objects.equals(amostradaPorId, that.amostradaPorId) &&
            Objects.equals(recebidaPorId, that.recebidaPorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataHoraColeta,
            observacao,
            identificadorExterno,
            recebimentoNoLaboratorio,
            finalidadesId,
            operacaoId,
            origemAmostraId,
            produtoId,
            tipoAmostraId,
            amostradaPorId,
            recebidaPorId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmostraCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dataHoraColeta != null ? "dataHoraColeta=" + dataHoraColeta + ", " : "") +
            (observacao != null ? "observacao=" + observacao + ", " : "") +
            (identificadorExterno != null ? "identificadorExterno=" + identificadorExterno + ", " : "") +
            (recebimentoNoLaboratorio != null ? "recebimentoNoLaboratorio=" + recebimentoNoLaboratorio + ", " : "") +
            (finalidadesId != null ? "finalidadesId=" + finalidadesId + ", " : "") +
            (operacaoId != null ? "operacaoId=" + operacaoId + ", " : "") +
            (origemAmostraId != null ? "origemAmostraId=" + origemAmostraId + ", " : "") +
            (produtoId != null ? "produtoId=" + produtoId + ", " : "") +
            (tipoAmostraId != null ? "tipoAmostraId=" + tipoAmostraId + ", " : "") +
            (amostradaPorId != null ? "amostradaPorId=" + amostradaPorId + ", " : "") +
            (recebidaPorId != null ? "recebidaPorId=" + recebidaPorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
