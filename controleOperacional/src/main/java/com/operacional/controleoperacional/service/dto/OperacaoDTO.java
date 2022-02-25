package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
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
     * Atributo descrição.\nDeve existir algum tipo de indicação\nque mostre a quantidade de amostras\nsuficiente ou insuficiente para a operação.\nDeve existir uma visualização em lista\nde todas as amostras pertencentes a operação\nna sua tela de visualização\nDeve existir um recurso para facilitar\na conferência do plano de amostragem\ndurante a criação da operação/plano de\namostragem.\n\nDescreve de forma simples a operação.
     */
    @NotNull
    @ApiModelProperty(
        value = "Atributo descrição.\nDeve existir algum tipo de indicação\nque mostre a quantidade de amostras\nsuficiente ou insuficiente para a operação.\nDeve existir uma visualização em lista\nde todas as amostras pertencentes a operação\nna sua tela de visualização\nDeve existir um recurso para facilitar\na conferência do plano de amostragem\ndurante a criação da operação/plano de\namostragem.\n\nDescreve de forma simples a operação.",
        required = true
    )
    private String descricao;

    /**
     * O volume ou peso total da operação.
     */
    @NotNull
    @ApiModelProperty(value = "O volume ou peso total da operação.", required = true)
    private Integer volumePeso;

    /**
     * O horário de início da operação.
     */
    @ApiModelProperty(value = "O horário de início da operação.")
    private ZonedDateTime inicio;

    /**
     * O horário de término da operação.
     */
    @ApiModelProperty(value = "O horário de término da operação.")
    private ZonedDateTime fim;

    /**
     * Atributo quantidadeAmostras mostra\nquantas amostras devem fazer parte da operação.\nFoco em cumprir o plano de amostragem.\nUma possível solução seria um botão\npara criar novas operações usando\noperações passadas como modelo, outra\nsolução mais elaborada seria existir\nvários planos de amostragem já\ncadastrados, esses planos teriam um nome\ne uma lista de amostras, assim sempre\nque uma operação selecionar este plano\nnovas amostras seriam criadas, usando\na lista do plano de amostragem como\nmodelo.\nOutra alternativa seria usar o lembrete como guia.\n\nQuantas amostras devem ter nessa operação.
     */
    @NotNull
    @ApiModelProperty(
        value = "Atributo quantidadeAmostras mostra\nquantas amostras devem fazer parte da operação.\nFoco em cumprir o plano de amostragem.\nUma possível solução seria um botão\npara criar novas operações usando\noperações passadas como modelo, outra\nsolução mais elaborada seria existir\nvários planos de amostragem já\ncadastrados, esses planos teriam um nome\ne uma lista de amostras, assim sempre\nque uma operação selecionar este plano\nnovas amostras seriam criadas, usando\na lista do plano de amostragem como\nmodelo.\nOutra alternativa seria usar o lembrete como guia.\n\nQuantas amostras devem ter nessa operação.",
        required = true
    )
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

    public ZonedDateTime getInicio() {
        return inicio;
    }

    public void setInicio(ZonedDateTime inicio) {
        this.inicio = inicio;
    }

    public ZonedDateTime getFim() {
        return fim;
    }

    public void setFim(ZonedDateTime fim) {
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
