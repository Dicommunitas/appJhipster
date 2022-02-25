package com.operacional.controleoperacional.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade Operação.\n@author Diego.\nAo criar uma Operação deve existir\numa opção de usar operações passadas\ncomo modelo para sua criação, inclusive\ncopiando o plano de amostragem
 */
@Entity
@Table(name = "operacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Operacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Atributo descrição.\nDeve existir algum tipo de indicação\nque mostre a quantidade de amostras\nsuficiente ou insuficiente para a operação.\nDeve existir uma visualização em lista\nde todas as amostras pertencentes a operação\nna sua tela de visualização\nDeve existir um recurso para facilitar\na conferência do plano de amostragem\ndurante a criação da operação/plano de\namostragem.\n\nDescreve de forma simples a operação.
     */
    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    /**
     * O volume ou peso total da operação.
     */
    @NotNull
    @Column(name = "volume_peso", nullable = false)
    private Integer volumePeso;

    /**
     * O horário de início da operação.
     */
    @Column(name = "inicio")
    private ZonedDateTime inicio;

    /**
     * O horário de término da operação.
     */
    @Column(name = "fim")
    private ZonedDateTime fim;

    /**
     * Atributo quantidadeAmostras mostra\nquantas amostras devem fazer parte da operação.\nFoco em cumprir o plano de amostragem.\nUma possível solução seria um botão\npara criar novas operações usando\noperações passadas como modelo, outra\nsolução mais elaborada seria existir\nvários planos de amostragem já\ncadastrados, esses planos teriam um nome\ne uma lista de amostras, assim sempre\nque uma operação selecionar este plano\nnovas amostras seriam criadas, usando\na lista do plano de amostragem como\nmodelo.\nOutra alternativa seria usar o lembrete como guia.\n\nQuantas amostras devem ter nessa operação.
     */
    @NotNull
    @Column(name = "quantidade_amostras", nullable = false)
    private Integer quantidadeAmostras;

    /**
     * Observações que forem necessárias para melhorar\na descrição dos acontecimentos relativos da operação.
     */
    @Column(name = "observacao")
    private String observacao;

    /**
     * Define de que tipo é a operação\n(Barcaça - Carga, Caminhão - Descarga...)
     */
    @ManyToOne(optional = false)
    @NotNull
    private TipoOperacao tipoOperacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Operacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Operacao descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getVolumePeso() {
        return this.volumePeso;
    }

    public Operacao volumePeso(Integer volumePeso) {
        this.setVolumePeso(volumePeso);
        return this;
    }

    public void setVolumePeso(Integer volumePeso) {
        this.volumePeso = volumePeso;
    }

    public ZonedDateTime getInicio() {
        return this.inicio;
    }

    public Operacao inicio(ZonedDateTime inicio) {
        this.setInicio(inicio);
        return this;
    }

    public void setInicio(ZonedDateTime inicio) {
        this.inicio = inicio;
    }

    public ZonedDateTime getFim() {
        return this.fim;
    }

    public Operacao fim(ZonedDateTime fim) {
        this.setFim(fim);
        return this;
    }

    public void setFim(ZonedDateTime fim) {
        this.fim = fim;
    }

    public Integer getQuantidadeAmostras() {
        return this.quantidadeAmostras;
    }

    public Operacao quantidadeAmostras(Integer quantidadeAmostras) {
        this.setQuantidadeAmostras(quantidadeAmostras);
        return this;
    }

    public void setQuantidadeAmostras(Integer quantidadeAmostras) {
        this.quantidadeAmostras = quantidadeAmostras;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public Operacao observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public TipoOperacao getTipoOperacao() {
        return this.tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public Operacao tipoOperacao(TipoOperacao tipoOperacao) {
        this.setTipoOperacao(tipoOperacao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operacao)) {
            return false;
        }
        return id != null && id.equals(((Operacao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operacao{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", volumePeso=" + getVolumePeso() +
            ", inicio='" + getInicio() + "'" +
            ", fim='" + getFim() + "'" +
            ", quantidadeAmostras=" + getQuantidadeAmostras() +
            ", observacao='" + getObservacao() + "'" +
            "}";
    }
}
