package com.operacional.controleoperacional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Chave do usuário, deve ter 4 caracteres.
     */
    @NotNull
    @Size(min = 4, max = 4)
    @Column(name = "chave", length = 4, nullable = false, unique = true)
    private String chave;

    /**
     * Nome do usuário.
     */
    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    /**
     * Atributo linksExternos.\nVerificar a viabilidade de usar iframe\n\nLinks e lembretes de apoio para o usuário.
     */
    @Lob
    @Column(name = "links_externos")
    private String linksExternos;

    /**
     * Irá ligar usuário ao User
     */
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(mappedBy = "usuariosAuts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "usuariosAuts" }, allowSetters = true)
    private Set<TipoRelatorio> relAutorizados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChave() {
        return this.chave;
    }

    public Usuario chave(String chave) {
        this.setChave(chave);
        return this;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getNome() {
        return this.nome;
    }

    public Usuario nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLinksExternos() {
        return this.linksExternos;
    }

    public Usuario linksExternos(String linksExternos) {
        this.setLinksExternos(linksExternos);
        return this;
    }

    public void setLinksExternos(String linksExternos) {
        this.linksExternos = linksExternos;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Usuario user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<TipoRelatorio> getRelAutorizados() {
        return this.relAutorizados;
    }

    public void setRelAutorizados(Set<TipoRelatorio> tipoRelatorios) {
        if (this.relAutorizados != null) {
            this.relAutorizados.forEach(i -> i.removeUsuariosAut(this));
        }
        if (tipoRelatorios != null) {
            tipoRelatorios.forEach(i -> i.addUsuariosAut(this));
        }
        this.relAutorizados = tipoRelatorios;
    }

    public Usuario relAutorizados(Set<TipoRelatorio> tipoRelatorios) {
        this.setRelAutorizados(tipoRelatorios);
        return this;
    }

    public Usuario addRelAutorizados(TipoRelatorio tipoRelatorio) {
        this.relAutorizados.add(tipoRelatorio);
        tipoRelatorio.getUsuariosAuts().add(this);
        return this;
    }

    public Usuario removeRelAutorizados(TipoRelatorio tipoRelatorio) {
        this.relAutorizados.remove(tipoRelatorio);
        tipoRelatorio.getUsuariosAuts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", chave='" + getChave() + "'" +
            ", nome='" + getNome() + "'" +
            ", linksExternos='" + getLinksExternos() + "'" +
            "}";
    }
}
