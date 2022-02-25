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
    description = "Entidade Problema.\n@author Diego.\nUm problema pode ser qualquer anormalidade encontrada.\nUm problema pode ter vários status para sua finalização\ncada status devem ser tratado por uma área necessária ao\ntratamento do problema.\nOs problemas devem ter sua apresentação para\no usuário de forma diferenciada com relação\naos status resolvidos e não resolvidos, assim\ncomo problemas já finalizados e não finalizados."
)
public class ProblemaDTO implements Serializable {

    private Long id;

    /**
     * A data em que o problema foi verificado
     */
    @NotNull
    @ApiModelProperty(value = "A data em que o problema foi verificado", required = true)
    private LocalDate data;

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
     * Imforma se o problema foi finalizado/sanado.\nSomente quem criou o problema tem permisão\npara aceitar sua finalização.\nO problema só pode ser finalizado se ele tiver\ntodos os seus status resolvidos.
     */
    @ApiModelProperty(
        value = "Imforma se o problema foi finalizado/sanado.\nSomente quem criou o problema tem permisão\npara aceitar sua finalização.\nO problema só pode ser finalizado se ele tiver\ntodos os seus status resolvidos."
    )
    private Boolean aceitarFinalizacao;

    @Lob
    private byte[] foto;

    private String fotoContentType;

    /**
     * Impácto do problema ao sistema como um todo.\nSe o problema tiver criticidade IMEDIATA\no atributo impácto não pode estar em branco
     */
    @NotNull
    @ApiModelProperty(
        value = "Impácto do problema ao sistema como um todo.\nSe o problema tiver criticidade IMEDIATA\no atributo impácto não pode estar em branco",
        required = true
    )
    private String impacto;

    private UsuarioDTO relator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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

    public Boolean getAceitarFinalizacao() {
        return aceitarFinalizacao;
    }

    public void setAceitarFinalizacao(Boolean aceitarFinalizacao) {
        this.aceitarFinalizacao = aceitarFinalizacao;
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

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public UsuarioDTO getRelator() {
        return relator;
    }

    public void setRelator(UsuarioDTO relator) {
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
            ", data='" + getData() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", criticidade='" + getCriticidade() + "'" +
            ", aceitarFinalizacao='" + getAceitarFinalizacao() + "'" +
            ", foto='" + getFoto() + "'" +
            ", impacto='" + getImpacto() + "'" +
            ", relator=" + getRelator() +
            "}";
    }
}
