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
     * Observações que forem necessárias para melhorar\na identificação da origem amostra.
     */
    @Column(name = "descricao_de_origen")
    private String descricaoDeOrigen;

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

    @Size(max = 50)
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Size(max = 50)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

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
    @JsonIgnoreProperties(value = { "produto", "tipoOperacao" }, allowSetters = true)
    private Operacao operacao;

    /**
     * Define qual é a origem da amostra
     */
    @ManyToOne(optional = false)
    @NotNull
    private OrigemAmostra origemAmostra;

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

    public String getDescricaoDeOrigen() {
        return this.descricaoDeOrigen;
    }

    public Amostra descricaoDeOrigen(String descricaoDeOrigen) {
        this.setDescricaoDeOrigen(descricaoDeOrigen);
        return this;
    }

    public void setDescricaoDeOrigen(String descricaoDeOrigen) {
        this.descricaoDeOrigen = descricaoDeOrigen;
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

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Amostra createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Amostra createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Amostra lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Amostra lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
            ", descricaoDeOrigen='" + getDescricaoDeOrigen() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", recebimentoNoLaboratorio='" + getRecebimentoNoLaboratorio() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
