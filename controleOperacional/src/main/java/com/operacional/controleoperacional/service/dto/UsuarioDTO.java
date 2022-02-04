package com.operacional.controleoperacional.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.operacional.controleoperacional.domain.Usuario} entity.
 */
public class UsuarioDTO implements Serializable {

    private Long id;

    /**
     * Atributo chave.\nA chave do usuário
     */
    @NotNull
    @Size(min = 4, max = 4)
    @ApiModelProperty(value = "Atributo chave.\nA chave do usuário", required = true)
    private String chave;

    /**
     * Atributo nome.\nO nome do usuário
     */
    @NotNull
    @ApiModelProperty(value = "Atributo nome.\nO nome do usuário", required = true)
    private String nome;

    /**
     * Atributo linksExternos.\nVerificar a viabilidade de usar iframe
     */
    @ApiModelProperty(value = "Atributo linksExternos.\nVerificar a viabilidade de usar iframe")
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
