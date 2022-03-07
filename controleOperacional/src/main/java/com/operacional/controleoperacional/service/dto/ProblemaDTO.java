package com.operacional.controleoperacional.service.dto;

import com.operacional.controleoperacional.domain.enumeration.Criticidade;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Problema} entity.
 */
@ApiModel(
    description = "Entidade Problema.\n@author Diego.\nUm problema pode ser qualquer anormalidade encontrada.\nUm problema pode ter vários status para sua finalização\ncada status devem ser tratado por uma área necessária ao\ntratamento do problema.\nOs problemas devem ter sua apresentação para\no usuário de forma diferenciada com relação\naos status resolvidos e não resolvidos, assim\ncomo problemas já finalizados e não finalizados. Para\nfácil identificação."
)
public class ProblemaDTO implements Serializable {

    private Long id;

    /**
     * A data em que o problema foi verificado.
     */
    @NotNull
    @ApiModelProperty(value = "A data em que o problema foi verificado.", required = true)
    private LocalDate dataVerificacao;

    /**
     * Descrição do problema.
     */
    @NotNull
    @ApiModelProperty(value = "Descrição do problema.", required = true)
    private String descricao;

    /**
     * Gravidade do problema.\nSe o problema tiver criticidade IMEDIATA\no atributo impácto não pode estar em branco
     */
    @NotNull
    @ApiModelProperty(
        value = "Gravidade do problema.\nSe o problema tiver criticidade IMEDIATA\no atributo impácto não pode estar em branco",
        required = true
    )
    private Criticidade criticidade;

    /**
     * O impácto do problema para o sistema como um todo.\nSe o problema tiver criticidade IMEDIATA\no atributo impácto não pode estar em branco
     */
    @NotNull
    @ApiModelProperty(
        value = "O impácto do problema para o sistema como um todo.\nSe o problema tiver criticidade IMEDIATA\no atributo impácto não pode estar em branco",
        required = true
    )
    private String impacto;

    /**
     * Imforma se o problema foi finalizado/sanado.\nSomente quem criou o problema tem permisão\npara informar sua finalização.\nO problema só pode ser finalizado se ele tiver\ntodos os seus status resolvidos.
     */
    @ApiModelProperty(
        value = "Imforma se o problema foi finalizado/sanado.\nSomente quem criou o problema tem permisão\npara informar sua finalização.\nO problema só pode ser finalizado se ele tiver\ntodos os seus status resolvidos."
    )
    private LocalDate dataFinalizacao;

    /**
     * Uma imagem que possa facilitar a identificação do problema.
     */

    @ApiModelProperty(value = "Uma imagem que possa facilitar a identificação do problema.")
    @Lob
    private byte[] foto;

    private String fotoContentType;
    private UserDTO relator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataVerificacao() {
        return dataVerificacao;
    }

    public void setDataVerificacao(LocalDate dataVerificacao) {
        this.dataVerificacao = dataVerificacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Criticidade getCriticidade() {
        return criticidade;
    }

    public void setCriticidade(Criticidade criticidade) {
        this.criticidade = criticidade;
    }

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public LocalDate getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(LocalDate dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return fotoContentType;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public UserDTO getRelator() {
        return relator;
    }

    public void setRelator(UserDTO relator) {
        this.relator = relator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProblemaDTO)) {
            return false;
        }

        ProblemaDTO problemaDTO = (ProblemaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, problemaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProblemaDTO{" +
            "id=" + getId() +
            ", dataVerificacao='" + getDataVerificacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", criticidade='" + getCriticidade() + "'" +
            ", impacto='" + getImpacto() + "'" +
            ", dataFinalizacao='" + getDataFinalizacao() + "'" +
            ", foto='" + getFoto() + "'" +
            ", relator=" + getRelator() +
            "}";
    }
}
