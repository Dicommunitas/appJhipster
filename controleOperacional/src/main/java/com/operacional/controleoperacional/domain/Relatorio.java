package com.operacional.controleoperacional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade Relatorio.\n@author Diego.\nNa tela de visualização/edição de um relatório\ndeve ser mostrado uma lista das operações com fim em branco\nou início em até 12 horas antes da hora de criação do relatório,\numa lista dos problemas com o campo aceitarFinalizacao em falso\ne uma lista das amostras criadas em até 12 horas antes da\ncriação do relatório.\nTambém deve ser apresentado um \"satatus\" do\nplano de amostragem.\n\nUm relatório só pode ser alterado/excluído por quem o criou\ne se ele for o último relatório do seu tipo.\n\nNa tela de visualização/edição de um relatório\ndeve ser mostrado as telas dos linksExternos\nexemplo a tela do Paranaguá pilots.\n\nAo usar iframe verificar o funcionamento de links com\ncaminhos relativos, para dentro do projeto com links internos,\ne caminhos absolutos, para links externos.\nhttps:
 */
@Entity
@Table(name = "relatorio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Relatorio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Atributo relato.\nVerificar a viabilidade de usar um objeto\ne não um texto.\n\nUsar comparação de texto para visualizar as\nalterações entre o últomo relatório criado e o último\nrelatório criado pelo usuário.
     */
    @Lob
    @Column(name = "relato", nullable = false)
    private String relato;

    /**
     * Atributo linksExternos.\nVerificar a viabilidade de usar iframe
     */
    @Column(name = "links_externos")
    private String linksExternos;

    /**
     * Define de que tipo é o relatório (Supervisor, controle, píer...)
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "usuariosAuts" }, allowSetters = true)
    private TipoRelatorio tipo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "relAutorizados" }, allowSetters = true)
    private Usuario responsavel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Relatorio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelato() {
        return this.relato;
    }

    public Relatorio relato(String relato) {
        this.setRelato(relato);
        return this;
    }

    public void setRelato(String relato) {
        this.relato = relato;
    }

    public String getLinksExternos() {
        return this.linksExternos;
    }

    public Relatorio linksExternos(String linksExternos) {
        this.setLinksExternos(linksExternos);
        return this;
    }

    public void setLinksExternos(String linksExternos) {
        this.linksExternos = linksExternos;
    }

    public TipoRelatorio getTipo() {
        return this.tipo;
    }

    public void setTipo(TipoRelatorio tipoRelatorio) {
        this.tipo = tipoRelatorio;
    }

    public Relatorio tipo(TipoRelatorio tipoRelatorio) {
        this.setTipo(tipoRelatorio);
        return this;
    }

    public Usuario getResponsavel() {
        return this.responsavel;
    }

    public void setResponsavel(Usuario usuario) {
        this.responsavel = usuario;
    }

    public Relatorio responsavel(Usuario usuario) {
        this.setResponsavel(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Relatorio)) {
            return false;
        }
        return id != null && id.equals(((Relatorio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Relatorio{" +
            "id=" + getId() +
            ", relato='" + getRelato() + "'" +
            ", linksExternos='" + getLinksExternos() + "'" +
            "}";
    }
}
