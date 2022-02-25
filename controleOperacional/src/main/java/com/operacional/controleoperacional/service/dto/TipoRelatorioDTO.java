package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.TipoRelatorio} entity.
 */
@ApiModel(
    description = "Entidade TipoRelatorio.\n@author Diego.\nO TipoRelatorio irá descrever de que tipo é\no relatório, relatório de sutur, relatório píer, cco...\nDeve existir uma tela com um botão para listar todos\nos relatórios agrupados por seus tipos."
)
public class TipoRelatorioDTO implements Serializable {

    private Long id;

    /**
     * Nome dado ao grupo de relatórios.
     */
    @NotNull
    @ApiModelProperty(value = "Nome dado ao grupo de relatórios.", required = true)
    private String nome;

    private Set<UsuarioDTO> usuariosAuts = new HashSet<>();

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

    public Set<UsuarioDTO> getUsuariosAuts() {
        return usuariosAuts;
    }

    public void setUsuariosAuts(Set<UsuarioDTO> usuariosAuts) {
        this.usuariosAuts = usuariosAuts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoRelatorioDTO)) {
            return false;
        }

        TipoRelatorioDTO tipoRelatorioDTO = (TipoRelatorioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoRelatorioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoRelatorioDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", usuariosAuts=" + getUsuariosAuts() +
            "}";
    }
}
