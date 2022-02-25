package com.operacional.controleoperacional.web.rest;

import static com.operacional.controleoperacional.web.rest.TestUtil.sameInstant;
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
import com.operacional.controleoperacional.domain.Usuario;
import com.operacional.controleoperacional.repository.AmostraRepository;
import com.operacional.controleoperacional.service.criteria.AmostraCriteria;
import com.operacional.controleoperacional.service.dto.AmostraDTO;
import com.operacional.controleoperacional.service.mapper.AmostraMapper;
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
 * Integration tests for the {@link AmostraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AmostraResourceIT {

    private static final ZonedDateTime DEFAULT_DATA_HORA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_HORA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_HORA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICADOR_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR_EXTERNO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AMOSTRA_NO_LABORATORIO = false;
    private static final Boolean UPDATED_AMOSTRA_NO_LABORATORIO = true;

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
            .dataHora(DEFAULT_DATA_HORA)
            .observacao(DEFAULT_OBSERVACAO)
            .identificadorExterno(DEFAULT_IDENTIFICADOR_EXTERNO)
            .amostraNoLaboratorio(DEFAULT_AMOSTRA_NO_LABORATORIO);
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
            .dataHora(UPDATED_DATA_HORA)
            .observacao(UPDATED_OBSERVACAO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .amostraNoLaboratorio(UPDATED_AMOSTRA_NO_LABORATORIO);
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
        assertThat(testAmostra.getDataHora()).isEqualTo(DEFAULT_DATA_HORA);
        assertThat(testAmostra.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testAmostra.getIdentificadorExterno()).isEqualTo(DEFAULT_IDENTIFICADOR_EXTERNO);
        assertThat(testAmostra.getAmostraNoLaboratorio()).isEqualTo(DEFAULT_AMOSTRA_NO_LABORATORIO);
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
    void getAllAmostras() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList
        restAmostraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amostra.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHora").value(hasItem(sameInstant(DEFAULT_DATA_HORA))))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].identificadorExterno").value(hasItem(DEFAULT_IDENTIFICADOR_EXTERNO)))
            .andExpect(jsonPath("$.[*].amostraNoLaboratorio").value(hasItem(DEFAULT_AMOSTRA_NO_LABORATORIO.booleanValue())));
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
            .andExpect(jsonPath("$.dataHora").value(sameInstant(DEFAULT_DATA_HORA)))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.identificadorExterno").value(DEFAULT_IDENTIFICADOR_EXTERNO))
            .andExpect(jsonPath("$.amostraNoLaboratorio").value(DEFAULT_AMOSTRA_NO_LABORATORIO.booleanValue()));
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
    void getAllAmostrasByDataHoraIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHora equals to DEFAULT_DATA_HORA
        defaultAmostraShouldBeFound("dataHora.equals=" + DEFAULT_DATA_HORA);

        // Get all the amostraList where dataHora equals to UPDATED_DATA_HORA
        defaultAmostraShouldNotBeFound("dataHora.equals=" + UPDATED_DATA_HORA);
    }

    @Test
    @Transactional
    void getAllAmostrasByDataHoraIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHora not equals to DEFAULT_DATA_HORA
        defaultAmostraShouldNotBeFound("dataHora.notEquals=" + DEFAULT_DATA_HORA);

        // Get all the amostraList where dataHora not equals to UPDATED_DATA_HORA
        defaultAmostraShouldBeFound("dataHora.notEquals=" + UPDATED_DATA_HORA);
    }

    @Test
    @Transactional
    void getAllAmostrasByDataHoraIsInShouldWork() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHora in DEFAULT_DATA_HORA or UPDATED_DATA_HORA
        defaultAmostraShouldBeFound("dataHora.in=" + DEFAULT_DATA_HORA + "," + UPDATED_DATA_HORA);

        // Get all the amostraList where dataHora equals to UPDATED_DATA_HORA
        defaultAmostraShouldNotBeFound("dataHora.in=" + UPDATED_DATA_HORA);
    }

    @Test
    @Transactional
    void getAllAmostrasByDataHoraIsNullOrNotNull() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHora is not null
        defaultAmostraShouldBeFound("dataHora.specified=true");

        // Get all the amostraList where dataHora is null
        defaultAmostraShouldNotBeFound("dataHora.specified=false");
    }

    @Test
    @Transactional
    void getAllAmostrasByDataHoraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHora is greater than or equal to DEFAULT_DATA_HORA
        defaultAmostraShouldBeFound("dataHora.greaterThanOrEqual=" + DEFAULT_DATA_HORA);

        // Get all the amostraList where dataHora is greater than or equal to UPDATED_DATA_HORA
        defaultAmostraShouldNotBeFound("dataHora.greaterThanOrEqual=" + UPDATED_DATA_HORA);
    }

    @Test
    @Transactional
    void getAllAmostrasByDataHoraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHora is less than or equal to DEFAULT_DATA_HORA
        defaultAmostraShouldBeFound("dataHora.lessThanOrEqual=" + DEFAULT_DATA_HORA);

        // Get all the amostraList where dataHora is less than or equal to SMALLER_DATA_HORA
        defaultAmostraShouldNotBeFound("dataHora.lessThanOrEqual=" + SMALLER_DATA_HORA);
    }

    @Test
    @Transactional
    void getAllAmostrasByDataHoraIsLessThanSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHora is less than DEFAULT_DATA_HORA
        defaultAmostraShouldNotBeFound("dataHora.lessThan=" + DEFAULT_DATA_HORA);

        // Get all the amostraList where dataHora is less than UPDATED_DATA_HORA
        defaultAmostraShouldBeFound("dataHora.lessThan=" + UPDATED_DATA_HORA);
    }

    @Test
    @Transactional
    void getAllAmostrasByDataHoraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where dataHora is greater than DEFAULT_DATA_HORA
        defaultAmostraShouldNotBeFound("dataHora.greaterThan=" + DEFAULT_DATA_HORA);

        // Get all the amostraList where dataHora is greater than SMALLER_DATA_HORA
        defaultAmostraShouldBeFound("dataHora.greaterThan=" + SMALLER_DATA_HORA);
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
    void getAllAmostrasByAmostraNoLaboratorioIsEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where amostraNoLaboratorio equals to DEFAULT_AMOSTRA_NO_LABORATORIO
        defaultAmostraShouldBeFound("amostraNoLaboratorio.equals=" + DEFAULT_AMOSTRA_NO_LABORATORIO);

        // Get all the amostraList where amostraNoLaboratorio equals to UPDATED_AMOSTRA_NO_LABORATORIO
        defaultAmostraShouldNotBeFound("amostraNoLaboratorio.equals=" + UPDATED_AMOSTRA_NO_LABORATORIO);
    }

    @Test
    @Transactional
    void getAllAmostrasByAmostraNoLaboratorioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where amostraNoLaboratorio not equals to DEFAULT_AMOSTRA_NO_LABORATORIO
        defaultAmostraShouldNotBeFound("amostraNoLaboratorio.notEquals=" + DEFAULT_AMOSTRA_NO_LABORATORIO);

        // Get all the amostraList where amostraNoLaboratorio not equals to UPDATED_AMOSTRA_NO_LABORATORIO
        defaultAmostraShouldBeFound("amostraNoLaboratorio.notEquals=" + UPDATED_AMOSTRA_NO_LABORATORIO);
    }

    @Test
    @Transactional
    void getAllAmostrasByAmostraNoLaboratorioIsInShouldWork() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where amostraNoLaboratorio in DEFAULT_AMOSTRA_NO_LABORATORIO or UPDATED_AMOSTRA_NO_LABORATORIO
        defaultAmostraShouldBeFound("amostraNoLaboratorio.in=" + DEFAULT_AMOSTRA_NO_LABORATORIO + "," + UPDATED_AMOSTRA_NO_LABORATORIO);

        // Get all the amostraList where amostraNoLaboratorio equals to UPDATED_AMOSTRA_NO_LABORATORIO
        defaultAmostraShouldNotBeFound("amostraNoLaboratorio.in=" + UPDATED_AMOSTRA_NO_LABORATORIO);
    }

    @Test
    @Transactional
    void getAllAmostrasByAmostraNoLaboratorioIsNullOrNotNull() throws Exception {
        // Initialize the database
        amostraRepository.saveAndFlush(amostra);

        // Get all the amostraList where amostraNoLaboratorio is not null
        defaultAmostraShouldBeFound("amostraNoLaboratorio.specified=true");

        // Get all the amostraList where amostraNoLaboratorio is null
        defaultAmostraShouldNotBeFound("amostraNoLaboratorio.specified=false");
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
        Usuario amostradaPor;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            amostradaPor = UsuarioResourceIT.createEntity(em);
            em.persist(amostradaPor);
            em.flush();
        } else {
            amostradaPor = TestUtil.findAll(em, Usuario.class).get(0);
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
        Usuario recebidaPor;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            recebidaPor = UsuarioResourceIT.createEntity(em);
            em.persist(recebidaPor);
            em.flush();
        } else {
            recebidaPor = TestUtil.findAll(em, Usuario.class).get(0);
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
            .andExpect(jsonPath("$.[*].dataHora").value(hasItem(sameInstant(DEFAULT_DATA_HORA))))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].identificadorExterno").value(hasItem(DEFAULT_IDENTIFICADOR_EXTERNO)))
            .andExpect(jsonPath("$.[*].amostraNoLaboratorio").value(hasItem(DEFAULT_AMOSTRA_NO_LABORATORIO.booleanValue())));

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
            .dataHora(UPDATED_DATA_HORA)
            .observacao(UPDATED_OBSERVACAO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .amostraNoLaboratorio(UPDATED_AMOSTRA_NO_LABORATORIO);
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
        assertThat(testAmostra.getDataHora()).isEqualTo(UPDATED_DATA_HORA);
        assertThat(testAmostra.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testAmostra.getIdentificadorExterno()).isEqualTo(UPDATED_IDENTIFICADOR_EXTERNO);
        assertThat(testAmostra.getAmostraNoLaboratorio()).isEqualTo(UPDATED_AMOSTRA_NO_LABORATORIO);
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
            .amostraNoLaboratorio(UPDATED_AMOSTRA_NO_LABORATORIO);

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
        assertThat(testAmostra.getDataHora()).isEqualTo(DEFAULT_DATA_HORA);
        assertThat(testAmostra.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testAmostra.getIdentificadorExterno()).isEqualTo(UPDATED_IDENTIFICADOR_EXTERNO);
        assertThat(testAmostra.getAmostraNoLaboratorio()).isEqualTo(UPDATED_AMOSTRA_NO_LABORATORIO);
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
            .dataHora(UPDATED_DATA_HORA)
            .observacao(UPDATED_OBSERVACAO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .amostraNoLaboratorio(UPDATED_AMOSTRA_NO_LABORATORIO);

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
        assertThat(testAmostra.getDataHora()).isEqualTo(UPDATED_DATA_HORA);
        assertThat(testAmostra.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testAmostra.getIdentificadorExterno()).isEqualTo(UPDATED_IDENTIFICADOR_EXTERNO);
        assertThat(testAmostra.getAmostraNoLaboratorio()).isEqualTo(UPDATED_AMOSTRA_NO_LABORATORIO);
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
