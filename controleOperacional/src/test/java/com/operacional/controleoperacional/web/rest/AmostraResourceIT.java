package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.Amostra;
import com.operacional.controleoperacional.domain.FinalidadeAmostra;
import com.operacional.controleoperacional.domain.Operacao;
import com.operacional.controleoperacional.domain.OrigemAmostra;
import com.operacional.controleoperacional.domain.Produto;
import com.operacional.controleoperacional.domain.TipoAmostra;
import com.operacional.controleoperacional.domain.User;
import com.operacional.controleoperacional.repository.AmostraRepository;
import com.operacional.controleoperacional.service.criteria.AmostraCriteria;
import com.operacional.controleoperacional.service.dto.AmostraDTO;
import com.operacional.controleoperacional.service.mapper.AmostraMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AmostraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AmostraResourceIT {

    private static final Instant DEFAULT_DATA_HORA_COLETA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORA_COLETA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICADOR_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR_EXTERNO = "BBBBBBBBBB";

    private static final Instant DEFAULT_RECEBIMENTO_NO_LABORATORIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEBIMENTO_NO_LABORATORIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/amostras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AmostraRepository amostraRepository;

    @Autowired
    private AmostraMapper amostraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAmostraMockMvc;

    private Amostra amostra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Amostra createEntity(EntityManager em) {
        Amostra amostra = new Amostra()
            .dataHoraColeta(DEFAULT_DATA_HORA_COLETA)
            .observacao(DEFAULT_OBSERVACAO)
            .identificadorExterno(DEFAULT_IDENTIFICADOR_EXTERNO)
            .recebimentoNoLaboratorio(DEFAULT_RECEBIMENTO_NO_LABORATORIO)
            //.createdBy(DEFAULT_CREATED_BY)
            //.createdDate(DEFAULT_CREATED_DATE)
            //.lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            //.lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
            ;
        // Add required entity
        Operacao operacao;
        if (TestUtil.findAll(em, Operacao.class).isEmpty()) {
            operacao = OperacaoResourceIT.createEntity(em);
            em.persist(operacao);
            em.flush();
        } else {
            operacao = TestUtil.findAll(em, Operacao.class).get(0);
        }
        amostra.setOperacao(operacao);
        // Add required entity
        OrigemAmostra origemAmostra;
        if (TestUtil.findAll(em, OrigemAmostra.class).isEmpty()) {
            origemAmostra = OrigemAmostraResourceIT.createEntity(em);
            em.persist(origemAmostra);
            em.flush();
        } else {
            origemAmostra = TestUtil.findAll(em, OrigemAmostra.class).get(0);
        }
        amostra.setOrigemAmostra(origemAmostra);
        // Add required entity
        Produto produto;
        if (TestUtil.findAll(em, Produto.class).isEmpty()) {
            produto = ProdutoResourceIT.createEntity(em);
            em.persist(produto);
            em.flush();
        } else {
            produto = TestUtil.findAll(em, Produto.class).get(0);
        }
        amostra.setProduto(produto);
        // Add required entity
        TipoAmostra tipoAmostra;
        if (TestUtil.findAll(em, TipoAmostra.class).isEmpty()) {
            tipoAmostra = TipoAmostraResourceIT.createEntity(em);
            em.persist(tipoAmostra);
            em.flush();
        } else {
            tipoAmostra = TestUtil.findAll(em, TipoAmostra.class).get(0);
        }
        amostra.setTipoAmostra(tipoAmostra);
        return amostra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Amostra createUpdatedEntity(EntityManager em) {
        Amostra amostra = new Amostra()
            .dataHoraColeta(UPDATED_DATA_HORA_COLETA)
            .observacao(UPDATED_OBSERVACAO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .recebimentoNoLaboratorio(UPDATED_RECEBIMENTO_NO_LABORATORIO)
            //.createdBy(UPDATED_CREATED_BY)
            //.createdDate(UPDATED_CREATED_DATE)
            //.lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            //.lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
            ;
        // Add required entity
        Operacao operacao;
        if (TestUtil.findAll(em, Operacao.class).isEmpty()) {
            operacao = OperacaoResourceIT.createUpdatedEntity(em);
            em.persist(operacao);
            em.flush();
        } else {
            operacao = TestUtil.findAll(em, Operacao.class).get(0);
        }
        amostra.setOperacao(operacao);
        // Add required entity
        OrigemAmostra origemAmostra;
        if (TestUtil.findAll(em, OrigemAmostra.class).isEmpty()) {
            origemAmostra = OrigemAmostraResourceIT.createUpdatedEntity(em);
            em.persist(origemAmostra);
            em.flush();
        } else {
            origemAmostra = TestUtil.findAll(em, OrigemAmostra.class).get(0);
        }
        amostra.setOrigemAmostra(origemAmostra);
        // Add required entity
        Produto produto;
        if (TestUtil.findAll(em, Produto.class).isEmpty()) {
            produto = ProdutoResourceIT.createUpdatedEntity(em);
            em.persist(produto);
            em.flush();
        } else {
            produto = TestUtil.findAll(em, Produto.class).get(0);
        }
        amostra.setProduto(produto);
        // Add required entity
        TipoAmostra tipoAmostra;
        if (TestUtil.findAll(em, TipoAmostra.class).isEmpty()) {
            tipoAmostra = TipoAmostraResourceIT.createUpdatedEntity(em);
            em.persist(tipoAmostra);
            em.flush();
        } else {
            tipoAmostra = TestUtil.findAll(em, TipoAmostra.class).get(0);
        }
        amostra.setTipoAmostra(tipoAmostra);
        return amostra;
    }

    @BeforeEach
    public void initTest() {
        amostra = createEntity(em);
    }

    @Test
    @Transactional
    void createAmostra() throws Exception {
        int databaseSizeBeforeCreate = amostraRepository.findAll().size();
        // Create the Amostra
        AmostraDTO amostraDTO = amostraMapper.toDto(amostra);
        restAmostraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amostraDTO)))
            .andExpect(status().isCreated());

        // Validate the Amostra in the database
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeCreate + 1);
        Amostra testAmostra = amostraList.get(amostraList.size() - 1);
        assertThat(testAmostra.getDataHoraColeta()).isEqualTo(DEFAULT_DATA_HORA_COLETA);
        assertThat(testAmostra.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testAmostra.getIdentificadorExterno()).isEqualTo(DEFAULT_IDENTIFICADOR_EXTERNO);
        assertThat(testAmostra.getRecebimentoNoLaboratorio()).isEqualTo(DEFAULT_RECEBIMENTO_NO_LABORATORIO);
        assertThat(testAmostra.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAmostra.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAmostra.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAmostra.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createAmostraWithExistingId() throws Exception {
        // Create the Amostra with an existing ID
        amostra.setId(1L);
        AmostraDTO amostraDTO = amostraMapper.toDto(amostra);

        int databaseSizeBeforeCreate = amostraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmostraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amostraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Amostra in the database
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = amostraRepository.findAll().size();
        // set the field null
        amostra.setCreatedBy(null);

        // Create the Amostra, which fails.
        AmostraDTO amostraDTO = amostraMapper.toDto(amostra);

        restAmostraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amostraDTO)))
            .andExpect(status().isBadRequest());

        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAmostras() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList
        restAmostraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amostra.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHoraColeta").value(hasItem(DEFAULT_DATA_HORA_COLETA.toString())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].identificadorExterno").value(hasItem(DEFAULT_IDENTIFICADOR_EXTERNO)))
            .andExpect(jsonPath("$.[*].recebimentoNoLaboratorio").value(hasItem(DEFAULT_RECEBIMENTO_NO_LABORATORIO.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAmostra() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get the amostra
        restAmostraMockMvc
            .perform(get(ENTITY_API_URL_ID, amostra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(amostra.getId().intValue()))
            .andExpect(jsonPath("$.dataHoraColeta").value(DEFAULT_DATA_HORA_COLETA.toString()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.identificadorExterno").value(DEFAULT_IDENTIFICADOR_EXTERNO))
            .andExpect(jsonPath("$.recebimentoNoLaboratorio").value(DEFAULT_RECEBIMENTO_NO_LABORATORIO.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAmostrasByIdFiltering() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        Long id = amostra.getId();

        defaultAmostraShouldBeFound("id.equals=" + id);
        defaultAmostraShouldNotBeFound("id.notEquals=" + id);

        defaultAmostraShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAmostraShouldNotBeFound("id.greaterThan=" + id);

        defaultAmostraShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAmostraShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAmostrasByDataHoraColetaIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHoraColeta equals to DEFAULT_DATA_HORA_COLETA
        defaultAmostraShouldBeFound("dataHoraColeta.equals=" + DEFAULT_DATA_HORA_COLETA);

        // Get all the amostraList where dataHoraColeta equals to UPDATED_DATA_HORA_COLETA
        defaultAmostraShouldNotBeFound("dataHoraColeta.equals=" + UPDATED_DATA_HORA_COLETA);
    }

    @Test
    @Transactional
    void getAllAmostrasByDataHoraColetaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHoraColeta not equals to DEFAULT_DATA_HORA_COLETA
        defaultAmostraShouldNotBeFound("dataHoraColeta.notEquals=" + DEFAULT_DATA_HORA_COLETA);

        // Get all the amostraList where dataHoraColeta not equals to UPDATED_DATA_HORA_COLETA
        defaultAmostraShouldBeFound("dataHoraColeta.notEquals=" + UPDATED_DATA_HORA_COLETA);
    }

    @Test
    @Transactional
    void getAllAmostrasByDataHoraColetaIsInShouldWork() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHoraColeta in DEFAULT_DATA_HORA_COLETA or UPDATED_DATA_HORA_COLETA
        defaultAmostraShouldBeFound("dataHoraColeta.in=" + DEFAULT_DATA_HORA_COLETA + "," + UPDATED_DATA_HORA_COLETA);

        // Get all the amostraList where dataHoraColeta equals to UPDATED_DATA_HORA_COLETA
        defaultAmostraShouldNotBeFound("dataHoraColeta.in=" + UPDATED_DATA_HORA_COLETA);
    }

    @Test
    @Transactional
    void getAllAmostrasByDataHoraColetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHoraColeta is not null
        defaultAmostraShouldBeFound("dataHoraColeta.specified=true");

        // Get all the amostraList where dataHoraColeta is null
        defaultAmostraShouldNotBeFound("dataHoraColeta.specified=false");
    }

    @Test
    @Transactional
    void getAllAmostrasByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where observacao equals to DEFAULT_OBSERVACAO
        defaultAmostraShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the amostraList where observacao equals to UPDATED_OBSERVACAO
        defaultAmostraShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllAmostrasByObservacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where observacao not equals to DEFAULT_OBSERVACAO
        defaultAmostraShouldNotBeFound("observacao.notEquals=" + DEFAULT_OBSERVACAO);

        // Get all the amostraList where observacao not equals to UPDATED_OBSERVACAO
        defaultAmostraShouldBeFound("observacao.notEquals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllAmostrasByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultAmostraShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the amostraList where observacao equals to UPDATED_OBSERVACAO
        defaultAmostraShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllAmostrasByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where observacao is not null
        defaultAmostraShouldBeFound("observacao.specified=true");

        // Get all the amostraList where observacao is null
        defaultAmostraShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllAmostrasByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where observacao contains DEFAULT_OBSERVACAO
        defaultAmostraShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the amostraList where observacao contains UPDATED_OBSERVACAO
        defaultAmostraShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllAmostrasByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where observacao does not contain DEFAULT_OBSERVACAO
        defaultAmostraShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the amostraList where observacao does not contain UPDATED_OBSERVACAO
        defaultAmostraShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllAmostrasByIdentificadorExternoIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where identificadorExterno equals to DEFAULT_IDENTIFICADOR_EXTERNO
        defaultAmostraShouldBeFound("identificadorExterno.equals=" + DEFAULT_IDENTIFICADOR_EXTERNO);

        // Get all the amostraList where identificadorExterno equals to UPDATED_IDENTIFICADOR_EXTERNO
        defaultAmostraShouldNotBeFound("identificadorExterno.equals=" + UPDATED_IDENTIFICADOR_EXTERNO);
    }

    @Test
    @Transactional
    void getAllAmostrasByIdentificadorExternoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where identificadorExterno not equals to DEFAULT_IDENTIFICADOR_EXTERNO
        defaultAmostraShouldNotBeFound("identificadorExterno.notEquals=" + DEFAULT_IDENTIFICADOR_EXTERNO);

        // Get all the amostraList where identificadorExterno not equals to UPDATED_IDENTIFICADOR_EXTERNO
        defaultAmostraShouldBeFound("identificadorExterno.notEquals=" + UPDATED_IDENTIFICADOR_EXTERNO);
    }

    @Test
    @Transactional
    void getAllAmostrasByIdentificadorExternoIsInShouldWork() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where identificadorExterno in DEFAULT_IDENTIFICADOR_EXTERNO or UPDATED_IDENTIFICADOR_EXTERNO
        defaultAmostraShouldBeFound("identificadorExterno.in=" + DEFAULT_IDENTIFICADOR_EXTERNO + "," + UPDATED_IDENTIFICADOR_EXTERNO);

        // Get all the amostraList where identificadorExterno equals to UPDATED_IDENTIFICADOR_EXTERNO
        defaultAmostraShouldNotBeFound("identificadorExterno.in=" + UPDATED_IDENTIFICADOR_EXTERNO);
    }

    @Test
    @Transactional
    void getAllAmostrasByIdentificadorExternoIsNullOrNotNull() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where identificadorExterno is not null
        defaultAmostraShouldBeFound("identificadorExterno.specified=true");

        // Get all the amostraList where identificadorExterno is null
        defaultAmostraShouldNotBeFound("identificadorExterno.specified=false");
    }

    @Test
    @Transactional
    void getAllAmostrasByIdentificadorExternoContainsSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where identificadorExterno contains DEFAULT_IDENTIFICADOR_EXTERNO
        defaultAmostraShouldBeFound("identificadorExterno.contains=" + DEFAULT_IDENTIFICADOR_EXTERNO);

        // Get all the amostraList where identificadorExterno contains UPDATED_IDENTIFICADOR_EXTERNO
        defaultAmostraShouldNotBeFound("identificadorExterno.contains=" + UPDATED_IDENTIFICADOR_EXTERNO);
    }

    @Test
    @Transactional
    void getAllAmostrasByIdentificadorExternoNotContainsSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where identificadorExterno does not contain DEFAULT_IDENTIFICADOR_EXTERNO
        defaultAmostraShouldNotBeFound("identificadorExterno.doesNotContain=" + DEFAULT_IDENTIFICADOR_EXTERNO);

        // Get all the amostraList where identificadorExterno does not contain UPDATED_IDENTIFICADOR_EXTERNO
        defaultAmostraShouldBeFound("identificadorExterno.doesNotContain=" + UPDATED_IDENTIFICADOR_EXTERNO);
    }

    @Test
    @Transactional
    void getAllAmostrasByRecebimentoNoLaboratorioIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where recebimentoNoLaboratorio equals to DEFAULT_RECEBIMENTO_NO_LABORATORIO
        defaultAmostraShouldBeFound("recebimentoNoLaboratorio.equals=" + DEFAULT_RECEBIMENTO_NO_LABORATORIO);

        // Get all the amostraList where recebimentoNoLaboratorio equals to UPDATED_RECEBIMENTO_NO_LABORATORIO
        defaultAmostraShouldNotBeFound("recebimentoNoLaboratorio.equals=" + UPDATED_RECEBIMENTO_NO_LABORATORIO);
    }

    @Test
    @Transactional
    void getAllAmostrasByRecebimentoNoLaboratorioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where recebimentoNoLaboratorio not equals to DEFAULT_RECEBIMENTO_NO_LABORATORIO
        defaultAmostraShouldNotBeFound("recebimentoNoLaboratorio.notEquals=" + DEFAULT_RECEBIMENTO_NO_LABORATORIO);

        // Get all the amostraList where recebimentoNoLaboratorio not equals to UPDATED_RECEBIMENTO_NO_LABORATORIO
        defaultAmostraShouldBeFound("recebimentoNoLaboratorio.notEquals=" + UPDATED_RECEBIMENTO_NO_LABORATORIO);
    }

    @Test
    @Transactional
    void getAllAmostrasByRecebimentoNoLaboratorioIsInShouldWork() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where recebimentoNoLaboratorio in DEFAULT_RECEBIMENTO_NO_LABORATORIO or UPDATED_RECEBIMENTO_NO_LABORATORIO
        defaultAmostraShouldBeFound(
            "recebimentoNoLaboratorio.in=" + DEFAULT_RECEBIMENTO_NO_LABORATORIO + "," + UPDATED_RECEBIMENTO_NO_LABORATORIO
        );

        // Get all the amostraList where recebimentoNoLaboratorio equals to UPDATED_RECEBIMENTO_NO_LABORATORIO
        defaultAmostraShouldNotBeFound("recebimentoNoLaboratorio.in=" + UPDATED_RECEBIMENTO_NO_LABORATORIO);
    }

    @Test
    @Transactional
    void getAllAmostrasByRecebimentoNoLaboratorioIsNullOrNotNull() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where recebimentoNoLaboratorio is not null
        defaultAmostraShouldBeFound("recebimentoNoLaboratorio.specified=true");

        // Get all the amostraList where recebimentoNoLaboratorio is null
        defaultAmostraShouldNotBeFound("recebimentoNoLaboratorio.specified=false");
    }

    @Test
    @Transactional
    void getAllAmostrasByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where createdBy equals to DEFAULT_CREATED_BY
        defaultAmostraShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the amostraList where createdBy equals to UPDATED_CREATED_BY
        defaultAmostraShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAmostrasByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where createdBy not equals to DEFAULT_CREATED_BY
        defaultAmostraShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the amostraList where createdBy not equals to UPDATED_CREATED_BY
        defaultAmostraShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAmostrasByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultAmostraShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the amostraList where createdBy equals to UPDATED_CREATED_BY
        defaultAmostraShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAmostrasByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where createdBy is not null
        defaultAmostraShouldBeFound("createdBy.specified=true");

        // Get all the amostraList where createdBy is null
        defaultAmostraShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAmostrasByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where createdBy contains DEFAULT_CREATED_BY
        defaultAmostraShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the amostraList where createdBy contains UPDATED_CREATED_BY
        defaultAmostraShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAmostrasByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where createdBy does not contain DEFAULT_CREATED_BY
        defaultAmostraShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the amostraList where createdBy does not contain UPDATED_CREATED_BY
        defaultAmostraShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAmostrasByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where createdDate equals to DEFAULT_CREATED_DATE
        defaultAmostraShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the amostraList where createdDate equals to UPDATED_CREATED_DATE
        defaultAmostraShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAmostrasByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultAmostraShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the amostraList where createdDate not equals to UPDATED_CREATED_DATE
        defaultAmostraShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAmostrasByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultAmostraShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the amostraList where createdDate equals to UPDATED_CREATED_DATE
        defaultAmostraShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAmostrasByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where createdDate is not null
        defaultAmostraShouldBeFound("createdDate.specified=true");

        // Get all the amostraList where createdDate is null
        defaultAmostraShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAmostrasByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultAmostraShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the amostraList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAmostraShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAmostrasByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultAmostraShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the amostraList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultAmostraShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAmostrasByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultAmostraShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the amostraList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAmostraShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAmostrasByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where lastModifiedBy is not null
        defaultAmostraShouldBeFound("lastModifiedBy.specified=true");

        // Get all the amostraList where lastModifiedBy is null
        defaultAmostraShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAmostrasByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultAmostraShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the amostraList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultAmostraShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAmostrasByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultAmostraShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the amostraList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultAmostraShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAmostrasByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultAmostraShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the amostraList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultAmostraShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAmostrasByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultAmostraShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the amostraList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultAmostraShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAmostrasByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultAmostraShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the amostraList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultAmostraShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAmostrasByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where lastModifiedDate is not null
        defaultAmostraShouldBeFound("lastModifiedDate.specified=true");

        // Get all the amostraList where lastModifiedDate is null
        defaultAmostraShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAmostrasByFinalidadesIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);
        FinalidadeAmostra finalidades;
        if (TestUtil.findAll(em, FinalidadeAmostra.class).isEmpty()) {
            finalidades = FinalidadeAmostraResourceIT.createEntity(em);
            em.persist(finalidades);
            em.flush();
        } else {
            finalidades = TestUtil.findAll(em, FinalidadeAmostra.class).get(0);
        }
        em.persist(finalidades);
        em.flush();
        amostra.addFinalidades(finalidades);
        amostraRepository.saveAndFlush(amostra);
        Long finalidadesId = finalidades.getId();

        // Get all the amostraList where finalidades equals to finalidadesId
        defaultAmostraShouldBeFound("finalidadesId.equals=" + finalidadesId);

        // Get all the amostraList where finalidades equals to (finalidadesId + 1)
        defaultAmostraShouldNotBeFound("finalidadesId.equals=" + (finalidadesId + 1));
    }

    @Test
    @Transactional
    void getAllAmostrasByOperacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);
        Operacao operacao;
        if (TestUtil.findAll(em, Operacao.class).isEmpty()) {
            operacao = OperacaoResourceIT.createEntity(em);
            em.persist(operacao);
            em.flush();
        } else {
            operacao = TestUtil.findAll(em, Operacao.class).get(0);
        }
        em.persist(operacao);
        em.flush();
        amostra.setOperacao(operacao);
        amostraRepository.saveAndFlush(amostra);
        Long operacaoId = operacao.getId();

        // Get all the amostraList where operacao equals to operacaoId
        defaultAmostraShouldBeFound("operacaoId.equals=" + operacaoId);

        // Get all the amostraList where operacao equals to (operacaoId + 1)
        defaultAmostraShouldNotBeFound("operacaoId.equals=" + (operacaoId + 1));
    }

    @Test
    @Transactional
    void getAllAmostrasByOrigemAmostraIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);
        OrigemAmostra origemAmostra;
        if (TestUtil.findAll(em, OrigemAmostra.class).isEmpty()) {
            origemAmostra = OrigemAmostraResourceIT.createEntity(em);
            em.persist(origemAmostra);
            em.flush();
        } else {
            origemAmostra = TestUtil.findAll(em, OrigemAmostra.class).get(0);
        }
        em.persist(origemAmostra);
        em.flush();
        amostra.setOrigemAmostra(origemAmostra);
        amostraRepository.saveAndFlush(amostra);
        Long origemAmostraId = origemAmostra.getId();

        // Get all the amostraList where origemAmostra equals to origemAmostraId
        defaultAmostraShouldBeFound("origemAmostraId.equals=" + origemAmostraId);

        // Get all the amostraList where origemAmostra equals to (origemAmostraId + 1)
        defaultAmostraShouldNotBeFound("origemAmostraId.equals=" + (origemAmostraId + 1));
    }

    @Test
    @Transactional
    void getAllAmostrasByProdutoIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);
        Produto produto;
        if (TestUtil.findAll(em, Produto.class).isEmpty()) {
            produto = ProdutoResourceIT.createEntity(em);
            em.persist(produto);
            em.flush();
        } else {
            produto = TestUtil.findAll(em, Produto.class).get(0);
        }
        em.persist(produto);
        em.flush();
        amostra.setProduto(produto);
        amostraRepository.saveAndFlush(amostra);
        Long produtoId = produto.getId();

        // Get all the amostraList where produto equals to produtoId
        defaultAmostraShouldBeFound("produtoId.equals=" + produtoId);

        // Get all the amostraList where produto equals to (produtoId + 1)
        defaultAmostraShouldNotBeFound("produtoId.equals=" + (produtoId + 1));
    }

    @Test
    @Transactional
    void getAllAmostrasByTipoAmostraIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);
        TipoAmostra tipoAmostra;
        if (TestUtil.findAll(em, TipoAmostra.class).isEmpty()) {
            tipoAmostra = TipoAmostraResourceIT.createEntity(em);
            em.persist(tipoAmostra);
            em.flush();
        } else {
            tipoAmostra = TestUtil.findAll(em, TipoAmostra.class).get(0);
        }
        em.persist(tipoAmostra);
        em.flush();
        amostra.setTipoAmostra(tipoAmostra);
        amostraRepository.saveAndFlush(amostra);
        Long tipoAmostraId = tipoAmostra.getId();

        // Get all the amostraList where tipoAmostra equals to tipoAmostraId
        defaultAmostraShouldBeFound("tipoAmostraId.equals=" + tipoAmostraId);

        // Get all the amostraList where tipoAmostra equals to (tipoAmostraId + 1)
        defaultAmostraShouldNotBeFound("tipoAmostraId.equals=" + (tipoAmostraId + 1));
    }

    @Test
    @Transactional
    void getAllAmostrasByAmostradaPorIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);
        User amostradaPor;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            amostradaPor = UserResourceIT.createEntity(em);
            em.persist(amostradaPor);
            em.flush();
        } else {
            amostradaPor = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(amostradaPor);
        em.flush();
        amostra.setAmostradaPor(amostradaPor);
        amostraRepository.saveAndFlush(amostra);
        Long amostradaPorId = amostradaPor.getId();

        // Get all the amostraList where amostradaPor equals to amostradaPorId
        defaultAmostraShouldBeFound("amostradaPorId.equals=" + amostradaPorId);

        // Get all the amostraList where amostradaPor equals to (amostradaPorId + 1)
        defaultAmostraShouldNotBeFound("amostradaPorId.equals=" + (amostradaPorId + 1));
    }

    @Test
    @Transactional
    void getAllAmostrasByRecebidaPorIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);
        User recebidaPor;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            recebidaPor = UserResourceIT.createEntity(em);
            em.persist(recebidaPor);
            em.flush();
        } else {
            recebidaPor = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(recebidaPor);
        em.flush();
        amostra.setRecebidaPor(recebidaPor);
        amostraRepository.saveAndFlush(amostra);
        Long recebidaPorId = recebidaPor.getId();

        // Get all the amostraList where recebidaPor equals to recebidaPorId
        defaultAmostraShouldBeFound("recebidaPorId.equals=" + recebidaPorId);

        // Get all the amostraList where recebidaPor equals to (recebidaPorId + 1)
        defaultAmostraShouldNotBeFound("recebidaPorId.equals=" + (recebidaPorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAmostraShouldBeFound(String filter) throws Exception {
        restAmostraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amostra.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHoraColeta").value(hasItem(DEFAULT_DATA_HORA_COLETA.toString())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].identificadorExterno").value(hasItem(DEFAULT_IDENTIFICADOR_EXTERNO)))
            .andExpect(jsonPath("$.[*].recebimentoNoLaboratorio").value(hasItem(DEFAULT_RECEBIMENTO_NO_LABORATORIO.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restAmostraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAmostraShouldNotBeFound(String filter) throws Exception {
        restAmostraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAmostraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAmostra() throws Exception {
        // Get the amostra
        restAmostraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAmostra() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        int databaseSizeBeforeUpdate = amostraRepository.findAll().size();

        // Update the amostra
        Amostra updatedAmostra = amostraRepository.findById(amostra.getId()).get();
        // Disconnect from session so that the updates on updatedAmostra are not directly saved in db
        em.detach(updatedAmostra);
        updatedAmostra
            .dataHoraColeta(UPDATED_DATA_HORA_COLETA)
            .observacao(UPDATED_OBSERVACAO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .recebimentoNoLaboratorio(UPDATED_RECEBIMENTO_NO_LABORATORIO)
            //.createdBy(UPDATED_CREATED_BY)
            //.createdDate(UPDATED_CREATED_DATE)
            //.lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            //.lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
            ;
        AmostraDTO amostraDTO = amostraMapper.toDto(updatedAmostra);

        restAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amostraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amostraDTO))
            )
            .andExpect(status().isOk());

        // Validate the Amostra in the database
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeUpdate);
        Amostra testAmostra = amostraList.get(amostraList.size() - 1);
        assertThat(testAmostra.getDataHoraColeta()).isEqualTo(UPDATED_DATA_HORA_COLETA);
        assertThat(testAmostra.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testAmostra.getIdentificadorExterno()).isEqualTo(UPDATED_IDENTIFICADOR_EXTERNO);
        assertThat(testAmostra.getRecebimentoNoLaboratorio()).isEqualTo(UPDATED_RECEBIMENTO_NO_LABORATORIO);
        assertThat(testAmostra.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAmostra.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAmostra.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAmostra.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAmostra() throws Exception {
        int databaseSizeBeforeUpdate = amostraRepository.findAll().size();
        amostra.setId(count.incrementAndGet());

        // Create the Amostra
        AmostraDTO amostraDTO = amostraMapper.toDto(amostra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amostraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amostra in the database
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAmostra() throws Exception {
        int databaseSizeBeforeUpdate = amostraRepository.findAll().size();
        amostra.setId(count.incrementAndGet());

        // Create the Amostra
        AmostraDTO amostraDTO = amostraMapper.toDto(amostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amostra in the database
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAmostra() throws Exception {
        int databaseSizeBeforeUpdate = amostraRepository.findAll().size();
        amostra.setId(count.incrementAndGet());

        // Create the Amostra
        AmostraDTO amostraDTO = amostraMapper.toDto(amostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmostraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amostraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Amostra in the database
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAmostraWithPatch() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        int databaseSizeBeforeUpdate = amostraRepository.findAll().size();

        // Update the amostra using partial update
        Amostra partialUpdatedAmostra = new Amostra();
        partialUpdatedAmostra.setId(amostra.getId());

        partialUpdatedAmostra
            .observacao(UPDATED_OBSERVACAO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .recebimentoNoLaboratorio(UPDATED_RECEBIMENTO_NO_LABORATORIO)
            //.createdBy(UPDATED_CREATED_BY)
            //.lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
            ;

        restAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmostra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmostra))
            )
            .andExpect(status().isOk());

        // Validate the Amostra in the database
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeUpdate);
        Amostra testAmostra = amostraList.get(amostraList.size() - 1);
        assertThat(testAmostra.getDataHoraColeta()).isEqualTo(DEFAULT_DATA_HORA_COLETA);
        assertThat(testAmostra.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testAmostra.getIdentificadorExterno()).isEqualTo(UPDATED_IDENTIFICADOR_EXTERNO);
        assertThat(testAmostra.getRecebimentoNoLaboratorio()).isEqualTo(UPDATED_RECEBIMENTO_NO_LABORATORIO);
        assertThat(testAmostra.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAmostra.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAmostra.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAmostra.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAmostraWithPatch() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        int databaseSizeBeforeUpdate = amostraRepository.findAll().size();

        // Update the amostra using partial update
        Amostra partialUpdatedAmostra = new Amostra();
        partialUpdatedAmostra.setId(amostra.getId());

        partialUpdatedAmostra
            .dataHoraColeta(UPDATED_DATA_HORA_COLETA)
            .observacao(UPDATED_OBSERVACAO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .recebimentoNoLaboratorio(UPDATED_RECEBIMENTO_NO_LABORATORIO)
            //.createdBy(UPDATED_CREATED_BY)
            //.createdDate(UPDATED_CREATED_DATE)
            //.lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            //.lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
            ;

        restAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmostra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmostra))
            )
            .andExpect(status().isOk());

        // Validate the Amostra in the database
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeUpdate);
        Amostra testAmostra = amostraList.get(amostraList.size() - 1);
        assertThat(testAmostra.getDataHoraColeta()).isEqualTo(UPDATED_DATA_HORA_COLETA);
        assertThat(testAmostra.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testAmostra.getIdentificadorExterno()).isEqualTo(UPDATED_IDENTIFICADOR_EXTERNO);
        assertThat(testAmostra.getRecebimentoNoLaboratorio()).isEqualTo(UPDATED_RECEBIMENTO_NO_LABORATORIO);
        assertThat(testAmostra.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAmostra.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAmostra.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAmostra.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAmostra() throws Exception {
        int databaseSizeBeforeUpdate = amostraRepository.findAll().size();
        amostra.setId(count.incrementAndGet());

        // Create the Amostra
        AmostraDTO amostraDTO = amostraMapper.toDto(amostra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, amostraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amostra in the database
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAmostra() throws Exception {
        int databaseSizeBeforeUpdate = amostraRepository.findAll().size();
        amostra.setId(count.incrementAndGet());

        // Create the Amostra
        AmostraDTO amostraDTO = amostraMapper.toDto(amostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amostra in the database
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAmostra() throws Exception {
        int databaseSizeBeforeUpdate = amostraRepository.findAll().size();
        amostra.setId(count.incrementAndGet());

        // Create the Amostra
        AmostraDTO amostraDTO = amostraMapper.toDto(amostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(amostraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Amostra in the database
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAmostra() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        int databaseSizeBeforeDelete = amostraRepository.findAll().size();

        // Delete the amostra
        restAmostraMockMvc
            .perform(delete(ENTITY_API_URL_ID, amostra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Amostra> amostraList = amostraRepository.findAll();
        assertThat(amostraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
