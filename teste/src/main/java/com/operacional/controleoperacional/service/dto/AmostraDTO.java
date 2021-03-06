package com.operacional.controleoperacional.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Amostra} entity.
 */
@Schema(description = "Entidade Amostra.\n@author Diego.")
public class AmostraDTO implements Serializable {

    private Long id;

    /**
     * Data e hora que a amostra foi coletada.
     */
    @Schema(description = "Data e hora que a amostra foi coletada.")
    private Instant dataHoraColeta;

    /**
     * Observações que forem necessárias para melhorar\na identificação da origem amostra.
     */
    @Schema(description = "Observações que forem necessárias para melhorar\na identificação da origem amostra.")
    private String descricaoDeOrigen;

    /**
     * Observações que forem necessárias para melhorar\na identificação da amostra.
     */
    @Schema(description = "Observações que forem necessárias para melhorar\na identificação da amostra.")
    private String observacao;

    /**
     * Identificador que \"ligue/identifique\" essa\namostra em outro sistema.
     */
    @Schema(description = "Identificador que \"ligue/identifique\" essa\namostra em outro sistema.")
    private String identificadorExterno;

    /**
     * Identifica se a amostra está ou não no laboratório.
     */
    @Schema(description = "Identifica se a amostra está ou não no laboratório.")
    private Instant recebimentoNoLaboratorio;

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private OperacaoDTO operacao;

    private OrigemAmostraDTO origemAmostra;

    private TipoAmostraDTO tipoAmostra;

    private UserDTO amostradaPor;

    private UserDTO recebidaPor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataHoraColeta() {
        return dataHoraColeta;
    }

    public void setDataHoraColeta(Instant dataHoraColeta) {
        this.dataHoraColeta = dataHoraColeta;
    }

    public String getDescricaoDeOrigen() {
        return descricaoDeOrigen;
    }

    public void setDescricaoDeOrigen(String descricaoDeOrigen) {
        this.descricaoDeOrigen = descricaoDeOrigen;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getIdentificadorExterno() {
        return identificadorExterno;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public Instant getRecebimentoNoLaboratorio() {
        return recebimentoNoLaboratorio;
    }

    public void setRecebimentoNoLaboratorio(Instant recebimentoNoLaboratorio) {
        this.recebimentoNoLaboratorio = recebimentoNoLaboratorio;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public OperacaoDTO getOperacao() {
        return operacao;
    }

    public void setOperacao(OperacaoDTO operacao) {
        this.operacao = operacao;
    }

    public OrigemAmostraDTO getOrigemAmostra() {
        return origemAmostra;
    }

    public void setOrigemAmostra(OrigemAmostraDTO origemAmostra) {
        this.origemAmostra = origemAmostra;
    }

    public TipoAmostraDTO getTipoAmostra() {
        return tipoAmostra;
    }

    public void setTipoAmostra(TipoAmostraDTO tipoAmostra) {
        this.tipoAmostra = tipoAmostra;
    }

    public UserDTO getAmostradaPor() {
        return amostradaPor;
    }

    public void setAmostradaPor(UserDTO amostradaPor) {
        this.amostradaPor = amostradaPor;
    }

    public UserDTO getRecebidaPor() {
        return recebidaPor;
    }

    public void setRecebidaPor(UserDTO recebidaPor) {
        this.recebidaPor = recebidaPor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AmostraDTO)) {
            return false;
        }

        AmostraDTO amostraDTO = (AmostraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, amostraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmostraDTO{" +
            "id=" + getId() +
            ", dataHoraColeta='" + getDataHoraColeta() + "'" +
            ", descricaoDeOrigen='" + getDescricaoDeOrigen() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", recebimentoNoLaboratorio='" + getRecebimentoNoLaboratorio() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", operacao=" + getOperacao() +
            ", origemAmostra=" + getOrigemAmostra() +
            ", tipoAmostra=" + getTipoAmostra() +
            ", amostradaPor=" + getAmostradaPor() +
            ", recebidaPor=" + getRecebidaPor() +
            "}";
    }
}
