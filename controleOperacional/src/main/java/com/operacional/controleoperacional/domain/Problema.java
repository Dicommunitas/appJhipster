package com.operacional.controleoperacional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.operacional.controleoperacional.domain.enumeration.Criticidade;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade Problema.\n@author Diego.\nUm problema pode ser qualquer anormalidade encontrada.\nUm problema pode ter vários status para sua finalização\ncada status devem ser tratado por uma área necessária ao\ntratamento do problema.\nOs problemas devem ter sua apresentação para\no usuário de forma diferenciada com relação\naos status resolvidos e não resolvidos, assim\ncomo problemas já finalizados e não finalizados. Para\nfácil identificação.
 */
@Entity
@Table(name = "problema")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Problema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * A data em que o problema foi verificado.
     */
    @NotNull
    @Column(name = "data_verificacao", nullable = false)
    private LocalDate dataVerificacao;

    /**
     * Descrição do problema.
     */
    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    /**
     * Gravidade do problema.\nSe o problema tiver criticidade IMEDIATA\no atributo impácto não pode estar em branco
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "criticidade", nullable = false)
    private Criticidade criticidade;

    /**
     * O impácto do problema para o sistema como um todo.\nSe o problema tiver criticidade IMEDIATA\no atributo impácto não pode estar em branco
     */
    @NotNull
    @Column(name = "impacto", nullable = false)
    private String impacto;

    /**
     * Imforma se o problema foi finalizado/sanado.\nSomente quem criou o problema tem permisão\npara informar sua finalização.\nO problema só pode ser finalizado se ele tiver\ntodos os seus status resolvidos.
     */
    @Column(name = "data_finalizacao")
    private LocalDate dataFinalizacao;

    /**
     * Uma imagem que possa facilitar a identificação do problema.\nA imagem não pode ter mai que 40KB.
     */
    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "foto_content_type")
    private String fotoContentType;

    /**
     * Todo problema deverá ter pelo menos\num Status, que será um relato da situação\ninicial do problema.
     */
    @OneToMany(mappedBy = "problema")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "relator", "responsavel", "problema" }, allowSetters = true)
    private Set<Status> statuses = new HashSet<>();

    /**
     * Quem é o relator do problema.
     */
    @ManyToOne(optional = false)
    @NotNull
    private User relator;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Problema id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataVerificacao() {
        return this.dataVerificacao;
    }

    public Problema dataVerificacao(LocalDate dataVerificacao) {
        this.setDataVerificacao(dataVerificacao);
        return this;
    }

    public void setDataVerificacao(LocalDate dataVerificacao) {
        this.dataVerificacao = dataVerificacao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Problema descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Criticidade getCriticidade() {
        return this.criticidade;
    }

    public Problema criticidade(Criticidade criticidade) {
        this.setCriticidade(criticidade);
        return this;
    }

    public void setCriticidade(Criticidade criticidade) {
        this.criticidade = criticidade;
    }

    public String getImpacto() {
        return this.impacto;
    }

    public Problema impacto(String impacto) {
        this.setImpacto(impacto);
        return this;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public LocalDate getDataFinalizacao() {
        return this.dataFinalizacao;
    }

    public Problema dataFinalizacao(LocalDate dataFinalizacao) {
        this.setDataFinalizacao(dataFinalizacao);
        return this;
    }

    public void setDataFinalizacao(LocalDate dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public byte[] getFoto() {
        return this.foto;
    }

    public Problema foto(byte[] foto) {
        this.setFoto(foto);
        return this;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return this.fotoContentType;
    }

    public Problema fotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
        return this;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public Set<Status> getStatuses() {
        return this.statuses;
    }

    public void setStatuses(Set<Status> statuses) {
        if (this.statuses != null) {
            this.statuses.forEach(i -> i.setProblema(null));
        }
        if (statuses != null) {
            statuses.forEach(i -> i.setProblema(this));
        }
        this.statuses = statuses;
    }

    public Problema statuses(Set<Status> statuses) {
        this.setStatuses(statuses);
        return this;
    }

    public Problema addStatus(Status status) {
        this.statuses.add(status);
        status.setProblema(this);
        return this;
    }

    public Problema removeStatus(Status status) {
        this.statuses.remove(status);
        status.setProblema(null);
        return this;
    }

    public User getRelator() {
        return this.relator;
    }

    public void setRelator(User user) {
        this.relator = user;
    }

    public Problema relator(User user) {
        this.setRelator(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Problema)) {
            return false;
        }
        return id != null && id.equals(((Problema) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Problema{" +
            "id=" + getId() +
            ", dataVerificacao='" + getDataVerificacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", criticidade='" + getCriticidade() + "'" +
            ", impacto='" + getImpacto() + "'" +
            ", dataFinalizacao='" + getDataFinalizacao() + "'" +
            ", foto='" + getFoto() + "'" +
            ", fotoContentType='" + getFotoContentType() + "'" +
            "}";
    }
}
