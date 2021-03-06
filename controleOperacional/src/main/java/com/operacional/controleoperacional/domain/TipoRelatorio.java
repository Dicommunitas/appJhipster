package com.operacional.controleoperacional.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade TipoRelatorio.\n@author Diego.\nO TipoRelatorio irá descrever de que tipo é\no relatório, relatório de sutur, relatório píer, cco...\nDeve existir uma tela com um botão para listar todos\nos relatórios agrupados por seus tipos.
 */
@Entity
@Table(name = "tipo_relatorio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TipoRelatorio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Nome dado ao grupo de relatórios.
     */
    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    /**
     * Descreve quais usuários podem alterar os relatórios\ndesse tipo
     */
    @ManyToMany
    @JoinTable(
        name = "rel_tipo_relatorio__usuarios_aut",
        joinColumns = @JoinColumn(name = "tipo_relatorio_id"),
        inverseJoinColumns = @JoinColumn(name = "usuarios_aut_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> usuariosAuts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoRelatorio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public TipoRelatorio nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<User> getUsuariosAuts() {
        return this.usuariosAuts;
    }

    public void setUsuariosAuts(Set<User> users) {
        this.usuariosAuts = users;
    }

    public TipoRelatorio usuariosAuts(Set<User> users) {
        this.setUsuariosAuts(users);
        return this;
    }

    public TipoRelatorio addUsuariosAut(User user) {
        this.usuariosAuts.add(user);
        return this;
    }

    public TipoRelatorio removeUsuariosAut(User user) {
        this.usuariosAuts.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoRelatorio)) {
            return false;
        }
        return id != null && id.equals(((TipoRelatorio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoRelatorio{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
