package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Operacao} entity.
 */
@ApiModel(
    description = "Entidade Operação.\n@author Diego.\nAo criar uma Operação deve existir\numa opção de usar operações passadas\ncomo modelo para sua criação, inclusive\ncopiando o plano de amostragem"
)
public class OperacaoDTO implements Serializable {

    private Long id;

    /**
     * Descreve de forma simples a operação.
     */
    @NotNull
    @ApiModelProperty(value = "Descreve de forma simples a operação.", required = true)
    private String descricao;

    /**
     * O volume ou peso total da operação. Em metros cúbicos ou toneladas
     */
    @NotNull
    @ApiModelProperty(value = "O volume ou peso total da operação. Em metros cúbicos ou toneladas", required = true)
    private Integer volumePeso;

    /**
     * O horário de início da operação.
     */
    @ApiModelProperty(value = "O horário de início da operação.")
    private Instant inicio;

    /**
     * O horário de término da operação.
     */
    @ApiModelProperty(value = "O horário de término da operação.")
    private Instant fim;

    /**
     * Quantas amostras devem ter nessa operação.
     */
    @NotNull
    @ApiModelProperty(value = "Quantas amostras devem ter nessa operação.", required = true)
    private Integer quantidadeAmostras;

    /**
     * Observações que forem necessárias para melhorar\na descrição dos acontecimentos relativos da operação.
     */
    @ApiModelProperty(value = "Observações que forem necessárias para melhorar\na descrição dos acontecimentos relativos da operação.")
    private String observacao;

    private TipoOperacaoDTO tipoOperacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getVolumePeso() {
        return volumePeso;
    }

    public void setVolumePeso(Integer volumePeso) {
        this.volumePeso = volumePeso;
    }

    public Instant getInicio() {
        return inicio;
    }

    public void setInicio(Instant inicio) {
        this.inicio = inicio;
    }

    public Instant getFim() {
        return fim;
    }

    public void setFim(Instant fim) {
        this.fim = fim;
    }

    public Integer getQuantidadeAmostras() {
        return quantidadeAmostras;
    }

    public void setQuantidadeAmostras(Integer quantidadeAmostras) {
        this.quantidadeAmostras = quantidadeAmostras;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public TipoOperacaoDTO getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacaoDTO tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperacaoDTO)) {
            return false;
        }

        OperacaoDTO operacaoDTO = (OperacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, operacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperacaoDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", volumePeso=" + getVolumePeso() +
            ", inicio='" + getInicio() + "'" +
            ", fim='" + getFim() + "'" +
            ", quantidadeAmostras=" + getQuantidadeAmostras() +
            ", observacao='" + getObservacao() + "'" +
            ", tipoOperacao=" + getTipoOperacao() +
            "}";
    }
}
