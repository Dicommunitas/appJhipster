package com.operacional.controleoperacional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade Lembrete.\n@author Diego.\nOs lembrestes devem ficar ao lada da tela de visualização e edição\ndos relatórios que o tever associado.
 */
@Entity
@Table(name = "lembrete")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lembrete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Atributo data.\nA data em que o Lembrete foi criado
     */
    @NotNull
    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    /**
     * Atributo nome.\nnome do lembrete
     */
    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    /**
     * Atributo texto.\nLembretes diversos
     */
    @Lob
    @Column(name = "texto", nullable = false)
    private String texto;

    /**
     * Define os lembretes que servirão como guia para\ntodos os relatórios desse tipo
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "usuariosAuts" }, allowSetters = true)
    private TipoRelatorio tipoRelatorio;

    /**
     * O lembrete deverá ser exibido sempre que\numa operação selecionar o mesmo tipoOperacao\ndo lembrete.
     */
    @ManyToOne
    private TipoOperacao tipoOperacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lembrete id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getData() {
        return this.data;
    }

    public Lembrete data(ZonedDateTime data) {
        this.setData(data);
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public String getNome() {
        return this.nome;
    }

    public Lembrete nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTexto() {
        return this.texto;
    }

    public Lembrete texto(String texto) {
        this.setTexto(texto);
        return this;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public TipoRelatorio getTipoRelatorio() {
        return this.tipoRelatorio;
    }

    public void setTipoRelatorio(TipoRelatorio tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public Lembrete tipoRelatorio(TipoRelatorio tipoRelatorio) {
        this.setTipoRelatorio(tipoRelatorio);
        return this;
    }

    public TipoOperacao getTipoOperacao() {
        return this.tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public Lembrete tipoOperacao(TipoOperacao tipoOperacao) {
        this.setTipoOperacao(tipoOperacao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lembrete)) {
            return false;
        }
        return id != null && id.equals(((Lembrete) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lembrete{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", nome='" + getNome() + "'" +
            ", texto='" + getTexto() + "'" +
            "}";
    }
}
