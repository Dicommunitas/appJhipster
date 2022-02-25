package com.operacional.controleoperacional.web.rest;

import static com.operacional.controleoperacional.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.Operacao;
import com.operacional.controleoperacional.domain.TipoOperacao;
import com.operacional.controleoperacional.repository.OperacaoRepository;
import com.operacional.controleoperacional.service.criteria.OperacaoCriteria;
import com.operacional.controleoperacional.service.dto.OperacaoDTO;
import com.operacional.controleoperacional.service.mapper.OperacaoMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link OperacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperacaoResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_VOLUME_PESO = 1;
    private static final Integer UPDATED_VOLUME_PESO = 2;
    private static final Integer SMALLER_VOLUME_PESO = 1 - 1;

    private static final ZonedDateTime DEFAULT_INICIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INICIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_INICIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_FIM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FIM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FIM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_QUANTIDADE_AMOSTRAS = 1;
    private static final Integer UPDATED_QUANTIDADE_AMOSTRAS = 2;
    private static final Integer SMALLER_QUANTIDADE_AMOSTRAS = 1 - 1;

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperacaoRepository operacaoRepository;

    @Autowired
    private OperacaoMapper operacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperacaoMockMvc;

    private Operacao operacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operacao createEntity(EntityManager em) {
        Operacao operacao = new Operacao()
            .descricao(DEFAULT_DESCRICAO)
            .volumePeso(DEFAULT_VOLUME_PESO)
            .inicio(DEFAULT_INICIO)
            .fim(DEFAULT_FIM)
            .quantidadeAmostras(DEFAULT_QUANTIDADE_AMOSTRAS)
            .observacao(DEFAULT_OBSERVACAO);
        // Add required entity
        TipoOperacao tipoOperacao;
        if (TestUtil.findAll(em, TipoOperacao.class).isEmpty()) {
            tipoOperacao = TipoOperacaoResourceIT.createEntity(em);
            em.persist(tipoOperacao);
            em.flush();
        } else {
            tipoOperacao = TestUtil.findAll(em, TipoOperacao.class).get(0);
        }
        operacao.setTipoOperacao(tipoOperacao);
        return operacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operacao createUpdatedEntity(EntityManager em) {
        Operacao operacao = new Operacao()
            .descricao(UPDATED_DESCRICAO)
            .volumePeso(UPDATED_VOLUME_PESO)
            .inicio(UPDATED_INICIO)
            .fim(UPDATED_FIM)
            .quantidadeAmostras(UPDATED_QUANTIDADE_AMOSTRAS)
            .observacao(UPDATED_OBSERVACAO);
        // Add required entity
        TipoOperacao tipoOperacao;
        if (TestUtil.findAll(em, TipoOperacao.class).isEmpty()) {
            tipoOperacao = TipoOperacaoResourceIT.createUpdatedEntity(em);
            em.persist(tipoOperacao);
            em.flush();
        } else {
            tipoOperacao = TestUtil.findAll(em, TipoOperacao.class).get(0);
        }
        operacao.setTipoOperacao(tipoOperacao);
        return operacao;
    }

    @BeforeEach
    public void initTest() {
        operacao = createEntity(em);
    }

    @Test
    @Transactional
    void createOperacao() throws Exception {
        int databaseSizeBeforeCreate = operacaoRepository.findAll().size();
        // Create the Operacao
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(operacao);
        restOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operacaoDTO)))
            .andExpect(status().isCreated());

        // Validate the Operacao in the database
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Operacao testOperacao = operacaoList.get(operacaoList.size() - 1);
        assertThat(testOperacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testOperacao.getVolumePeso()).isEqualTo(DEFAULT_VOLUME_PESO);
        assertThat(testOperacao.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testOperacao.getFim()).isEqualTo(DEFAULT_FIM);
        assertThat(testOperacao.getQuantidadeAmostras()).isEqualTo(DEFAULT_QUANTIDADE_AMOSTRAS);
        assertThat(testOperacao.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
    }

    @Test
    @Transactional
    void createOperacaoWithExistingId() throws Exception {
        // Create the Operacao with an existing ID
        operacao.setId(1L);
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(operacao);

        int databaseSizeBeforeCreate = operacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operacao in the database
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = operacaoRepository.findAll().size();
        // set the field null
        operacao.setDescricao(null);

        // Create the Operacao, which fails.
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(operacao);

        restOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operacaoDTO)))
            .andExpect(status().isBadRequest());

        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVolumePesoIsRequired() throws Exception {
        int databaseSizeBeforeTest = operacaoRepository.findAll().size();
        // set the field null
        operacao.setVolumePeso(null);

        // Create the Operacao, which fails.
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(operacao);

        restOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operacaoDTO)))
            .andExpect(status().isBadRequest());

        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantidadeAmostrasIsRequired() throws Exception {
        int databaseSizeBeforeTest = operacaoRepository.findAll().size();
        // set the field null
        operacao.setQuantidadeAmostras(null);

        // Create the Operacao, which fails.
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(operacao);

        restOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operacaoDTO)))
            .andExpect(status().isBadRequest());

        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperacaos() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList
        restOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].volumePeso").value(hasItem(DEFAULT_VOLUME_PESO)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(sameInstant(DEFAULT_INICIO))))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(sameInstant(DEFAULT_FIM))))
            .andExpect(jsonPath("$.[*].quantidadeAmostras").value(hasItem(DEFAULT_QUANTIDADE_AMOSTRAS)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));
    }

    @Test
    @Transactional
    void getOperacao() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get the operacao
        restOperacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, operacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operacao.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.volumePeso").value(DEFAULT_VOLUME_PESO))
            .andExpect(jsonPath("$.inicio").value(sameInstant(DEFAULT_INICIO)))
            .andExpect(jsonPath("$.fim").value(sameInstant(DEFAULT_FIM)))
            .andExpect(jsonPath("$.quantidadeAmostras").value(DEFAULT_QUANTIDADE_AMOSTRAS))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO));
    }

    @Test
    @Transactional
    void getOperacaosByIdFiltering() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        Long id = operacao.getId();

        defaultOperacaoShouldBeFound("id.equals=" + id);
        defaultOperacaoShouldNotBeFound("id.notEquals=" + id);

        defaultOperacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOperacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultOperacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOperacaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOperacaosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where descricao equals to DEFAULT_DESCRICAO
        defaultOperacaoShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the operacaoList where descricao equals to UPDATED_DESCRICAO
        defaultOperacaoShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllOperacaosByDescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where descricao not equals to DEFAULT_DESCRICAO
        defaultOperacaoShouldNotBeFound("descricao.notEquals=" + DEFAULT_DESCRICAO);

        // Get all the operacaoList where descricao not equals to UPDATED_DESCRICAO
        defaultOperacaoShouldBeFound("descricao.notEquals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllOperacaosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultOperacaoShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the operacaoList where descricao equals to UPDATED_DESCRICAO
        defaultOperacaoShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllOperacaosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where descricao is not null
        defaultOperacaoShouldBeFound("descricao.specified=true");

        // Get all the operacaoList where descricao is null
        defaultOperacaoShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllOperacaosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where descricao contains DEFAULT_DESCRICAO
        defaultOperacaoShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the operacaoList where descricao contains UPDATED_DESCRICAO
        defaultOperacaoShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllOperacaosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where descricao does not contain DEFAULT_DESCRICAO
        defaultOperacaoShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the operacaoList where descricao does not contain UPDATED_DESCRICAO
        defaultOperacaoShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllOperacaosByVolumePesoIsEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where volumePeso equals to DEFAULT_VOLUME_PESO
        defaultOperacaoShouldBeFound("volumePeso.equals=" + DEFAULT_VOLUME_PESO);

        // Get all the operacaoList where volumePeso equals to UPDATED_VOLUME_PESO
        defaultOperacaoShouldNotBeFound("volumePeso.equals=" + UPDATED_VOLUME_PESO);
    }

    @Test
    @Transactional
    void getAllOperacaosByVolumePesoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where volumePeso not equals to DEFAULT_VOLUME_PESO
        defaultOperacaoShouldNotBeFound("volumePeso.notEquals=" + DEFAULT_VOLUME_PESO);

        // Get all the operacaoList where volumePeso not equals to UPDATED_VOLUME_PESO
        defaultOperacaoShouldBeFound("volumePeso.notEquals=" + UPDATED_VOLUME_PESO);
    }

    @Test
    @Transactional
    void getAllOperacaosByVolumePesoIsInShouldWork() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where volumePeso in DEFAULT_VOLUME_PESO or UPDATED_VOLUME_PESO
        defaultOperacaoShouldBeFound("volumePeso.in=" + DEFAULT_VOLUME_PESO + "," + UPDATED_VOLUME_PESO);

        // Get all the operacaoList where volumePeso equals to UPDATED_VOLUME_PESO
        defaultOperacaoShouldNotBeFound("volumePeso.in=" + UPDATED_VOLUME_PESO);
    }

    @Test
    @Transactional
    void getAllOperacaosByVolumePesoIsNullOrNotNull() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where volumePeso is not null
        defaultOperacaoShouldBeFound("volumePeso.specified=true");

        // Get all the operacaoList where volumePeso is null
        defaultOperacaoShouldNotBeFound("volumePeso.specified=false");
    }

    @Test
    @Transactional
    void getAllOperacaosByVolumePesoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where volumePeso is greater than or equal to DEFAULT_VOLUME_PESO
        defaultOperacaoShouldBeFound("volumePeso.greaterThanOrEqual=" + DEFAULT_VOLUME_PESO);

        // Get all the operacaoList where volumePeso is greater than or equal to UPDATED_VOLUME_PESO
        defaultOperacaoShouldNotBeFound("volumePeso.greaterThanOrEqual=" + UPDATED_VOLUME_PESO);
    }

    @Test
    @Transactional
    void getAllOperacaosByVolumePesoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where volumePeso is less than or equal to DEFAULT_VOLUME_PESO
        defaultOperacaoShouldBeFound("volumePeso.lessThanOrEqual=" + DEFAULT_VOLUME_PESO);

        // Get all the operacaoList where volumePeso is less than or equal to SMALLER_VOLUME_PESO
        defaultOperacaoShouldNotBeFound("volumePeso.lessThanOrEqual=" + SMALLER_VOLUME_PESO);
    }

    @Test
    @Transactional
    void getAllOperacaosByVolumePesoIsLessThanSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where volumePeso is less than DEFAULT_VOLUME_PESO
        defaultOperacaoShouldNotBeFound("volumePeso.lessThan=" + DEFAULT_VOLUME_PESO);

        // Get all the operacaoList where volumePeso is less than UPDATED_VOLUME_PESO
        defaultOperacaoShouldBeFound("volumePeso.lessThan=" + UPDATED_VOLUME_PESO);
    }

    @Test
    @Transactional
    void getAllOperacaosByVolumePesoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where volumePeso is greater than DEFAULT_VOLUME_PESO
        defaultOperacaoShouldNotBeFound("volumePeso.greaterThan=" + DEFAULT_VOLUME_PESO);

        // Get all the operacaoList where volumePeso is greater than SMALLER_VOLUME_PESO
        defaultOperacaoShouldBeFound("volumePeso.greaterThan=" + SMALLER_VOLUME_PESO);
    }

    @Test
    @Transactional
    void getAllOperacaosByInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where inicio equals to DEFAULT_INICIO
        defaultOperacaoShouldBeFound("inicio.equals=" + DEFAULT_INICIO);

        // Get all the operacaoList where inicio equals to UPDATED_INICIO
        defaultOperacaoShouldNotBeFound("inicio.equals=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllOperacaosByInicioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where inicio not equals to DEFAULT_INICIO
        defaultOperacaoShouldNotBeFound("inicio.notEquals=" + DEFAULT_INICIO);

        // Get all the operacaoList where inicio not equals to UPDATED_INICIO
        defaultOperacaoShouldBeFound("inicio.notEquals=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllOperacaosByInicioIsInShouldWork() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where inicio in DEFAULT_INICIO or UPDATED_INICIO
        defaultOperacaoShouldBeFound("inicio.in=" + DEFAULT_INICIO + "," + UPDATED_INICIO);

        // Get all the operacaoList where inicio equals to UPDATED_INICIO
        defaultOperacaoShouldNotBeFound("inicio.in=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllOperacaosByInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where inicio is not null
        defaultOperacaoShouldBeFound("inicio.specified=true");

        // Get all the operacaoList where inicio is null
        defaultOperacaoShouldNotBeFound("inicio.specified=false");
    }

    @Test
    @Transactional
    void getAllOperacaosByInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where inicio is greater than or equal to DEFAULT_INICIO
        defaultOperacaoShouldBeFound("inicio.greaterThanOrEqual=" + DEFAULT_INICIO);

        // Get all the operacaoList where inicio is greater than or equal to UPDATED_INICIO
        defaultOperacaoShouldNotBeFound("inicio.greaterThanOrEqual=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllOperacaosByInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where inicio is less than or equal to DEFAULT_INICIO
        defaultOperacaoShouldBeFound("inicio.lessThanOrEqual=" + DEFAULT_INICIO);

        // Get all the operacaoList where inicio is less than or equal to SMALLER_INICIO
        defaultOperacaoShouldNotBeFound("inicio.lessThanOrEqual=" + SMALLER_INICIO);
    }

    @Test
    @Transactional
    void getAllOperacaosByInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where inicio is less than DEFAULT_INICIO
        defaultOperacaoShouldNotBeFound("inicio.lessThan=" + DEFAULT_INICIO);

        // Get all the operacaoList where inicio is less than UPDATED_INICIO
        defaultOperacaoShouldBeFound("inicio.lessThan=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllOperacaosByInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where inicio is greater than DEFAULT_INICIO
        defaultOperacaoShouldNotBeFound("inicio.greaterThan=" + DEFAULT_INICIO);

        // Get all the operacaoList where inicio is greater than SMALLER_INICIO
        defaultOperacaoShouldBeFound("inicio.greaterThan=" + SMALLER_INICIO);
    }

    @Test
    @Transactional
    void getAllOperacaosByFimIsEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where fim equals to DEFAULT_FIM
        defaultOperacaoShouldBeFound("fim.equals=" + DEFAULT_FIM);

        // Get all the operacaoList where fim equals to UPDATED_FIM
        defaultOperacaoShouldNotBeFound("fim.equals=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    void getAllOperacaosByFimIsNotEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where fim not equals to DEFAULT_FIM
        defaultOperacaoShouldNotBeFound("fim.notEquals=" + DEFAULT_FIM);

        // Get all the operacaoList where fim not equals to UPDATED_FIM
        defaultOperacaoShouldBeFound("fim.notEquals=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    void getAllOperacaosByFimIsInShouldWork() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where fim in DEFAULT_FIM or UPDATED_FIM
        defaultOperacaoShouldBeFound("fim.in=" + DEFAULT_FIM + "," + UPDATED_FIM);

        // Get all the operacaoList where fim equals to UPDATED_FIM
        defaultOperacaoShouldNotBeFound("fim.in=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    void getAllOperacaosByFimIsNullOrNotNull() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where fim is not null
        defaultOperacaoShouldBeFound("fim.specified=true");

        // Get all the operacaoList where fim is null
        defaultOperacaoShouldNotBeFound("fim.specified=false");
    }

    @Test
    @Transactional
    void getAllOperacaosByFimIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where fim is greater than or equal to DEFAULT_FIM
        defaultOperacaoShouldBeFound("fim.greaterThanOrEqual=" + DEFAULT_FIM);

        // Get all the operacaoList where fim is greater than or equal to UPDATED_FIM
        defaultOperacaoShouldNotBeFound("fim.greaterThanOrEqual=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    void getAllOperacaosByFimIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where fim is less than or equal to DEFAULT_FIM
        defaultOperacaoShouldBeFound("fim.lessThanOrEqual=" + DEFAULT_FIM);

        // Get all the operacaoList where fim is less than or equal to SMALLER_FIM
        defaultOperacaoShouldNotBeFound("fim.lessThanOrEqual=" + SMALLER_FIM);
    }

    @Test
    @Transactional
    void getAllOperacaosByFimIsLessThanSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where fim is less than DEFAULT_FIM
        defaultOperacaoShouldNotBeFound("fim.lessThan=" + DEFAULT_FIM);

        // Get all the operacaoList where fim is less than UPDATED_FIM
        defaultOperacaoShouldBeFound("fim.lessThan=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    void getAllOperacaosByFimIsGreaterThanSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where fim is greater than DEFAULT_FIM
        defaultOperacaoShouldNotBeFound("fim.greaterThan=" + DEFAULT_FIM);

        // Get all the operacaoList where fim is greater than SMALLER_FIM
        defaultOperacaoShouldBeFound("fim.greaterThan=" + SMALLER_FIM);
    }

    @Test
    @Transactional
    void getAllOperacaosByQuantidadeAmostrasIsEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where quantidadeAmostras equals to DEFAULT_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldBeFound("quantidadeAmostras.equals=" + DEFAULT_QUANTIDADE_AMOSTRAS);

        // Get all the operacaoList where quantidadeAmostras equals to UPDATED_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldNotBeFound("quantidadeAmostras.equals=" + UPDATED_QUANTIDADE_AMOSTRAS);
    }

    @Test
    @Transactional
    void getAllOperacaosByQuantidadeAmostrasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where quantidadeAmostras not equals to DEFAULT_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldNotBeFound("quantidadeAmostras.notEquals=" + DEFAULT_QUANTIDADE_AMOSTRAS);

        // Get all the operacaoList where quantidadeAmostras not equals to UPDATED_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldBeFound("quantidadeAmostras.notEquals=" + UPDATED_QUANTIDADE_AMOSTRAS);
    }

    @Test
    @Transactional
    void getAllOperacaosByQuantidadeAmostrasIsInShouldWork() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where quantidadeAmostras in DEFAULT_QUANTIDADE_AMOSTRAS or UPDATED_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldBeFound("quantidadeAmostras.in=" + DEFAULT_QUANTIDADE_AMOSTRAS + "," + UPDATED_QUANTIDADE_AMOSTRAS);

        // Get all the operacaoList where quantidadeAmostras equals to UPDATED_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldNotBeFound("quantidadeAmostras.in=" + UPDATED_QUANTIDADE_AMOSTRAS);
    }

    @Test
    @Transactional
    void getAllOperacaosByQuantidadeAmostrasIsNullOrNotNull() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where quantidadeAmostras is not null
        defaultOperacaoShouldBeFound("quantidadeAmostras.specified=true");

        // Get all the operacaoList where quantidadeAmostras is null
        defaultOperacaoShouldNotBeFound("quantidadeAmostras.specified=false");
    }

    @Test
    @Transactional
    void getAllOperacaosByQuantidadeAmostrasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where quantidadeAmostras is greater than or equal to DEFAULT_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldBeFound("quantidadeAmostras.greaterThanOrEqual=" + DEFAULT_QUANTIDADE_AMOSTRAS);

        // Get all the operacaoList where quantidadeAmostras is greater than or equal to UPDATED_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldNotBeFound("quantidadeAmostras.greaterThanOrEqual=" + UPDATED_QUANTIDADE_AMOSTRAS);
    }

    @Test
    @Transactional
    void getAllOperacaosByQuantidadeAmostrasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where quantidadeAmostras is less than or equal to DEFAULT_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldBeFound("quantidadeAmostras.lessThanOrEqual=" + DEFAULT_QUANTIDADE_AMOSTRAS);

        // Get all the operacaoList where quantidadeAmostras is less than or equal to SMALLER_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldNotBeFound("quantidadeAmostras.lessThanOrEqual=" + SMALLER_QUANTIDADE_AMOSTRAS);
    }

    @Test
    @Transactional
    void getAllOperacaosByQuantidadeAmostrasIsLessThanSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where quantidadeAmostras is less than DEFAULT_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldNotBeFound("quantidadeAmostras.lessThan=" + DEFAULT_QUANTIDADE_AMOSTRAS);

        // Get all the operacaoList where quantidadeAmostras is less than UPDATED_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldBeFound("quantidadeAmostras.lessThan=" + UPDATED_QUANTIDADE_AMOSTRAS);
    }

    @Test
    @Transactional
    void getAllOperacaosByQuantidadeAmostrasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where quantidadeAmostras is greater than DEFAULT_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldNotBeFound("quantidadeAmostras.greaterThan=" + DEFAULT_QUANTIDADE_AMOSTRAS);

        // Get all the operacaoList where quantidadeAmostras is greater than SMALLER_QUANTIDADE_AMOSTRAS
        defaultOperacaoShouldBeFound("quantidadeAmostras.greaterThan=" + SMALLER_QUANTIDADE_AMOSTRAS);
    }

    @Test
    @Transactional
    void getAllOperacaosByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where observacao equals to DEFAULT_OBSERVACAO
        defaultOperacaoShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the operacaoList where observacao equals to UPDATED_OBSERVACAO
        defaultOperacaoShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllOperacaosByObservacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where observacao not equals to DEFAULT_OBSERVACAO
        defaultOperacaoShouldNotBeFound("observacao.notEquals=" + DEFAULT_OBSERVACAO);

        // Get all the operacaoList where observacao not equals to UPDATED_OBSERVACAO
        defaultOperacaoShouldBeFound("observacao.notEquals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllOperacaosByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultOperacaoShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the operacaoList where observacao equals to UPDATED_OBSERVACAO
        defaultOperacaoShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllOperacaosByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where observacao is not null
        defaultOperacaoShouldBeFound("observacao.specified=true");

        // Get all the operacaoList where observacao is null
        defaultOperacaoShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    void getAllOperacaosByObservacaoContainsSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where observacao contains DEFAULT_OBSERVACAO
        defaultOperacaoShouldBeFound("observacao.contains=" + DEFAULT_OBSERVACAO);

        // Get all the operacaoList where observacao contains UPDATED_OBSERVACAO
        defaultOperacaoShouldNotBeFound("observacao.contains=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllOperacaosByObservacaoNotContainsSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        // Get all the operacaoList where observacao does not contain DEFAULT_OBSERVACAO
        defaultOperacaoShouldNotBeFound("observacao.doesNotContain=" + DEFAULT_OBSERVACAO);

        // Get all the operacaoList where observacao does not contain UPDATED_OBSERVACAO
        defaultOperacaoShouldBeFound("observacao.doesNotContain=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void getAllOperacaosByTipoOperacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);
        TipoOperacao tipoOperacao;
        if (TestUtil.findAll(em, TipoOperacao.class).isEmpty()) {
            tipoOperacao = TipoOperacaoResourceIT.createEntity(em);
            em.persist(tipoOperacao);
            em.flush();
        } else {
            tipoOperacao = TestUtil.findAll(em, TipoOperacao.class).get(0);
        }
        em.persist(tipoOperacao);
        em.flush();
        operacao.setTipoOperacao(tipoOperacao);
        operacaoRepository.saveAndFlush(operacao);
        Long tipoOperacaoId = tipoOperacao.getId();

        // Get all the operacaoList where tipoOperacao equals to tipoOperacaoId
        defaultOperacaoShouldBeFound("tipoOperacaoId.equals=" + tipoOperacaoId);

        // Get all the operacaoList where tipoOperacao equals to (tipoOperacaoId + 1)
        defaultOperacaoShouldNotBeFound("tipoOperacaoId.equals=" + (tipoOperacaoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOperacaoShouldBeFound(String filter) throws Exception {
        restOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].volumePeso").value(hasItem(DEFAULT_VOLUME_PESO)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(sameInstant(DEFAULT_INICIO))))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(sameInstant(DEFAULT_FIM))))
            .andExpect(jsonPath("$.[*].quantidadeAmostras").value(hasItem(DEFAULT_QUANTIDADE_AMOSTRAS)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)));

        // Check, that the count call also returns 1
        restOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOperacaoShouldNotBeFound(String filter) throws Exception {
        restOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOperacao() throws Exception {
        // Get the operacao
        restOperacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOperacao() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        int databaseSizeBeforeUpdate = operacaoRepository.findAll().size();

        // Update the operacao
        Operacao updatedOperacao = operacaoRepository.findById(operacao.getId()).get();
        // Disconnect from session so that the updates on updatedOperacao are not directly saved in db
        em.detach(updatedOperacao);
        updatedOperacao
            .descricao(UPDATED_DESCRICAO)
            .volumePeso(UPDATED_VOLUME_PESO)
            .inicio(UPDATED_INICIO)
            .fim(UPDATED_FIM)
            .quantidadeAmostras(UPDATED_QUANTIDADE_AMOSTRAS)
            .observacao(UPDATED_OBSERVACAO);
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(updatedOperacao);

        restOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Operacao in the database
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeUpdate);
        Operacao testOperacao = operacaoList.get(operacaoList.size() - 1);
        assertThat(testOperacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testOperacao.getVolumePeso()).isEqualTo(UPDATED_VOLUME_PESO);
        assertThat(testOperacao.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testOperacao.getFim()).isEqualTo(UPDATED_FIM);
        assertThat(testOperacao.getQuantidadeAmostras()).isEqualTo(UPDATED_QUANTIDADE_AMOSTRAS);
        assertThat(testOperacao.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void putNonExistingOperacao() throws Exception {
        int databaseSizeBeforeUpdate = operacaoRepository.findAll().size();
        operacao.setId(count.incrementAndGet());

        // Create the Operacao
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(operacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operacao in the database
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperacao() throws Exception {
        int databaseSizeBeforeUpdate = operacaoRepository.findAll().size();
        operacao.setId(count.incrementAndGet());

        // Create the Operacao
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(operacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operacao in the database
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperacao() throws Exception {
        int databaseSizeBeforeUpdate = operacaoRepository.findAll().size();
        operacao.setId(count.incrementAndGet());

        // Create the Operacao
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(operacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operacao in the database
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperacaoWithPatch() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        int databaseSizeBeforeUpdate = operacaoRepository.findAll().size();

        // Update the operacao using partial update
        Operacao partialUpdatedOperacao = new Operacao();
        partialUpdatedOperacao.setId(operacao.getId());

        partialUpdatedOperacao.descricao(UPDATED_DESCRICAO).fim(UPDATED_FIM);

        restOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperacao))
            )
            .andExpect(status().isOk());

        // Validate the Operacao in the database
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeUpdate);
        Operacao testOperacao = operacaoList.get(operacaoList.size() - 1);
        assertThat(testOperacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testOperacao.getVolumePeso()).isEqualTo(DEFAULT_VOLUME_PESO);
        assertThat(testOperacao.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testOperacao.getFim()).isEqualTo(UPDATED_FIM);
        assertThat(testOperacao.getQuantidadeAmostras()).isEqualTo(DEFAULT_QUANTIDADE_AMOSTRAS);
        assertThat(testOperacao.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
    }

    @Test
    @Transactional
    void fullUpdateOperacaoWithPatch() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        int databaseSizeBeforeUpdate = operacaoRepository.findAll().size();

        // Update the operacao using partial update
        Operacao partialUpdatedOperacao = new Operacao();
        partialUpdatedOperacao.setId(operacao.getId());

        partialUpdatedOperacao
            .descricao(UPDATED_DESCRICAO)
            .volumePeso(UPDATED_VOLUME_PESO)
            .inicio(UPDATED_INICIO)
            .fim(UPDATED_FIM)
            .quantidadeAmostras(UPDATED_QUANTIDADE_AMOSTRAS)
            .observacao(UPDATED_OBSERVACAO);

        restOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperacao))
            )
            .andExpect(status().isOk());

        // Validate the Operacao in the database
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeUpdate);
        Operacao testOperacao = operacaoList.get(operacaoList.size() - 1);
        assertThat(testOperacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testOperacao.getVolumePeso()).isEqualTo(UPDATED_VOLUME_PESO);
        assertThat(testOperacao.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testOperacao.getFim()).isEqualTo(UPDATED_FIM);
        assertThat(testOperacao.getQuantidadeAmostras()).isEqualTo(UPDATED_QUANTIDADE_AMOSTRAS);
        assertThat(testOperacao.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    void patchNonExistingOperacao() throws Exception {
        int databaseSizeBeforeUpdate = operacaoRepository.findAll().size();
        operacao.setId(count.incrementAndGet());

        // Create the Operacao
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(operacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operacao in the database
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperacao() throws Exception {
        int databaseSizeBeforeUpdate = operacaoRepository.findAll().size();
        operacao.setId(count.incrementAndGet());

        // Create the Operacao
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(operacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operacao in the database
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperacao() throws Exception {
        int databaseSizeBeforeUpdate = operacaoRepository.findAll().size();
        operacao.setId(count.incrementAndGet());

        // Create the Operacao
        OperacaoDTO operacaoDTO = operacaoMapper.toDto(operacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operacao in the database
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperacao() throws Exception {
        // Initialize the database
        operacaoRepository.saveAndFlush(operacao);

        int databaseSizeBeforeDelete = operacaoRepository.findAll().size();

        // Delete the operacao
        restOperacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, operacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operacao> operacaoList = operacaoRepository.findAll();
        assertThat(operacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
