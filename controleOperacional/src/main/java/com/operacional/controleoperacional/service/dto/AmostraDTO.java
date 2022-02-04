package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Amostra} entity.
 */
@ApiModel(description = "Entidade Amostra.\n@author Diego.")
public class AmostraDTO implements Serializable {

    private Long id;

    /**
     * Atributo dataHora.\nnão deve ser obrigatório e as amostras\npor padrão devem ser ordenadas pela data\nficando as mais novas por primeiro na\nvizualização e as sem data por útimo
     */
    @ApiModelProperty(
        value = "Atributo dataHora.\nnão deve ser obrigatório e as amostras\npor padrão devem ser ordenadas pela data\nficando as mais novas por primeiro na\nvizualização e as sem data por útimo"
    )
    private Instant dataHora;

    private String observacao;

    private String identificadorExterno;

    private Boolean amostraNoLaboratorio;

    private OperacaoDTO operacao;

    private OrigemAmostraDTO origemAmostra;

    private ProdutoDTO produto;

    private TipoAmostraDTO tipoAmostra;

    private UsuarioDTO amostradaPor;

    private UsuarioDTO recebidaPor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataHora() {
        return dataHora;
    }

    public void setDataHora(Instant dataHora) {
        this.dataHora = dataHora;
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

    public Boolean getAmostraNoLaboratorio() {
        return amostraNoLaboratorio;
    }

    public void setAmostraNoLaboratorio(Boolean amostraNoLaboratorio) {
        this.amostraNoLaboratorio = amostraNoLaboratorio;
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

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public TipoAmostraDTO getTipoAmostra() {
        return tipoAmostra;
    }

    public void setTipoAmostra(TipoAmostraDTO tipoAmostra) {
        this.tipoAmostra = tipoAmostra;
    }

    public UsuarioDTO getAmostradaPor() {
        return amostradaPor;
    }

    public void setAmostradaPor(UsuarioDTO amostradaPor) {
        this.amostradaPor = amostradaPor;
    }

    public UsuarioDTO getRecebidaPor() {
        return recebidaPor;
    }

    public void setRecebidaPor(UsuarioDTO recebidaPor) {
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
            ", dataHora='" + getDataHora() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", amostraNoLaboratorio='" + getAmostraNoLaboratorio() + "'" +
            ", operacao=" + getOperacao() +
            ", origemAmostra=" + getOrigemAmostra() +
            ", produto=" + getProduto() +
            ", tipoAmostra=" + getTipoAmostra() +
            ", amostradaPor=" + getAmostradaPor() +
            ", recebidaPor=" + getRecebidaPor() +
            "}";
    }
}
