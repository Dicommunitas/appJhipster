package com.operacional.controleoperacional.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Operacao} entity.
 */
@Schema(
    description = "Entidade Operação.\n@author Diego.\nAo criar uma Operação deve existir\numa opção de usar operações passadas\ncomo modelo para sua criação, inclusive\ncopiando o plano de amostragem"
)
public class OperacaoDTO implements Serializable {

    private Long id;

    /**
     * Descreve de forma simples a operação.
     */
    @NotNull
    @Schema(description = "Descreve de forma simples a operação.", required = true)
    private String descricao;

    /**
     * O volume ou peso total da operação. Em metros cúbicos ou toneladas
     */
    @NotNull
    @Schema(description = "O volume ou peso total da operação. Em metros cúbicos ou toneladas", required = true)
    private Integer volumePeso;

    /**
     * O horário de início da operação.
     */
    @Schema(description = "O horário de início da operação.")
    private Instant inicio;

    /**
     * O horário de término da operação.
     */
    @Schema(description = "O horário de término da operação.")
    private Instant fim;

    /**
     * Quantas amostras devem ter nessa operação.
     */
    @Schema(description = "Quantas amostras devem ter nessa operação.")
    private Integer quantidadeAmostras;

    /**
     * Observações que forem necessárias para melhorar\na descrição dos acontecimentos relativos da operação.
     */
    @Schema(description = "Observações que forem necessárias para melhorar\na descrição dos acontecimentos relativos da operação.")
    private String observacao;

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private ProdutoDTO produto;

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

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
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
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", produto=" + getProduto() +
            ", tipoOperacao=" + getTipoOperacao() +
            "}";
    }
}
