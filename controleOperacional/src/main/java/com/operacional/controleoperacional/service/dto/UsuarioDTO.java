package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Usuario} entity.
 */
public class UsuarioDTO implements Serializable {

    private Long id;

    /**
     * Chave do usuário, deve ter 4 caracteres.
     */
    @NotNull
    @Size(min = 4, max = 4)
    @ApiModelProperty(value = "Chave do usuário, deve ter 4 caracteres.", required = true)
    private String chave;

    /**
     * Nome do usuário.
     */
    @NotNull
    @ApiModelProperty(value = "Nome do usuário.", required = true)
    private String nome;

    /**
     * Atributo linksExternos.\nVerificar a viabilidade de usar iframe\n\nLinks e lembretes de apoio para o usuário.
     */
    @ApiModelProperty(
        value = "Atributo linksExternos.\nVerificar a viabilidade de usar iframe\n\nLinks e lembretes de apoio para o usuário."
    )
    @Lob
    private String linksExternos;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLinksExternos() {
        return linksExternos;
    }

    public void setLinksExternos(String linksExternos) {
        this.linksExternos = linksExternos;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioDTO)) {
            return false;
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, usuarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioDTO{" +
            "id=" + getId() +
            ", chave='" + getChave() + "'" +
            ", nome='" + getNome() + "'" +
            ", linksExternos='" + getLinksExternos() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
