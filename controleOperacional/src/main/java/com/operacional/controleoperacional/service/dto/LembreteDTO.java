package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Lembrete} entity.
 */
@ApiModel(
    description = "Entidade Lembrete.\n@author Diego.\nOs lembrestes devem aparecer nas telas de visualização e edição\ndos relatórios e das operações, para os quais estiverem associados.\nIsso deve ocorrer pelos tipos associados com os lembretes.\nLembretes podem ser associadados a um tipo de relatório e/ou\num tipo de operação."
)
public class LembreteDTO implements Serializable {

    private Long id;

    /**
     * Nome dado para o lembrete
     */
    @NotNull
    @ApiModelProperty(value = "Nome dado para o lembrete", required = true)
    private String nome;

    /**
     * Lembrete de apoio para relatório e/ou operação.
     */

    @ApiModelProperty(value = "Lembrete de apoio para relatório e/ou operação.", required = true)
    @Lob
    private String descricao;

    private TipoRelatorioDTO tipoRelatorio;

    private TipoOperacaoDTO tipoOperacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoRelatorioDTO getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(TipoRelatorioDTO tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
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
        if (!(o instanceof LembreteDTO)) {
            return false;
        }

        LembreteDTO lembreteDTO = (LembreteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lembreteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LembreteDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", tipoRelatorio=" + getTipoRelatorio() +
            ", tipoOperacao=" + getTipoOperacao() +
            "}";
    }
}
