package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Relatorio} entity.
 */
@ApiModel(
    description = "Entidade Relatorio.\n@author Diego.\nNa tela de visualização/edição de um relatório\ndeve ser mostrado uma lista das operações com fim em branco\nou início em até 12 horas antes da hora de criação do relatório,\numa lista dos problemas com o campo aceitarFinalizacao em falso\ne uma lista das amostras criadas em até 12 horas antes da\ncriação do relatório.\nTambém deve ser apresentado um \"satatus\" do\nplano de amostragem.\n\nUm relatório só pode ser alterado/excluído por quem o criou\ne se ele for o último relatório do seu tipo.\n\nNa tela de visualização/edição de um relatório\ndeve ser mostrado as telas dos linksExternos\nexemplo a tela do Paranaguá pilots.\n\nAo usar iframe verificar o funcionamento de links com\ncaminhos relativos, para dentro do projeto com links internos,\ne caminhos absolutos, para links externos.\nhttps:"
)
public class RelatorioDTO implements Serializable {

    private Long id;

    /**
     * Atributo relato.\nVerificar a viabilidade de usar um objeto\ne não um texto.\n\nUsar comparação de texto para visualizar as\nalterações entre o últomo relatório criado e o último\nrelatório criado pelo usuário.
     */

    @ApiModelProperty(
        value = "Atributo relato.\nVerificar a viabilidade de usar um objeto\ne não um texto.\n\nUsar comparação de texto para visualizar as\nalterações entre o últomo relatório criado e o último\nrelatório criado pelo usuário.",
        required = true
    )
    @Lob
    private String relato;

    /**
     * Atributo linksExternos.\nVerificar a viabilidade de usar iframe
     */
    @ApiModelProperty(value = "Atributo linksExternos.\nVerificar a viabilidade de usar iframe")
    private String linksExternos;

    private TipoRelatorioDTO tipo;

    private UsuarioDTO responsavel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelato() {
        return relato;
    }

    public void setRelato(String relato) {
        this.relato = relato;
    }

    public String getLinksExternos() {
        return linksExternos;
    }

    public void setLinksExternos(String linksExternos) {
        this.linksExternos = linksExternos;
    }

    public TipoRelatorioDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoRelatorioDTO tipo) {
        this.tipo = tipo;
    }

    public UsuarioDTO getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(UsuarioDTO responsavel) {
        this.responsavel = responsavel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RelatorioDTO)) {
            return false;
        }

        RelatorioDTO relatorioDTO = (RelatorioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, relatorioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RelatorioDTO{" +
            "id=" + getId() +
            ", relato='" + getRelato() + "'" +
            ", linksExternos='" + getLinksExternos() + "'" +
            ", tipo=" + getTipo() +
            ", responsavel=" + getResponsavel() +
            "}";
    }
}
