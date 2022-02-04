package com.operacional.controleoperacional.web.rest;

import static com.operacional.controleoperacional.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.Problema;
import com.operacional.controleoperacional.domain.Status;
import com.operacional.controleoperacional.domain.Usuario;
import com.operacional.controleoperacional.domain.enumeration.Criticidade;
import com.operacional.controleoperacional.repository.ProblemaRepository;
import com.operacional.controleoperacional.service.criteria.ProblemaCriteria;
import com.operacional.controleoperacional.service.dto.ProblemaDTO;
import com.operacional.controleoperacional.service.mapper.ProblemaMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProblemaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProblemaResourceIT {

    private static final ZonedDateTime DEFAULT_DATA_ZONED_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ZONED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_ZONED_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final LocalDate DEFAULT_DATA_LOCAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_LOCAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_LOCAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_DATA_INSTANT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_INSTANT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Duration DEFAULT_DATA_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DATA_DURATION = Duration.ofHours(12);
    private static final Duration SMALLER_DATA_DURATION = Duration.ofHours(5);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Criticidade DEFAULT_CRITICIDADE = Criticidade.BAIXA;
    private static final Criticidade UPDATED_CRITICIDADE = Criticidade.MEDIA;

    private static final Boolean DEFAULT_ACEITAR_FINALIZACAO = false;
    private static final Boolean UPDATED_ACEITAR_FINALIZACAO = true;

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMPACTO = "AAAAAAAAAA";
    private static final String UPDATED_IMPACTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/problemas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProblemaRepository problemaRepository;

    @Autowired
    private ProblemaMapper problemaMapper;

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
            .dataZonedDateTime(DEFAULT_DATA_ZONED_DATE_TIME)
            .dataLocalDate(DEFAULT_DATA_LOCAL_DATE)
            .dataInstant(DEFAULT_DATA_INSTANT)
            .dataDuration(DEFAULT_DATA_DURATION)
            .descricao(DEFAULT_DESCRICAO)
            .criticidade(DEFAULT_CRITICIDADE)
            .aceitarFinalizacao(DEFAULT_ACEITAR_FINALIZACAO)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE)
            .impacto(DEFAULT_IMPACTO);
        // Add required entity
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createEntity(em);
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        problema.setRelator(usuario);
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
            .dataZonedDateTime(UPDATED_DATA_ZONED_DATE_TIME)
            .dataLocalDate(UPDATED_DATA_LOCAL_DATE)
            .dataInstant(UPDATED_DATA_INSTANT)
            .dataDuration(UPDATED_DATA_DURATION)
            .descricao(UPDATED_DESCRICAO)
            .criticidade(UPDATED_CRITICIDADE)
            .aceitarFinalizacao(UPDATED_ACEITAR_FINALIZACAO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .impacto(UPDATED_IMPACTO);
        // Add required entity
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createUpdatedEntity(em);
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        problema.setRelator(usuario);
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
        assertThat(testProblema.getDataZonedDateTime()).isEqualTo(DEFAULT_DATA_ZONED_DATE_TIME);
        assertThat(testProblema.getDataLocalDate()).isEqualTo(DEFAULT_DATA_LOCAL_DATE);
        assertThat(testProblema.getDataInstant()).isEqualTo(DEFAULT_DATA_INSTANT);
        assertThat(testProblema.getDataDuration()).isEqualTo(DEFAULT_DATA_DURATION);
        assertThat(testProblema.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testProblema.getCriticidade()).isEqualTo(DEFAULT_CRITICIDADE);
        assertThat(testProblema.getAceitarFinalizacao()).isEqualTo(DEFAULT_ACEITAR_FINALIZACAO);
        assertThat(testProblema.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testProblema.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
        assertThat(testProblema.getImpacto()).isEqualTo(DEFAULT_IMPACTO);
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
    void checkDataZonedDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemaRepository.findAll().size();
        // set the field null
        problema.setDataZonedDateTime(null);

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
    void checkDataLocalDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemaRepository.findAll().size();
        // set the field null
        problema.setDataLocalDate(null);

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
    void checkDataInstantIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemaRepository.findAll().size();
        // set the field null
        problema.setDataInstant(null);

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
    void checkDataDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemaRepository.findAll().size();
        // set the field null
        problema.setDataDuration(null);

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
            .andExpect(jsonPath("$.[*].dataZonedDateTime").value(hasItem(sameInstant(DEFAULT_DATA_ZONED_DATE_TIME))))
            .andExpect(jsonPath("$.[*].dataLocalDate").value(hasItem(DEFAULT_DATA_LOCAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].dataInstant").value(hasItem(DEFAULT_DATA_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].dataDuration").value(hasItem(DEFAULT_DATA_DURATION.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].criticidade").value(hasItem(DEFAULT_CRITICIDADE.toString())))
            .andExpect(jsonPath("$.[*].aceitarFinalizacao").value(hasItem(DEFAULT_ACEITAR_FINALIZACAO.booleanValue())))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].impacto").value(hasItem(DEFAULT_IMPACTO)));
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
            .andExpect(jsonPath("$.dataZonedDateTime").value(sameInstant(DEFAULT_DATA_ZONED_DATE_TIME)))
            .andExpect(jsonPath("$.dataLocalDate").value(DEFAULT_DATA_LOCAL_DATE.toString()))
            .andExpect(jsonPath("$.dataInstant").value(DEFAULT_DATA_INSTANT.toString()))
            .andExpect(jsonPath("$.dataDuration").value(DEFAULT_DATA_DURATION.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.criticidade").value(DEFAULT_CRITICIDADE.toString()))
            .andExpect(jsonPath("$.aceitarFinalizacao").value(DEFAULT_ACEITAR_FINALIZACAO.booleanValue()))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)))
            .andExpect(jsonPath("$.impacto").value(DEFAULT_IMPACTO));
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
    void getAllProblemasByDataZonedDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataZonedDateTime equals to DEFAULT_DATA_ZONED_DATE_TIME
        defaultProblemaShouldBeFound("dataZonedDateTime.equals=" + DEFAULT_DATA_ZONED_DATE_TIME);

        // Get all the problemaList where dataZonedDateTime equals to UPDATED_DATA_ZONED_DATE_TIME
        defaultProblemaShouldNotBeFound("dataZonedDateTime.equals=" + UPDATED_DATA_ZONED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllProblemasByDataZonedDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataZonedDateTime not equals to DEFAULT_DATA_ZONED_DATE_TIME
        defaultProblemaShouldNotBeFound("dataZonedDateTime.notEquals=" + DEFAULT_DATA_ZONED_DATE_TIME);

        // Get all the problemaList where dataZonedDateTime not equals to UPDATED_DATA_ZONED_DATE_TIME
        defaultProblemaShouldBeFound("dataZonedDateTime.notEquals=" + UPDATED_DATA_ZONED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllProblemasByDataZonedDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataZonedDateTime in DEFAULT_DATA_ZONED_DATE_TIME or UPDATED_DATA_ZONED_DATE_TIME
        defaultProblemaShouldBeFound("dataZonedDateTime.in=" + DEFAULT_DATA_ZONED_DATE_TIME + "," + UPDATED_DATA_ZONED_DATE_TIME);

        // Get all the problemaList where dataZonedDateTime equals to UPDATED_DATA_ZONED_DATE_TIME
        defaultProblemaShouldNotBeFound("dataZonedDateTime.in=" + UPDATED_DATA_ZONED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllProblemasByDataZonedDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataZonedDateTime is not null
        defaultProblemaShouldBeFound("dataZonedDateTime.specified=true");

        // Get all the problemaList where dataZonedDateTime is null
        defaultProblemaShouldNotBeFound("dataZonedDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemasByDataZonedDateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataZonedDateTime is greater than or equal to DEFAULT_DATA_ZONED_DATE_TIME
        defaultProblemaShouldBeFound("dataZonedDateTime.greaterThanOrEqual=" + DEFAULT_DATA_ZONED_DATE_TIME);

        // Get all the problemaList where dataZonedDateTime is greater than or equal to UPDATED_DATA_ZONED_DATE_TIME
        defaultProblemaShouldNotBeFound("dataZonedDateTime.greaterThanOrEqual=" + UPDATED_DATA_ZONED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllProblemasByDataZonedDateTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataZonedDateTime is less than or equal to DEFAULT_DATA_ZONED_DATE_TIME
        defaultProblemaShouldBeFound("dataZonedDateTime.lessThanOrEqual=" + DEFAULT_DATA_ZONED_DATE_TIME);

        // Get all the problemaList where dataZonedDateTime is less than or equal to SMALLER_DATA_ZONED_DATE_TIME
        defaultProblemaShouldNotBeFound("dataZonedDateTime.lessThanOrEqual=" + SMALLER_DATA_ZONED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllProblemasByDataZonedDateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataZonedDateTime is less than DEFAULT_DATA_ZONED_DATE_TIME
        defaultProblemaShouldNotBeFound("dataZonedDateTime.lessThan=" + DEFAULT_DATA_ZONED_DATE_TIME);

        // Get all the problemaList where dataZonedDateTime is less than UPDATED_DATA_ZONED_DATE_TIME
        defaultProblemaShouldBeFound("dataZonedDateTime.lessThan=" + UPDATED_DATA_ZONED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllProblemasByDataZonedDateTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataZonedDateTime is greater than DEFAULT_DATA_ZONED_DATE_TIME
        defaultProblemaShouldNotBeFound("dataZonedDateTime.greaterThan=" + DEFAULT_DATA_ZONED_DATE_TIME);

        // Get all the problemaList where dataZonedDateTime is greater than SMALLER_DATA_ZONED_DATE_TIME
        defaultProblemaShouldBeFound("dataZonedDateTime.greaterThan=" + SMALLER_DATA_ZONED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllProblemasByDataLocalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataLocalDate equals to DEFAULT_DATA_LOCAL_DATE
        defaultProblemaShouldBeFound("dataLocalDate.equals=" + DEFAULT_DATA_LOCAL_DATE);

        // Get all the problemaList where dataLocalDate equals to UPDATED_DATA_LOCAL_DATE
        defaultProblemaShouldNotBeFound("dataLocalDate.equals=" + UPDATED_DATA_LOCAL_DATE);
    }

    @Test
    @Transactional
    void getAllProblemasByDataLocalDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataLocalDate not equals to DEFAULT_DATA_LOCAL_DATE
        defaultProblemaShouldNotBeFound("dataLocalDate.notEquals=" + DEFAULT_DATA_LOCAL_DATE);

        // Get all the problemaList where dataLocalDate not equals to UPDATED_DATA_LOCAL_DATE
        defaultProblemaShouldBeFound("dataLocalDate.notEquals=" + UPDATED_DATA_LOCAL_DATE);
    }

    @Test
    @Transactional
    void getAllProblemasByDataLocalDateIsInShouldWork() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataLocalDate in DEFAULT_DATA_LOCAL_DATE or UPDATED_DATA_LOCAL_DATE
        defaultProblemaShouldBeFound("dataLocalDate.in=" + DEFAULT_DATA_LOCAL_DATE + "," + UPDATED_DATA_LOCAL_DATE);

        // Get all the problemaList where dataLocalDate equals to UPDATED_DATA_LOCAL_DATE
        defaultProblemaShouldNotBeFound("dataLocalDate.in=" + UPDATED_DATA_LOCAL_DATE);
    }

    @Test
    @Transactional
    void getAllProblemasByDataLocalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataLocalDate is not null
        defaultProblemaShouldBeFound("dataLocalDate.specified=true");

        // Get all the problemaList where dataLocalDate is null
        defaultProblemaShouldNotBeFound("dataLocalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemasByDataLocalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataLocalDate is greater than or equal to DEFAULT_DATA_LOCAL_DATE
        defaultProblemaShouldBeFound("dataLocalDate.greaterThanOrEqual=" + DEFAULT_DATA_LOCAL_DATE);

        // Get all the problemaList where dataLocalDate is greater than or equal to UPDATED_DATA_LOCAL_DATE
        defaultProblemaShouldNotBeFound("dataLocalDate.greaterThanOrEqual=" + UPDATED_DATA_LOCAL_DATE);
    }

    @Test
    @Transactional
    void getAllProblemasByDataLocalDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataLocalDate is less than or equal to DEFAULT_DATA_LOCAL_DATE
        defaultProblemaShouldBeFound("dataLocalDate.lessThanOrEqual=" + DEFAULT_DATA_LOCAL_DATE);

        // Get all the problemaList where dataLocalDate is less than or equal to SMALLER_DATA_LOCAL_DATE
        defaultProblemaShouldNotBeFound("dataLocalDate.lessThanOrEqual=" + SMALLER_DATA_LOCAL_DATE);
    }

    @Test
    @Transactional
    void getAllProblemasByDataLocalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataLocalDate is less than DEFAULT_DATA_LOCAL_DATE
        defaultProblemaShouldNotBeFound("dataLocalDate.lessThan=" + DEFAULT_DATA_LOCAL_DATE);

        // Get all the problemaList where dataLocalDate is less than UPDATED_DATA_LOCAL_DATE
        defaultProblemaShouldBeFound("dataLocalDate.lessThan=" + UPDATED_DATA_LOCAL_DATE);
    }

    @Test
    @Transactional
    void getAllProblemasByDataLocalDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataLocalDate is greater than DEFAULT_DATA_LOCAL_DATE
        defaultProblemaShouldNotBeFound("dataLocalDate.greaterThan=" + DEFAULT_DATA_LOCAL_DATE);

        // Get all the problemaList where dataLocalDate is greater than SMALLER_DATA_LOCAL_DATE
        defaultProblemaShouldBeFound("dataLocalDate.greaterThan=" + SMALLER_DATA_LOCAL_DATE);
    }

    @Test
    @Transactional
    void getAllProblemasByDataInstantIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataInstant equals to DEFAULT_DATA_INSTANT
        defaultProblemaShouldBeFound("dataInstant.equals=" + DEFAULT_DATA_INSTANT);

        // Get all the problemaList where dataInstant equals to UPDATED_DATA_INSTANT
        defaultProblemaShouldNotBeFound("dataInstant.equals=" + UPDATED_DATA_INSTANT);
    }

    @Test
    @Transactional
    void getAllProblemasByDataInstantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataInstant not equals to DEFAULT_DATA_INSTANT
        defaultProblemaShouldNotBeFound("dataInstant.notEquals=" + DEFAULT_DATA_INSTANT);

        // Get all the problemaList where dataInstant not equals to UPDATED_DATA_INSTANT
        defaultProblemaShouldBeFound("dataInstant.notEquals=" + UPDATED_DATA_INSTANT);
    }

    @Test
    @Transactional
    void getAllProblemasByDataInstantIsInShouldWork() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataInstant in DEFAULT_DATA_INSTANT or UPDATED_DATA_INSTANT
        defaultProblemaShouldBeFound("dataInstant.in=" + DEFAULT_DATA_INSTANT + "," + UPDATED_DATA_INSTANT);

        // Get all the problemaList where dataInstant equals to UPDATED_DATA_INSTANT
        defaultProblemaShouldNotBeFound("dataInstant.in=" + UPDATED_DATA_INSTANT);
    }

    @Test
    @Transactional
    void getAllProblemasByDataInstantIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataInstant is not null
        defaultProblemaShouldBeFound("dataInstant.specified=true");

        // Get all the problemaList where dataInstant is null
        defaultProblemaShouldNotBeFound("dataInstant.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemasByDataDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataDuration equals to DEFAULT_DATA_DURATION
        defaultProblemaShouldBeFound("dataDuration.equals=" + DEFAULT_DATA_DURATION);

        // Get all the problemaList where dataDuration equals to UPDATED_DATA_DURATION
        defaultProblemaShouldNotBeFound("dataDuration.equals=" + UPDATED_DATA_DURATION);
    }

    @Test
    @Transactional
    void getAllProblemasByDataDurationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataDuration not equals to DEFAULT_DATA_DURATION
        defaultProblemaShouldNotBeFound("dataDuration.notEquals=" + DEFAULT_DATA_DURATION);

        // Get all the problemaList where dataDuration not equals to UPDATED_DATA_DURATION
        defaultProblemaShouldBeFound("dataDuration.notEquals=" + UPDATED_DATA_DURATION);
    }

    @Test
    @Transactional
    void getAllProblemasByDataDurationIsInShouldWork() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataDuration in DEFAULT_DATA_DURATION or UPDATED_DATA_DURATION
        defaultProblemaShouldBeFound("dataDuration.in=" + DEFAULT_DATA_DURATION + "," + UPDATED_DATA_DURATION);

        // Get all the problemaList where dataDuration equals to UPDATED_DATA_DURATION
        defaultProblemaShouldNotBeFound("dataDuration.in=" + UPDATED_DATA_DURATION);
    }

    @Test
    @Transactional
    void getAllProblemasByDataDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataDuration is not null
        defaultProblemaShouldBeFound("dataDuration.specified=true");

        // Get all the problemaList where dataDuration is null
        defaultProblemaShouldNotBeFound("dataDuration.specified=false");
    }

    @Test
    @Transactional
    void getAllProblemasByDataDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataDuration is greater than or equal to DEFAULT_DATA_DURATION
        defaultProblemaShouldBeFound("dataDuration.greaterThanOrEqual=" + DEFAULT_DATA_DURATION);

        // Get all the problemaList where dataDuration is greater than or equal to UPDATED_DATA_DURATION
        defaultProblemaShouldNotBeFound("dataDuration.greaterThanOrEqual=" + UPDATED_DATA_DURATION);
    }

    @Test
    @Transactional
    void getAllProblemasByDataDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataDuration is less than or equal to DEFAULT_DATA_DURATION
        defaultProblemaShouldBeFound("dataDuration.lessThanOrEqual=" + DEFAULT_DATA_DURATION);

        // Get all the problemaList where dataDuration is less than or equal to SMALLER_DATA_DURATION
        defaultProblemaShouldNotBeFound("dataDuration.lessThanOrEqual=" + SMALLER_DATA_DURATION);
    }

    @Test
    @Transactional
    void getAllProblemasByDataDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataDuration is less than DEFAULT_DATA_DURATION
        defaultProblemaShouldNotBeFound("dataDuration.lessThan=" + DEFAULT_DATA_DURATION);

        // Get all the problemaList where dataDuration is less than UPDATED_DATA_DURATION
        defaultProblemaShouldBeFound("dataDuration.lessThan=" + UPDATED_DATA_DURATION);
    }

    @Test
    @Transactional
    void getAllProblemasByDataDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where dataDuration is greater than DEFAULT_DATA_DURATION
        defaultProblemaShouldNotBeFound("dataDuration.greaterThan=" + DEFAULT_DATA_DURATION);

        // Get all the problemaList where dataDuration is greater than SMALLER_DATA_DURATION
        defaultProblemaShouldBeFound("dataDuration.greaterThan=" + SMALLER_DATA_DURATION);
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
    void getAllProblemasByAceitarFinalizacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where aceitarFinalizacao equals to DEFAULT_ACEITAR_FINALIZACAO
        defaultProblemaShouldBeFound("aceitarFinalizacao.equals=" + DEFAULT_ACEITAR_FINALIZACAO);

        // Get all the problemaList where aceitarFinalizacao equals to UPDATED_ACEITAR_FINALIZACAO
        defaultProblemaShouldNotBeFound("aceitarFinalizacao.equals=" + UPDATED_ACEITAR_FINALIZACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByAceitarFinalizacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where aceitarFinalizacao not equals to DEFAULT_ACEITAR_FINALIZACAO
        defaultProblemaShouldNotBeFound("aceitarFinalizacao.notEquals=" + DEFAULT_ACEITAR_FINALIZACAO);

        // Get all the problemaList where aceitarFinalizacao not equals to UPDATED_ACEITAR_FINALIZACAO
        defaultProblemaShouldBeFound("aceitarFinalizacao.notEquals=" + UPDATED_ACEITAR_FINALIZACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByAceitarFinalizacaoIsInShouldWork() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where aceitarFinalizacao in DEFAULT_ACEITAR_FINALIZACAO or UPDATED_ACEITAR_FINALIZACAO
        defaultProblemaShouldBeFound("aceitarFinalizacao.in=" + DEFAULT_ACEITAR_FINALIZACAO + "," + UPDATED_ACEITAR_FINALIZACAO);

        // Get all the problemaList where aceitarFinalizacao equals to UPDATED_ACEITAR_FINALIZACAO
        defaultProblemaShouldNotBeFound("aceitarFinalizacao.in=" + UPDATED_ACEITAR_FINALIZACAO);
    }

    @Test
    @Transactional
    void getAllProblemasByAceitarFinalizacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemaRepository.saveAndFlush(problema);

        // Get all the problemaList where aceitarFinalizacao is not null
        defaultProblemaShouldBeFound("aceitarFinalizacao.specified=true");

        // Get all the problemaList where aceitarFinalizacao is null
        defaultProblemaShouldNotBeFound("aceitarFinalizacao.specified=false");
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
        Usuario relator;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            relator = UsuarioResourceIT.createEntity(em);
            em.persist(relator);
            em.flush();
        } else {
            relator = TestUtil.findAll(em, Usuario.class).get(0);
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
            .andExpect(jsonPath("$.[*].dataZonedDateTime").value(hasItem(sameInstant(DEFAULT_DATA_ZONED_DATE_TIME))))
            .andExpect(jsonPath("$.[*].dataLocalDate").value(hasItem(DEFAULT_DATA_LOCAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].dataInstant").value(hasItem(DEFAULT_DATA_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].dataDuration").value(hasItem(DEFAULT_DATA_DURATION.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].criticidade").value(hasItem(DEFAULT_CRITICIDADE.toString())))
            .andExpect(jsonPath("$.[*].aceitarFinalizacao").value(hasItem(DEFAULT_ACEITAR_FINALIZACAO.booleanValue())))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].impacto").value(hasItem(DEFAULT_IMPACTO)));

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
            .dataZonedDateTime(UPDATED_DATA_ZONED_DATE_TIME)
            .dataLocalDate(UPDATED_DATA_LOCAL_DATE)
            .dataInstant(UPDATED_DATA_INSTANT)
            .dataDuration(UPDATED_DATA_DURATION)
            .descricao(UPDATED_DESCRICAO)
            .criticidade(UPDATED_CRITICIDADE)
            .aceitarFinalizacao(UPDATED_ACEITAR_FINALIZACAO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .impacto(UPDATED_IMPACTO);
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
        assertThat(testProblema.getDataZonedDateTime()).isEqualTo(UPDATED_DATA_ZONED_DATE_TIME);
        assertThat(testProblema.getDataLocalDate()).isEqualTo(UPDATED_DATA_LOCAL_DATE);
        assertThat(testProblema.getDataInstant()).isEqualTo(UPDATED_DATA_INSTANT);
        assertThat(testProblema.getDataDuration()).isEqualTo(UPDATED_DATA_DURATION);
        assertThat(testProblema.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testProblema.getCriticidade()).isEqualTo(UPDATED_CRITICIDADE);
        assertThat(testProblema.getAceitarFinalizacao()).isEqualTo(UPDATED_ACEITAR_FINALIZACAO);
        assertThat(testProblema.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testProblema.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testProblema.getImpacto()).isEqualTo(UPDATED_IMPACTO);
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

        partialUpdatedProblema
            .dataInstant(UPDATED_DATA_INSTANT)
            .dataDuration(UPDATED_DATA_DURATION)
            .descricao(UPDATED_DESCRICAO)
            .aceitarFinalizacao(UPDATED_ACEITAR_FINALIZACAO)
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
        assertThat(testProblema.getDataZonedDateTime()).isEqualTo(DEFAULT_DATA_ZONED_DATE_TIME);
        assertThat(testProblema.getDataLocalDate()).isEqualTo(DEFAULT_DATA_LOCAL_DATE);
        assertThat(testProblema.getDataInstant()).isEqualTo(UPDATED_DATA_INSTANT);
        assertThat(testProblema.getDataDuration()).isEqualTo(UPDATED_DATA_DURATION);
        assertThat(testProblema.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testProblema.getCriticidade()).isEqualTo(DEFAULT_CRITICIDADE);
        assertThat(testProblema.getAceitarFinalizacao()).isEqualTo(UPDATED_ACEITAR_FINALIZACAO);
        assertThat(testProblema.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testProblema.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testProblema.getImpacto()).isEqualTo(DEFAULT_IMPACTO);
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
            .dataZonedDateTime(UPDATED_DATA_ZONED_DATE_TIME)
            .dataLocalDate(UPDATED_DATA_LOCAL_DATE)
            .dataInstant(UPDATED_DATA_INSTANT)
            .dataDuration(UPDATED_DATA_DURATION)
            .descricao(UPDATED_DESCRICAO)
            .criticidade(UPDATED_CRITICIDADE)
            .aceitarFinalizacao(UPDATED_ACEITAR_FINALIZACAO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .impacto(UPDATED_IMPACTO);

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
        assertThat(testProblema.getDataZonedDateTime()).isEqualTo(UPDATED_DATA_ZONED_DATE_TIME);
        assertThat(testProblema.getDataLocalDate()).isEqualTo(UPDATED_DATA_LOCAL_DATE);
        assertThat(testProblema.getDataInstant()).isEqualTo(UPDATED_DATA_INSTANT);
        assertThat(testProblema.getDataDuration()).isEqualTo(UPDATED_DATA_DURATION);
        assertThat(testProblema.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testProblema.getCriticidade()).isEqualTo(UPDATED_CRITICIDADE);
        assertThat(testProblema.getAceitarFinalizacao()).isEqualTo(UPDATED_ACEITAR_FINALIZACAO);
        assertThat(testProblema.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testProblema.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testProblema.getImpacto()).isEqualTo(UPDATED_IMPACTO);
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
