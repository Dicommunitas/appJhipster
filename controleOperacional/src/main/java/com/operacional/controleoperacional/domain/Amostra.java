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
     * Atributo dataHora.\nnão deve ser obrigatório e as amostras\npor padrão devem ser ordenadas pela data\nficando as mais novas por primeiro na\nvizualização e as sem data por útimo
     */
    @Column(name = "data_hora")
    private Instant dataHora;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "identificador_externo")
    private String identificadorExterno;

    @Column(name = "amostra_no_laboratorio")
    private Boolean amostraNoLaboratorio;

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
     * Verificar a aplicabilidade de criar previamente\namostras em bloco, exemplo, ao criar uma operação nova\ncopiar suas amostras com os campos \"amostrado por\"\ne \"recebidaPor\" em branco.\nQuem retirou a amostra, responsável pela amostragem.\nEnquanto estiver em branco a amostra pode ser alterada\ne excluída a qualquer momento e deverá ter outra\ncor de identificação na listagem.
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "relAutorizados" }, allowSetters = true)
    private Usuario amostradaPor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "relAutorizados" }, allowSetters = true)
    private Usuario recebidaPor;

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

    public Instant getDataHora() {
        return this.dataHora;
    }

    public Amostra dataHora(Instant dataHora) {
        this.setDataHora(dataHora);
        return this;
    }

    public void setDataHora(Instant dataHora) {
        this.dataHora = dataHora;
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

    public Boolean getAmostraNoLaboratorio() {
        return this.amostraNoLaboratorio;
    }

    public Amostra amostraNoLaboratorio(Boolean amostraNoLaboratorio) {
        this.setAmostraNoLaboratorio(amostraNoLaboratorio);
        return this;
    }

    public void setAmostraNoLaboratorio(Boolean amostraNoLaboratorio) {
        this.amostraNoLaboratorio = amostraNoLaboratorio;
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

    public Usuario getAmostradaPor() {
        return this.amostradaPor;
    }

    public void setAmostradaPor(Usuario usuario) {
        this.amostradaPor = usuario;
    }

    public Amostra amostradaPor(Usuario usuario) {
        this.setAmostradaPor(usuario);
        return this;
    }

    public Usuario getRecebidaPor() {
        return this.recebidaPor;
    }

    public void setRecebidaPor(Usuario usuario) {
        this.recebidaPor = usuario;
    }

    public Amostra recebidaPor(Usuario usuario) {
        this.setRecebidaPor(usuario);
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
            ", dataHora='" + getDataHora() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", amostraNoLaboratorio='" + getAmostraNoLaboratorio() + "'" +
            "}";
    }
}
