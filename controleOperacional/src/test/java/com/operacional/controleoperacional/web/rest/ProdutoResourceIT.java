package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.AlertaProduto;
import com.operacional.controleoperacional.domain.Produto;
import com.operacional.controleoperacional.repository.ProdutoRepository;
import com.operacional.controleoperacional.service.ProdutoService;
import com.operacional.controleoperacional.service.criteria.ProdutoCriteria;
import com.operacional.controleoperacional.service.dto.ProdutoDTO;
import com.operacional.controleoperacional.service.mapper.ProdutoMapper;
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

/**
 * Integration tests for the {@link ProdutoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProdutoResourceIT {

    private static final String DEFAULT_CODIGO_BDEMQ = "AAA";
    private static final String UPDATED_CODIGO_BDEMQ = "BBB";

    private static final String DEFAULT_NOME_CURTO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_CURTO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_COMPLETO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_COMPLETO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/produtos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoRepository produtoRepositoryMock;

    @Autowired
    private ProdutoMapper produtoMapper;

    @Mock
    private ProdutoService produtoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProdutoMockMvc;

    private Produto produto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produto createEntity(EntityManager em) {
        Produto produto = new Produto().codigoBDEMQ(DEFAULT_CODIGO_BDEMQ).nomeCurto(DEFAULT_NOME_CURTO).nomeCompleto(DEFAULT_NOME_COMPLETO);
        return produto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produto createUpdatedEntity(EntityManager em) {
        Produto produto = new Produto().codigoBDEMQ(UPDATED_CODIGO_BDEMQ).nomeCurto(UPDATED_NOME_CURTO).nomeCompleto(UPDATED_NOME_COMPLETO);
        return produto;
    }

    @BeforeEach
    public void initTest() {
        produto = createEntity(em);
    }

    @Test
    @Transactional
    void createProduto() throws Exception {
        int databaseSizeBeforeCreate = produtoRepository.findAll().size();
        // Create the Produto
        ProdutoDTO produtoDTO = produtoMapper.toDto(produto);
        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produtoDTO)))
            .andExpect(status().isCreated());

        // Validate the Produto in the database
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeCreate + 1);
        Produto testProduto = produtoList.get(produtoList.size() - 1);
        assertThat(testProduto.getCodigoBDEMQ()).isEqualTo(DEFAULT_CODIGO_BDEMQ);
        assertThat(testProduto.getNomeCurto()).isEqualTo(DEFAULT_NOME_CURTO);
        assertThat(testProduto.getNomeCompleto()).isEqualTo(DEFAULT_NOME_COMPLETO);
    }

    @Test
    @Transactional
    void createProdutoWithExistingId() throws Exception {
        // Create the Produto with an existing ID
        produto.setId(1L);
        ProdutoDTO produtoDTO = produtoMapper.toDto(produto);

        int databaseSizeBeforeCreate = produtoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produtoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoBDEMQIsRequired() throws Exception {
        int databaseSizeBeforeTest = produtoRepository.findAll().size();
        // set the field null
        produto.setCodigoBDEMQ(null);

        // Create the Produto, which fails.
        ProdutoDTO produtoDTO = produtoMapper.toDto(produto);

        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produtoDTO)))
            .andExpect(status().isBadRequest());

        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeCurtoIsRequired() throws Exception {
        int databaseSizeBeforeTest = produtoRepository.findAll().size();
        // set the field null
        produto.setNomeCurto(null);

        // Create the Produto, which fails.
        ProdutoDTO produtoDTO = produtoMapper.toDto(produto);

        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produtoDTO)))
            .andExpect(status().isBadRequest());

        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeCompletoIsRequired() throws Exception {
        int databaseSizeBeforeTest = produtoRepository.findAll().size();
        // set the field null
        produto.setNomeCompleto(null);

        // Create the Produto, which fails.
        ProdutoDTO produtoDTO = produtoMapper.toDto(produto);

        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produtoDTO)))
            .andExpect(status().isBadRequest());

        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProdutos() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList
        restProdutoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produto.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigoBDEMQ").value(hasItem(DEFAULT_CODIGO_BDEMQ)))
            .andExpect(jsonPath("$.[*].nomeCurto").value(hasItem(DEFAULT_NOME_CURTO)))
            .andExpect(jsonPath("$.[*].nomeCompleto").value(hasItem(DEFAULT_NOME_COMPLETO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProdutosWithEagerRelationshipsIsEnabled() throws Exception {
        when(produtoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProdutoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(produtoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProdutosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(produtoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProdutoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(produtoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProduto() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get the produto
        restProdutoMockMvc
            .perform(get(ENTITY_API_URL_ID, produto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(produto.getId().intValue()))
            .andExpect(jsonPath("$.codigoBDEMQ").value(DEFAULT_CODIGO_BDEMQ))
            .andExpect(jsonPath("$.nomeCurto").value(DEFAULT_NOME_CURTO))
            .andExpect(jsonPath("$.nomeCompleto").value(DEFAULT_NOME_COMPLETO));
    }

    @Test
    @Transactional
    void getProdutosByIdFiltering() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        Long id = produto.getId();

        defaultProdutoShouldBeFound("id.equals=" + id);
        defaultProdutoShouldNotBeFound("id.notEquals=" + id);

        defaultProdutoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProdutoShouldNotBeFound("id.greaterThan=" + id);

        defaultProdutoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProdutoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProdutosByCodigoBDEMQIsEqualToSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where codigoBDEMQ equals to DEFAULT_CODIGO_BDEMQ
        defaultProdutoShouldBeFound("codigoBDEMQ.equals=" + DEFAULT_CODIGO_BDEMQ);

        // Get all the produtoList where codigoBDEMQ equals to UPDATED_CODIGO_BDEMQ
        defaultProdutoShouldNotBeFound("codigoBDEMQ.equals=" + UPDATED_CODIGO_BDEMQ);
    }

    @Test
    @Transactional
    void getAllProdutosByCodigoBDEMQIsNotEqualToSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where codigoBDEMQ not equals to DEFAULT_CODIGO_BDEMQ
        defaultProdutoShouldNotBeFound("codigoBDEMQ.notEquals=" + DEFAULT_CODIGO_BDEMQ);

        // Get all the produtoList where codigoBDEMQ not equals to UPDATED_CODIGO_BDEMQ
        defaultProdutoShouldBeFound("codigoBDEMQ.notEquals=" + UPDATED_CODIGO_BDEMQ);
    }

    @Test
    @Transactional
    void getAllProdutosByCodigoBDEMQIsInShouldWork() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where codigoBDEMQ in DEFAULT_CODIGO_BDEMQ or UPDATED_CODIGO_BDEMQ
        defaultProdutoShouldBeFound("codigoBDEMQ.in=" + DEFAULT_CODIGO_BDEMQ + "," + UPDATED_CODIGO_BDEMQ);

        // Get all the produtoList where codigoBDEMQ equals to UPDATED_CODIGO_BDEMQ
        defaultProdutoShouldNotBeFound("codigoBDEMQ.in=" + UPDATED_CODIGO_BDEMQ);
    }

    @Test
    @Transactional
    void getAllProdutosByCodigoBDEMQIsNullOrNotNull() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where codigoBDEMQ is not null
        defaultProdutoShouldBeFound("codigoBDEMQ.specified=true");

        // Get all the produtoList where codigoBDEMQ is null
        defaultProdutoShouldNotBeFound("codigoBDEMQ.specified=false");
    }

    @Test
    @Transactional
    void getAllProdutosByCodigoBDEMQContainsSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where codigoBDEMQ contains DEFAULT_CODIGO_BDEMQ
        defaultProdutoShouldBeFound("codigoBDEMQ.contains=" + DEFAULT_CODIGO_BDEMQ);

        // Get all the produtoList where codigoBDEMQ contains UPDATED_CODIGO_BDEMQ
        defaultProdutoShouldNotBeFound("codigoBDEMQ.contains=" + UPDATED_CODIGO_BDEMQ);
    }

    @Test
    @Transactional
    void getAllProdutosByCodigoBDEMQNotContainsSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where codigoBDEMQ does not contain DEFAULT_CODIGO_BDEMQ
        defaultProdutoShouldNotBeFound("codigoBDEMQ.doesNotContain=" + DEFAULT_CODIGO_BDEMQ);

        // Get all the produtoList where codigoBDEMQ does not contain UPDATED_CODIGO_BDEMQ
        defaultProdutoShouldBeFound("codigoBDEMQ.doesNotContain=" + UPDATED_CODIGO_BDEMQ);
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCurtoIsEqualToSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCurto equals to DEFAULT_NOME_CURTO
        defaultProdutoShouldBeFound("nomeCurto.equals=" + DEFAULT_NOME_CURTO);

        // Get all the produtoList where nomeCurto equals to UPDATED_NOME_CURTO
        defaultProdutoShouldNotBeFound("nomeCurto.equals=" + UPDATED_NOME_CURTO);
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCurtoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCurto not equals to DEFAULT_NOME_CURTO
        defaultProdutoShouldNotBeFound("nomeCurto.notEquals=" + DEFAULT_NOME_CURTO);

        // Get all the produtoList where nomeCurto not equals to UPDATED_NOME_CURTO
        defaultProdutoShouldBeFound("nomeCurto.notEquals=" + UPDATED_NOME_CURTO);
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCurtoIsInShouldWork() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCurto in DEFAULT_NOME_CURTO or UPDATED_NOME_CURTO
        defaultProdutoShouldBeFound("nomeCurto.in=" + DEFAULT_NOME_CURTO + "," + UPDATED_NOME_CURTO);

        // Get all the produtoList where nomeCurto equals to UPDATED_NOME_CURTO
        defaultProdutoShouldNotBeFound("nomeCurto.in=" + UPDATED_NOME_CURTO);
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCurtoIsNullOrNotNull() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCurto is not null
        defaultProdutoShouldBeFound("nomeCurto.specified=true");

        // Get all the produtoList where nomeCurto is null
        defaultProdutoShouldNotBeFound("nomeCurto.specified=false");
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCurtoContainsSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCurto contains DEFAULT_NOME_CURTO
        defaultProdutoShouldBeFound("nomeCurto.contains=" + DEFAULT_NOME_CURTO);

        // Get all the produtoList where nomeCurto contains UPDATED_NOME_CURTO
        defaultProdutoShouldNotBeFound("nomeCurto.contains=" + UPDATED_NOME_CURTO);
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCurtoNotContainsSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCurto does not contain DEFAULT_NOME_CURTO
        defaultProdutoShouldNotBeFound("nomeCurto.doesNotContain=" + DEFAULT_NOME_CURTO);

        // Get all the produtoList where nomeCurto does not contain UPDATED_NOME_CURTO
        defaultProdutoShouldBeFound("nomeCurto.doesNotContain=" + UPDATED_NOME_CURTO);
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCompletoIsEqualToSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCompleto equals to DEFAULT_NOME_COMPLETO
        defaultProdutoShouldBeFound("nomeCompleto.equals=" + DEFAULT_NOME_COMPLETO);

        // Get all the produtoList where nomeCompleto equals to UPDATED_NOME_COMPLETO
        defaultProdutoShouldNotBeFound("nomeCompleto.equals=" + UPDATED_NOME_COMPLETO);
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCompletoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCompleto not equals to DEFAULT_NOME_COMPLETO
        defaultProdutoShouldNotBeFound("nomeCompleto.notEquals=" + DEFAULT_NOME_COMPLETO);

        // Get all the produtoList where nomeCompleto not equals to UPDATED_NOME_COMPLETO
        defaultProdutoShouldBeFound("nomeCompleto.notEquals=" + UPDATED_NOME_COMPLETO);
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCompletoIsInShouldWork() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCompleto in DEFAULT_NOME_COMPLETO or UPDATED_NOME_COMPLETO
        defaultProdutoShouldBeFound("nomeCompleto.in=" + DEFAULT_NOME_COMPLETO + "," + UPDATED_NOME_COMPLETO);

        // Get all the produtoList where nomeCompleto equals to UPDATED_NOME_COMPLETO
        defaultProdutoShouldNotBeFound("nomeCompleto.in=" + UPDATED_NOME_COMPLETO);
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCompletoIsNullOrNotNull() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCompleto is not null
        defaultProdutoShouldBeFound("nomeCompleto.specified=true");

        // Get all the produtoList where nomeCompleto is null
        defaultProdutoShouldNotBeFound("nomeCompleto.specified=false");
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCompletoContainsSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCompleto contains DEFAULT_NOME_COMPLETO
        defaultProdutoShouldBeFound("nomeCompleto.contains=" + DEFAULT_NOME_COMPLETO);

        // Get all the produtoList where nomeCompleto contains UPDATED_NOME_COMPLETO
        defaultProdutoShouldNotBeFound("nomeCompleto.contains=" + UPDATED_NOME_COMPLETO);
    }

    @Test
    @Transactional
    void getAllProdutosByNomeCompletoNotContainsSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtoList where nomeCompleto does not contain DEFAULT_NOME_COMPLETO
        defaultProdutoShouldNotBeFound("nomeCompleto.doesNotContain=" + DEFAULT_NOME_COMPLETO);

        // Get all the produtoList where nomeCompleto does not contain UPDATED_NOME_COMPLETO
        defaultProdutoShouldBeFound("nomeCompleto.doesNotContain=" + UPDATED_NOME_COMPLETO);
    }

    @Test
    @Transactional
    void getAllProdutosByAlertasIsEqualToSomething() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);
        AlertaProduto alertas;
        if (TestUtil.findAll(em, AlertaProduto.class).isEmpty()) {
            alertas = AlertaProdutoResourceIT.createEntity(em);
            em.persist(alertas);
            em.flush();
        } else {
            alertas = TestUtil.findAll(em, AlertaProduto.class).get(0);
        }
        em.persist(alertas);
        em.flush();
        produto.addAlertas(alertas);
        produtoRepository.saveAndFlush(produto);
        Long alertasId = alertas.getId();

        // Get all the produtoList where alertas equals to alertasId
        defaultProdutoShouldBeFound("alertasId.equals=" + alertasId);

        // Get all the produtoList where alertas equals to (alertasId + 1)
        defaultProdutoShouldNotBeFound("alertasId.equals=" + (alertasId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProdutoShouldBeFound(String filter) throws Exception {
        restProdutoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produto.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigoBDEMQ").value(hasItem(DEFAULT_CODIGO_BDEMQ)))
            .andExpect(jsonPath("$.[*].nomeCurto").value(hasItem(DEFAULT_NOME_CURTO)))
            .andExpect(jsonPath("$.[*].nomeCompleto").value(hasItem(DEFAULT_NOME_COMPLETO)));

        // Check, that the count call also returns 1
        restProdutoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProdutoShouldNotBeFound(String filter) throws Exception {
        restProdutoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProdutoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduto() throws Exception {
        // Get the produto
        restProdutoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProduto() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        int databaseSizeBeforeUpdate = produtoRepository.findAll().size();

        // Update the produto
        Produto updatedProduto = produtoRepository.findById(produto.getId()).get();
        // Disconnect from session so that the updates on updatedProduto are not directly saved in db
        em.detach(updatedProduto);
        updatedProduto.codigoBDEMQ(UPDATED_CODIGO_BDEMQ).nomeCurto(UPDATED_NOME_CURTO).nomeCompleto(UPDATED_NOME_COMPLETO);
        ProdutoDTO produtoDTO = produtoMapper.toDto(updatedProduto);

        restProdutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, produtoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(produtoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Produto in the database
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeUpdate);
        Produto testProduto = produtoList.get(produtoList.size() - 1);
        assertThat(testProduto.getCodigoBDEMQ()).isEqualTo(UPDATED_CODIGO_BDEMQ);
        assertThat(testProduto.getNomeCurto()).isEqualTo(UPDATED_NOME_CURTO);
        assertThat(testProduto.getNomeCompleto()).isEqualTo(UPDATED_NOME_COMPLETO);
    }

    @Test
    @Transactional
    void putNonExistingProduto() throws Exception {
        int databaseSizeBeforeUpdate = produtoRepository.findAll().size();
        produto.setId(count.incrementAndGet());

        // Create the Produto
        ProdutoDTO produtoDTO = produtoMapper.toDto(produto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, produtoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(produtoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduto() throws Exception {
        int databaseSizeBeforeUpdate = produtoRepository.findAll().size();
        produto.setId(count.incrementAndGet());

        // Create the Produto
        ProdutoDTO produtoDTO = produtoMapper.toDto(produto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(produtoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduto() throws Exception {
        int databaseSizeBeforeUpdate = produtoRepository.findAll().size();
        produto.setId(count.incrementAndGet());

        // Create the Produto
        ProdutoDTO produtoDTO = produtoMapper.toDto(produto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produtoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produto in the database
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProdutoWithPatch() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        int databaseSizeBeforeUpdate = produtoRepository.findAll().size();

        // Update the produto using partial update
        Produto partialUpdatedProduto = new Produto();
        partialUpdatedProduto.setId(produto.getId());

        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduto))
            )
            .andExpect(status().isOk());

        // Validate the Produto in the database
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeUpdate);
        Produto testProduto = produtoList.get(produtoList.size() - 1);
        assertThat(testProduto.getCodigoBDEMQ()).isEqualTo(DEFAULT_CODIGO_BDEMQ);
        assertThat(testProduto.getNomeCurto()).isEqualTo(DEFAULT_NOME_CURTO);
        assertThat(testProduto.getNomeCompleto()).isEqualTo(DEFAULT_NOME_COMPLETO);
    }

    @Test
    @Transactional
    void fullUpdateProdutoWithPatch() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        int databaseSizeBeforeUpdate = produtoRepository.findAll().size();

        // Update the produto using partial update
        Produto partialUpdatedProduto = new Produto();
        partialUpdatedProduto.setId(produto.getId());

        partialUpdatedProduto.codigoBDEMQ(UPDATED_CODIGO_BDEMQ).nomeCurto(UPDATED_NOME_CURTO).nomeCompleto(UPDATED_NOME_COMPLETO);

        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduto))
            )
            .andExpect(status().isOk());

        // Validate the Produto in the database
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeUpdate);
        Produto testProduto = produtoList.get(produtoList.size() - 1);
        assertThat(testProduto.getCodigoBDEMQ()).isEqualTo(UPDATED_CODIGO_BDEMQ);
        assertThat(testProduto.getNomeCurto()).isEqualTo(UPDATED_NOME_CURTO);
        assertThat(testProduto.getNomeCompleto()).isEqualTo(UPDATED_NOME_COMPLETO);
    }

    @Test
    @Transactional
    void patchNonExistingProduto() throws Exception {
        int databaseSizeBeforeUpdate = produtoRepository.findAll().size();
        produto.setId(count.incrementAndGet());

        // Create the Produto
        ProdutoDTO produtoDTO = produtoMapper.toDto(produto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, produtoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(produtoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduto() throws Exception {
        int databaseSizeBeforeUpdate = produtoRepository.findAll().size();
        produto.setId(count.incrementAndGet());

        // Create the Produto
        ProdutoDTO produtoDTO = produtoMapper.toDto(produto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(produtoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduto() throws Exception {
        int databaseSizeBeforeUpdate = produtoRepository.findAll().size();
        produto.setId(count.incrementAndGet());

        // Create the Produto
        ProdutoDTO produtoDTO = produtoMapper.toDto(produto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(produtoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produto in the database
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduto() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        int databaseSizeBeforeDelete = produtoRepository.findAll().size();

        // Delete the produto
        restProdutoMockMvc
            .perform(delete(ENTITY_API_URL_ID, produto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Produto> produtoList = produtoRepository.findAll();
        assertThat(produtoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
