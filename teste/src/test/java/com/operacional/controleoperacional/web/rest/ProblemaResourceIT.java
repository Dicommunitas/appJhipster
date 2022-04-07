package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.Problema;
import com.operacional.controleoperacional.domain.Status;
import com.operacional.controleoperacional.domain.User;
import com.operacional.controleoperacional.domain.enumeration.Criticidade;
import com.operacional.controleoperacional.repository.ProblemaRepository;
import com.operacional.controleoperacional.service.ProblemaService;
import com.operacional.controleoperacional.service.criteria.ProblemaCriteria;
import com.operacional.controleoperacional.service.dto.ProblemaDTO;
import com.operacional.controleoperacional.service.mapper.ProblemaMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProblemaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProblemaResourceIT {

    private static final LocalDate DEFAULT_DATA_VERIFICACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_VERIFICACAO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_VERIFICACAO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Criticidade DEFAULT_CRITICIDADE = Criticidade.BAIXA;
    private static final Criticidade UPDATED_CRITICIDADE = Criticidade.MEDIA;

    private static final String DEFAULT_IMPACTO = "AAAAAAAAAA";
    private static final String UPDATED_IMPACTO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_FINALIZACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_FINALIZACAO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_FINALIZACAO = LocalDate.ofEpochDay(-1L);

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/problemas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProblemaRepository problemaRepository;

    @Mock
    private ProblemaRepository problemaRepositoryMock;

    @Autowired
    private ProblemaMapper problemaMapper;

    @Mock
    private ProblemaService problemaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProblemaMockMvc;

    private Problema problema;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Problema createEntity(EntityManager em) {
        Problema problema = new Problema()
            .dataVerificacao(DEFAULT_DATA_VERIFICACAO)
            .descricao(DEFAULT_DESCRICAO)
            .criticidade(DEFAULT_CRITICIDADE)
            .impacto(DEFAULT_IMPACTO)
            .dataFinalizacao(DEFAULT_DATA_FINALIZACAO)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        problema.setRelator(user);
        return problema;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Problema createUpdatedEntity(EntityManager em) {
        Problema problema = new Problema()
            .dataVerificacao(UPDATED_DATA_VERIFICACAO)
            .descricao(UPDATED_DESCRICAO)
            .criticidade(UPDATED_CRITICIDADE)
            .impacto(UPDATED_IMPACTO)
            .dataFinalizacao(UPDATED_DATA_FINALIZACAO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        problema.setRelator(user);
        return problema;
    }

    @BeforeEach
    public void initTest() {
        problema = createEntity(em);
    }

    @Test
    @Transactional
    void createProblema() throws Exception {
        int databaseSizeBeforeCreate = problemaRepository.findAll().size();
        // Create the Problema
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);
        restProblemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemaDTO)))
            .andExpect(status().isCreated());

        // Validate the Problema in the database
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeCreate + 1);
        Problema testProblema = problemaList.get(problemaList.size() - 1);
        assertThat(testProblema.getDataVerificacao()).isEqualTo(DEFAULT_DATA_VERIFICACAO);
        assertThat(testProblema.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testProblema.getCriticidade()).isEqualTo(DEFAULT_CRITICIDADE);
        assertThat(testProblema.getImpacto()).isEqualTo(DEFAULT_IMPACTO);
        assertThat(testProblema.getDataFinalizacao()).isEqualTo(DEFAULT_DATA_FINALIZACAO);
        assertThat(testProblema.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testProblema.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createProblemaWithExistingId() throws Exception {
        // Create the Problema with an existing ID
        problema.setId(1L);
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);

        int databaseSizeBeforeCreate = problemaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProblemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Problema in the database
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataVerificacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemaRepository.findAll().size();
        // set the field null
        problema.setDataVerificacao(null);

        // Create the Problema, which fails.
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);

        restProblemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemaDTO)))
            .andExpect(status().isBadRequest());

        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemaRepository.findAll().size();
        // set the field null
        problema.setDescricao(null);

        // Create the Problema, which fails.
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);

        restProblemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemaDTO)))
            .andExpect(status().isBadRequest());

        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCriticidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemaRepository.findAll().size();
        // set the field null
        problema.setCriticidade(null);

        // Create the Problema, which fails.
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);

        restProblemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemaDTO)))
            .andExpect(status().isBadRequest());

        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImpactoIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemaRepository.findAll().size();
        // set the field null
        problema.setImpacto(null);

        // Create the Problema, which fails.
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);

        restProblemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemaDTO)))
            .andExpect(status().isBadRequest());

        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProblemas() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList
        restProblemaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(problema.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataVerificacao").value(hasItem(DEFAULT_DATA_VERIFICACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].criticidade").value(hasItem(DEFAULT_CRITICIDADE.toString())))
            .andExpect(jsonPath("$.[*].impacto").value(hasItem(DEFAULT_IMPACTO)))
            .andExpect(jsonPath("$.[*].dataFinalizacao").value(hasItem(DEFAULT_DATA_FINALIZACAO.toString())))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProblemasWithEagerRelationshipsIsEnabled() throws Exception {
        when(problemaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProblemaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(problemaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProblemasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(problemaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProblemaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(problemaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProblema() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get the problema
        restProblemaMockMvc
            .perform(get(ENTITY_API_URL_ID, problema.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(problema.getId().intValue()))
            .andExpect(jsonPath("$.dataVerificacao").value(DEFAULT_DATA_VERIFICACAO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.criticidade").value(DEFAULT_CRITICIDADE.toString()))
            .andExpect(jsonPath("$.impacto").value(DEFAULT_IMPACTO))
            .andExpect(jsonPath("$.dataFinalizacao").value(DEFAULT_DATA_FINALIZACAO.toString()))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)));
    }

    @Test
    @Transactional
    void getProblemasByIdFiltering() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        Long id = problema.getId();

        defaultProblemaShouldBeFound("id.equals=" + id);
        defaultProblemaShouldNotBeFound("id.notEquals=" + id);

        defaultProblemaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProblemaShouldNotBeFound("id.greaterThan=" + id);

        defaultProblemaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProblemaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProblemasByDataVerificacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataVerificacao equals to DEFAULT_DATA_VERIFICACAO
        defaultProblemaShouldBeFound("dataVerificacao.equals=" + DEFAULT_DATA_VERIFICACAO);

        // Get all the problemaList where dataVerificacao equals to UPDATED_DATA_VERIFICACAO
        defaultProblemaShouldNotBeFound("dataVerificacao.equals=" + UPDATED_DATA_VERIFICACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataVerificacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataVerificacao not equals to DEFAULT_DATA_VERIFICACAO
        defaultProblemaShouldNotBeFound("dataVerificacao.notEquals=" + DEFAULT_DATA_VERIFICACAO);

        // Get all the problemaList where dataVerificacao not equals to UPDATED_DATA_VERIFICACAO
        defaultProblemaShouldBeFound("dataVerificacao.notEquals=" + UPDATED_DATA_VERIFICACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataVerificacaoIsInShouldWork() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataVerificacao in DEFAULT_DATA_VERIFICACAO or UPDATED_DATA_VERIFICACAO
        defaultProblemaShouldBeFound("dataVerificacao.in=" + DEFAULT_DATA_VERIFICACAO + "," + UPDATED_DATA_VERIFICACAO);

        // Get all the problemaList where dataVerificacao equals to UPDATED_DATA_VERIFICACAO
        defaultProblemaShouldNotBeFound("dataVerificacao.in=" + UPDATED_DATA_VERIFICACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataVerificacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataVerificacao is not null
        defaultProblemaShouldBeFound("dataVerificacao.specified=true");

        // Get all the problemaList where dataVerificacao is null
        defaultProblemaShouldNotBeFound("dataVerificacao.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemasByDataVerificacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataVerificacao is greater than or equal to DEFAULT_DATA_VERIFICACAO
        defaultProblemaShouldBeFound("dataVerificacao.greaterThanOrEqual=" + DEFAULT_DATA_VERIFICACAO);

        // Get all the problemaList where dataVerificacao is greater than or equal to UPDATED_DATA_VERIFICACAO
        defaultProblemaShouldNotBeFound("dataVerificacao.greaterThanOrEqual=" + UPDATED_DATA_VERIFICACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataVerificacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataVerificacao is less than or equal to DEFAULT_DATA_VERIFICACAO
        defaultProblemaShouldBeFound("dataVerificacao.lessThanOrEqual=" + DEFAULT_DATA_VERIFICACAO);

        // Get all the problemaList where dataVerificacao is less than or equal to SMALLER_DATA_VERIFICACAO
        defaultProblemaShouldNotBeFound("dataVerificacao.lessThanOrEqual=" + SMALLER_DATA_VERIFICACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataVerificacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataVerificacao is less than DEFAULT_DATA_VERIFICACAO
        defaultProblemaShouldNotBeFound("dataVerificacao.lessThan=" + DEFAULT_DATA_VERIFICACAO);

        // Get all the problemaList where dataVerificacao is less than UPDATED_DATA_VERIFICACAO
        defaultProblemaShouldBeFound("dataVerificacao.lessThan=" + UPDATED_DATA_VERIFICACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataVerificacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataVerificacao is greater than DEFAULT_DATA_VERIFICACAO
        defaultProblemaShouldNotBeFound("dataVerificacao.greaterThan=" + DEFAULT_DATA_VERIFICACAO);

        // Get all the problemaList where dataVerificacao is greater than SMALLER_DATA_VERIFICACAO
        defaultProblemaShouldBeFound("dataVerificacao.greaterThan=" + SMALLER_DATA_VERIFICACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where descricao equals to DEFAULT_DESCRICAO
        defaultProblemaShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the problemaList where descricao equals to UPDATED_DESCRICAO
        defaultProblemaShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where descricao not equals to DEFAULT_DESCRICAO
        defaultProblemaShouldNotBeFound("descricao.notEquals=" + DEFAULT_DESCRICAO);

        // Get all the problemaList where descricao not equals to UPDATED_DESCRICAO
        defaultProblemaShouldBeFound("descricao.notEquals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultProblemaShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the problemaList where descricao equals to UPDATED_DESCRICAO
        defaultProblemaShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where descricao is not null
        defaultProblemaShouldBeFound("descricao.specified=true");

        // Get all the problemaList where descricao is null
        defaultProblemaShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemasByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where descricao contains DEFAULT_DESCRICAO
        defaultProblemaShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the problemaList where descricao contains UPDATED_DESCRICAO
        defaultProblemaShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where descricao does not contain DEFAULT_DESCRICAO
        defaultProblemaShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the problemaList where descricao does not contain UPDATED_DESCRICAO
        defaultProblemaShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllProblemasByCriticidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where criticidade equals to DEFAULT_CRITICIDADE
        defaultProblemaShouldBeFound("criticidade.equals=" + DEFAULT_CRITICIDADE);

        // Get all the problemaList where criticidade equals to UPDATED_CRITICIDADE
        defaultProblemaShouldNotBeFound("criticidade.equals=" + UPDATED_CRITICIDADE);
    }

    @Test
    @Transactional
    void getAllProblemasByCriticidadeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where criticidade not equals to DEFAULT_CRITICIDADE
        defaultProblemaShouldNotBeFound("criticidade.notEquals=" + DEFAULT_CRITICIDADE);

        // Get all the problemaList where criticidade not equals to UPDATED_CRITICIDADE
        defaultProblemaShouldBeFound("criticidade.notEquals=" + UPDATED_CRITICIDADE);
    }

    @Test
    @Transactional
    void getAllProblemasByCriticidadeIsInShouldWork() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where criticidade in DEFAULT_CRITICIDADE or UPDATED_CRITICIDADE
        defaultProblemaShouldBeFound("criticidade.in=" + DEFAULT_CRITICIDADE + "," + UPDATED_CRITICIDADE);

        // Get all the problemaList where criticidade equals to UPDATED_CRITICIDADE
        defaultProblemaShouldNotBeFound("criticidade.in=" + UPDATED_CRITICIDADE);
    }

    @Test
    @Transactional
    void getAllProblemasByCriticidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where criticidade is not null
        defaultProblemaShouldBeFound("criticidade.specified=true");

        // Get all the problemaList where criticidade is null
        defaultProblemaShouldNotBeFound("criticidade.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemasByImpactoIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where impacto equals to DEFAULT_IMPACTO
        defaultProblemaShouldBeFound("impacto.equals=" + DEFAULT_IMPACTO);

        // Get all the problemaList where impacto equals to UPDATED_IMPACTO
        defaultProblemaShouldNotBeFound("impacto.equals=" + UPDATED_IMPACTO);
    }

    @Test
    @Transactional
    void getAllProblemasByImpactoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where impacto not equals to DEFAULT_IMPACTO
        defaultProblemaShouldNotBeFound("impacto.notEquals=" + DEFAULT_IMPACTO);

        // Get all the problemaList where impacto not equals to UPDATED_IMPACTO
        defaultProblemaShouldBeFound("impacto.notEquals=" + UPDATED_IMPACTO);
    }

    @Test
    @Transactional
    void getAllProblemasByImpactoIsInShouldWork() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where impacto in DEFAULT_IMPACTO or UPDATED_IMPACTO
        defaultProblemaShouldBeFound("impacto.in=" + DEFAULT_IMPACTO + "," + UPDATED_IMPACTO);

        // Get all the problemaList where impacto equals to UPDATED_IMPACTO
        defaultProblemaShouldNotBeFound("impacto.in=" + UPDATED_IMPACTO);
    }

    @Test
    @Transactional
    void getAllProblemasByImpactoIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where impacto is not null
        defaultProblemaShouldBeFound("impacto.specified=true");

        // Get all the problemaList where impacto is null
        defaultProblemaShouldNotBeFound("impacto.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemasByImpactoContainsSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where impacto contains DEFAULT_IMPACTO
        defaultProblemaShouldBeFound("impacto.contains=" + DEFAULT_IMPACTO);

        // Get all the problemaList where impacto contains UPDATED_IMPACTO
        defaultProblemaShouldNotBeFound("impacto.contains=" + UPDATED_IMPACTO);
    }

    @Test
    @Transactional
    void getAllProblemasByImpactoNotContainsSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where impacto does not contain DEFAULT_IMPACTO
        defaultProblemaShouldNotBeFound("impacto.doesNotContain=" + DEFAULT_IMPACTO);

        // Get all the problemaList where impacto does not contain UPDATED_IMPACTO
        defaultProblemaShouldBeFound("impacto.doesNotContain=" + UPDATED_IMPACTO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataFinalizacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataFinalizacao equals to DEFAULT_DATA_FINALIZACAO
        defaultProblemaShouldBeFound("dataFinalizacao.equals=" + DEFAULT_DATA_FINALIZACAO);

        // Get all the problemaList where dataFinalizacao equals to UPDATED_DATA_FINALIZACAO
        defaultProblemaShouldNotBeFound("dataFinalizacao.equals=" + UPDATED_DATA_FINALIZACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataFinalizacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataFinalizacao not equals to DEFAULT_DATA_FINALIZACAO
        defaultProblemaShouldNotBeFound("dataFinalizacao.notEquals=" + DEFAULT_DATA_FINALIZACAO);

        // Get all the problemaList where dataFinalizacao not equals to UPDATED_DATA_FINALIZACAO
        defaultProblemaShouldBeFound("dataFinalizacao.notEquals=" + UPDATED_DATA_FINALIZACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataFinalizacaoIsInShouldWork() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataFinalizacao in DEFAULT_DATA_FINALIZACAO or UPDATED_DATA_FINALIZACAO
        defaultProblemaShouldBeFound("dataFinalizacao.in=" + DEFAULT_DATA_FINALIZACAO + "," + UPDATED_DATA_FINALIZACAO);

        // Get all the problemaList where dataFinalizacao equals to UPDATED_DATA_FINALIZACAO
        defaultProblemaShouldNotBeFound("dataFinalizacao.in=" + UPDATED_DATA_FINALIZACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataFinalizacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataFinalizacao is not null
        defaultProblemaShouldBeFound("dataFinalizacao.specified=true");

        // Get all the problemaList where dataFinalizacao is null
        defaultProblemaShouldNotBeFound("dataFinalizacao.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemasByDataFinalizacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataFinalizacao is greater than or equal to DEFAULT_DATA_FINALIZACAO
        defaultProblemaShouldBeFound("dataFinalizacao.greaterThanOrEqual=" + DEFAULT_DATA_FINALIZACAO);

        // Get all the problemaList where dataFinalizacao is greater than or equal to UPDATED_DATA_FINALIZACAO
        defaultProblemaShouldNotBeFound("dataFinalizacao.greaterThanOrEqual=" + UPDATED_DATA_FINALIZACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataFinalizacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataFinalizacao is less than or equal to DEFAULT_DATA_FINALIZACAO
        defaultProblemaShouldBeFound("dataFinalizacao.lessThanOrEqual=" + DEFAULT_DATA_FINALIZACAO);

        // Get all the problemaList where dataFinalizacao is less than or equal to SMALLER_DATA_FINALIZACAO
        defaultProblemaShouldNotBeFound("dataFinalizacao.lessThanOrEqual=" + SMALLER_DATA_FINALIZACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataFinalizacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataFinalizacao is less than DEFAULT_DATA_FINALIZACAO
        defaultProblemaShouldNotBeFound("dataFinalizacao.lessThan=" + DEFAULT_DATA_FINALIZACAO);

        // Get all the problemaList where dataFinalizacao is less than UPDATED_DATA_FINALIZACAO
        defaultProblemaShouldBeFound("dataFinalizacao.lessThan=" + UPDATED_DATA_FINALIZACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByDataFinalizacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataFinalizacao is greater than DEFAULT_DATA_FINALIZACAO
        defaultProblemaShouldNotBeFound("dataFinalizacao.greaterThan=" + DEFAULT_DATA_FINALIZACAO);

        // Get all the problemaList where dataFinalizacao is greater than SMALLER_DATA_FINALIZACAO
        defaultProblemaShouldBeFound("dataFinalizacao.greaterThan=" + SMALLER_DATA_FINALIZACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createEntity(em);
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).get(0);
        }
        em.persist(status);
        em.flush();
        problema.addStatus(status);
        problemaRepository.saveAndFlush(problema);
        Long statusId = status.getId();

        // Get all the problemaList where status equals to statusId
        defaultProblemaShouldBeFound("statusId.equals=" + statusId);

        // Get all the problemaList where status equals to (statusId + 1)
        defaultProblemaShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllProblemasByRelatorIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);
        User relator;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            relator = UserResourceIT.createEntity(em);
            em.persist(relator);
            em.flush();
        } else {
            relator = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(relator);
        em.flush();
        problema.setRelator(relator);
        problemaRepository.saveAndFlush(problema);
        Long relatorId = relator.getId();

        // Get all the problemaList where relator equals to relatorId
        defaultProblemaShouldBeFound("relatorId.equals=" + relatorId);

        // Get all the problemaList where relator equals to (relatorId + 1)
        defaultProblemaShouldNotBeFound("relatorId.equals=" + (relatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProblemaShouldBeFound(String filter) throws Exception {
        restProblemaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(problema.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataVerificacao").value(hasItem(DEFAULT_DATA_VERIFICACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].criticidade").value(hasItem(DEFAULT_CRITICIDADE.toString())))
            .andExpect(jsonPath("$.[*].impacto").value(hasItem(DEFAULT_IMPACTO)))
            .andExpect(jsonPath("$.[*].dataFinalizacao").value(hasItem(DEFAULT_DATA_FINALIZACAO.toString())))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))));

        // Check, that the count call also returns 1
        restProblemaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProblemaShouldNotBeFound(String filter) throws Exception {
        restProblemaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProblemaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProblema() throws Exception {
        // Get the problema
        restProblemaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProblema() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        int databaseSizeBeforeUpdate = problemaRepository.findAll().size();

        // Update the problema
        Problema updatedProblema = problemaRepository.findById(problema.getId()).get();
        // Disconnect from session so that the updates on updatedProblema are not directly saved in db
        em.detach(updatedProblema);
        updatedProblema
            .dataVerificacao(UPDATED_DATA_VERIFICACAO)
            .descricao(UPDATED_DESCRICAO)
            .criticidade(UPDATED_CRITICIDADE)
            .impacto(UPDATED_IMPACTO)
            .dataFinalizacao(UPDATED_DATA_FINALIZACAO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        ProblemaDTO problemaDTO = problemaMapper.toDto(updatedProblema);

        restProblemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, problemaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(problemaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Problema in the database
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeUpdate);
        Problema testProblema = problemaList.get(problemaList.size() - 1);
        assertThat(testProblema.getDataVerificacao()).isEqualTo(UPDATED_DATA_VERIFICACAO);
        assertThat(testProblema.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testProblema.getCriticidade()).isEqualTo(UPDATED_CRITICIDADE);
        assertThat(testProblema.getImpacto()).isEqualTo(UPDATED_IMPACTO);
        assertThat(testProblema.getDataFinalizacao()).isEqualTo(UPDATED_DATA_FINALIZACAO);
        assertThat(testProblema.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testProblema.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingProblema() throws Exception {
        int databaseSizeBeforeUpdate = problemaRepository.findAll().size();
        problema.setId(count.incrementAndGet());

        // Create the Problema
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProblemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, problemaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(problemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problema in the database
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProblema() throws Exception {
        int databaseSizeBeforeUpdate = problemaRepository.findAll().size();
        problema.setId(count.incrementAndGet());

        // Create the Problema
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(problemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problema in the database
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProblema() throws Exception {
        int databaseSizeBeforeUpdate = problemaRepository.findAll().size();
        problema.setId(count.incrementAndGet());

        // Create the Problema
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Problema in the database
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProblemaWithPatch() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        int databaseSizeBeforeUpdate = problemaRepository.findAll().size();

        // Update the problema using partial update
        Problema partialUpdatedProblema = new Problema();
        partialUpdatedProblema.setId(problema.getId());

        partialUpdatedProblema.criticidade(UPDATED_CRITICIDADE).impacto(UPDATED_IMPACTO).dataFinalizacao(UPDATED_DATA_FINALIZACAO);

        restProblemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProblema.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProblema))
            )
            .andExpect(status().isOk());

        // Validate the Problema in the database
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeUpdate);
        Problema testProblema = problemaList.get(problemaList.size() - 1);
        assertThat(testProblema.getDataVerificacao()).isEqualTo(DEFAULT_DATA_VERIFICACAO);
        assertThat(testProblema.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testProblema.getCriticidade()).isEqualTo(UPDATED_CRITICIDADE);
        assertThat(testProblema.getImpacto()).isEqualTo(UPDATED_IMPACTO);
        assertThat(testProblema.getDataFinalizacao()).isEqualTo(UPDATED_DATA_FINALIZACAO);
        assertThat(testProblema.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testProblema.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateProblemaWithPatch() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        int databaseSizeBeforeUpdate = problemaRepository.findAll().size();

        // Update the problema using partial update
        Problema partialUpdatedProblema = new Problema();
        partialUpdatedProblema.setId(problema.getId());

        partialUpdatedProblema
            .dataVerificacao(UPDATED_DATA_VERIFICACAO)
            .descricao(UPDATED_DESCRICAO)
            .criticidade(UPDATED_CRITICIDADE)
            .impacto(UPDATED_IMPACTO)
            .dataFinalizacao(UPDATED_DATA_FINALIZACAO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restProblemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProblema.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProblema))
            )
            .andExpect(status().isOk());

        // Validate the Problema in the database
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeUpdate);
        Problema testProblema = problemaList.get(problemaList.size() - 1);
        assertThat(testProblema.getDataVerificacao()).isEqualTo(UPDATED_DATA_VERIFICACAO);
        assertThat(testProblema.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testProblema.getCriticidade()).isEqualTo(UPDATED_CRITICIDADE);
        assertThat(testProblema.getImpacto()).isEqualTo(UPDATED_IMPACTO);
        assertThat(testProblema.getDataFinalizacao()).isEqualTo(UPDATED_DATA_FINALIZACAO);
        assertThat(testProblema.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testProblema.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingProblema() throws Exception {
        int databaseSizeBeforeUpdate = problemaRepository.findAll().size();
        problema.setId(count.incrementAndGet());

        // Create the Problema
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProblemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, problemaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(problemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problema in the database
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProblema() throws Exception {
        int databaseSizeBeforeUpdate = problemaRepository.findAll().size();
        problema.setId(count.incrementAndGet());

        // Create the Problema
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(problemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problema in the database
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProblema() throws Exception {
        int databaseSizeBeforeUpdate = problemaRepository.findAll().size();
        problema.setId(count.incrementAndGet());

        // Create the Problema
        ProblemaDTO problemaDTO = problemaMapper.toDto(problema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(problemaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Problema in the database
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProblema() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        int databaseSizeBeforeDelete = problemaRepository.findAll().size();

        // Delete the problema
        restProblemaMockMvc
            .perform(delete(ENTITY_API_URL_ID, problema.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Problema> problemaList = problemaRepository.findAll();
        assertThat(problemaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
