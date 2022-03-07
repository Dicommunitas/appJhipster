package com.operacional.controleoperacional.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade Amostra.\n@author Diego.
 */
@Entity
@Table(name = "amostra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Amostra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Data e hora que a amostra foi coletada.
     */
    @Column(name = "data_hora_coleta")
    private Instant dataHoraColeta;

    /**
     * Observações que forem necessárias para melhorar\na identificação da amostra.
     */
    @Column(name = "observacao")
    private String observacao;

    /**
     * Identificador que \"ligue/identifique\" essa\namostra em outro sistema.
     */
    @Column(name = "identificador_externo")
    private String identificadorExterno;

    /**
     * Identifica se a amostra está ou não no laboratório.
     */
    @Column(name = "recebimento_no_laboratorio")
    private Instant recebimentoNoLaboratorio;

    /**
     * Descreve quais serão as finalidades de uma amostra
     */
    @OneToMany(mappedBy = "amostra")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tipoFinalidadeAmostra", "amostra" }, allowSetters = true)
    private Set<FinalidadeAmostra> finalidades = new HashSet<>();

    /**
     * Define a qual operação pertence a amostra
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tipoOperacao" }, allowSetters = true)
    private Operacao operacao;

    /**
     * Define qual é a origem da amostra
     */
    @ManyToOne(optional = false)
    @NotNull
    private OrigemAmostra origemAmostra;

    /**
     * Define qual o produto da amostra
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "alertas" }, allowSetters = true)
    private Produto produto;

    /**
     * Define qual o tipo da amostra (Composta, Corrida...)
     */
    @ManyToOne(optional = false)
    @NotNull
    private TipoAmostra tipoAmostra;

    /**
     * Quem retirou a amostra, responsável pela amostragem.
     */
    @ManyToOne
    private User amostradaPor;

    /**
     * Quem recebeu a amostra no laboratório.
     */
    @ManyToOne
    private User recebidaPor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Amostra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataHoraColeta() {
        return this.dataHoraColeta;
    }

    public Amostra dataHoraColeta(Instant dataHoraColeta) {
        this.setDataHoraColeta(dataHoraColeta);
        return this;
    }

    public void setDataHoraColeta(Instant dataHoraColeta) {
        this.dataHoraColeta = dataHoraColeta;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public Amostra observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getIdentificadorExterno() {
        return this.identificadorExterno;
    }

    public Amostra identificadorExterno(String identificadorExterno) {
        this.setIdentificadorExterno(identificadorExterno);
        return this;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public Instant getRecebimentoNoLaboratorio() {
        return this.recebimentoNoLaboratorio;
    }

    public Amostra recebimentoNoLaboratorio(Instant recebimentoNoLaboratorio) {
        this.setRecebimentoNoLaboratorio(recebimentoNoLaboratorio);
        return this;
    }

    public void setRecebimentoNoLaboratorio(Instant recebimentoNoLaboratorio) {
        this.recebimentoNoLaboratorio = recebimentoNoLaboratorio;
    }

    public Set<FinalidadeAmostra> getFinalidades() {
        return this.finalidades;
    }

    public void setFinalidades(Set<FinalidadeAmostra> finalidadeAmostras) {
        if (this.finalidades != null) {
            this.finalidades.forEach(i -> i.setAmostra(null));
        }
        if (finalidadeAmostras != null) {
            finalidadeAmostras.forEach(i -> i.setAmostra(this));
        }
        this.finalidades = finalidadeAmostras;
    }

    public Amostra finalidades(Set<FinalidadeAmostra> finalidadeAmostras) {
        this.setFinalidades(finalidadeAmostras);
        return this;
    }

    public Amostra addFinalidades(FinalidadeAmostra finalidadeAmostra) {
        this.finalidades.add(finalidadeAmostra);
        finalidadeAmostra.setAmostra(this);
        return this;
    }

    public Amostra removeFinalidades(FinalidadeAmostra finalidadeAmostra) {
        this.finalidades.remove(finalidadeAmostra);
        finalidadeAmostra.setAmostra(null);
        return this;
    }

    public Operacao getOperacao() {
        return this.operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    public Amostra operacao(Operacao operacao) {
        this.setOperacao(operacao);
        return this;
    }

    public OrigemAmostra getOrigemAmostra() {
        return this.origemAmostra;
    }

    public void setOrigemAmostra(OrigemAmostra origemAmostra) {
        this.origemAmostra = origemAmostra;
    }

    public Amostra origemAmostra(OrigemAmostra origemAmostra) {
        this.setOrigemAmostra(origemAmostra);
        return this;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Amostra produto(Produto produto) {
        this.setProduto(produto);
        return this;
    }

    public TipoAmostra getTipoAmostra() {
        return this.tipoAmostra;
    }

    public void setTipoAmostra(TipoAmostra tipoAmostra) {
        this.tipoAmostra = tipoAmostra;
    }

    public Amostra tipoAmostra(TipoAmostra tipoAmostra) {
        this.setTipoAmostra(tipoAmostra);
        return this;
    }

    public User getAmostradaPor() {
        return this.amostradaPor;
    }

    public void setAmostradaPor(User user) {
        this.amostradaPor = user;
    }

    public Amostra amostradaPor(User user) {
        this.setAmostradaPor(user);
        return this;
    }

    public User getRecebidaPor() {
        return this.recebidaPor;
    }

    public void setRecebidaPor(User user) {
        this.recebidaPor = user;
    }

    public Amostra recebidaPor(User user) {
        this.setRecebidaPor(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Amostra)) {
            return false;
        }
        return id != null && id.equals(((Amostra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Amostra{" +
            "id=" + getId() +
            ", dataHoraColeta='" + getDataHoraColeta() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", recebimentoNoLaboratorio='" + getRecebimentoNoLaboratorio() + "'" +
            "}";
    }
}
