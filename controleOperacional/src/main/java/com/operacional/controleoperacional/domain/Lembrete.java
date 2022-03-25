package com.operacional.controleoperacional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade Lembrete.\n@author Diego.\nOs lembrestes devem aparecer nas telas de visualização e edição\ndos relatórios e das operações, para os quais estiverem associados.\nIsso deve ocorrer pelos tipos associados com os lembretes.\nLembretes podem ser associadados a um tipo de relatório e/ou\num tipo de operação.
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
     * Nome dado para o lembrete
     */
    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    /**
     * Lembrete de apoio para relatório e/ou operação.
     */
    @Lob
    @Column(name = "descricao", nullable = false)
    private String descricao;

    /**
     * Define os lembretes que servirão como guia para\ntodos os relatórios desse tipo
     */
    @ManyToOne
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

    public String getDescricao() {
        return this.descricao;
    }

    public Lembrete descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
