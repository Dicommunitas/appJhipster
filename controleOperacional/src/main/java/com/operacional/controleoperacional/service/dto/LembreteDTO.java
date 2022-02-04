package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Lembrete} entity.
 */
@ApiModel(
    description = "Entidade Lembrete.\n@author Diego.\nOs lembrestes devem ficar ao lada da tela de visualização e edição\ndos relatórios que o tever associado."
)
public class LembreteDTO implements Serializable {

    private Long id;

    /**
     * Atributo data.\nA data em que o Lembrete foi criado
     */
    @NotNull
    @ApiModelProperty(value = "Atributo data.\nA data em que o Lembrete foi criado", required = true)
    private ZonedDateTime data;

    /**
     * Atributo nome.\nnome do lembrete
     */
    @NotNull
    @ApiModelProperty(value = "Atributo nome.\nnome do lembrete", required = true)
    private String nome;

    /**
     * Atributo texto.\nLembretes diversos
     */

    @ApiModelProperty(value = "Atributo texto.\nLembretes diversos", required = true)
    @Lob
    private String texto;

    private TipoRelatorioDTO tipoRelatorio;

    private TipoOperacaoDTO tipoOperacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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
            ", data='" + getData() + "'" +
            ", nome='" + getNome() + "'" +
            ", texto='" + getTexto() + "'" +
            ", tipoRelatorio=" + getTipoRelatorio() +
            ", tipoOperacao=" + getTipoOperacao() +
            "}";
    }
}
