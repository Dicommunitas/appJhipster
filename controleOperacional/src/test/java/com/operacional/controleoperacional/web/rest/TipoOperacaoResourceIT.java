package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.TipoOperacao;
import com.operacional.controleoperacional.repository.TipoOperacaoRepository;
import com.operacional.controleoperacional.service.dto.TipoOperacaoDTO;
import com.operacional.controleoperacional.service.mapper.TipoOperacaoMapper;
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
 * Integration tests for the {@link TipoOperacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoOperacaoResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-operacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoOperacaoRepository tipoOperacaoRepository;

    @Autowired
    private TipoOperacaoMapper tipoOperacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoOperacaoMockMvc;

    private TipoOperacao tipoOperacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoOperacao createEntity(EntityManager em) {
        TipoOperacao tipoOperacao = new TipoOperacao().descricao(DEFAULT_DESCRICAO);
        return tipoOperacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoOperacao createUpdatedEntity(EntityManager em) {
        TipoOperacao tipoOperacao = new TipoOperacao().descricao(UPDATED_DESCRICAO);
        return tipoOperacao;
    }

    @BeforeEach
    public void initTest() {
        tipoOperacao = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoOperacao() throws Exception {
        int databaseSizeBeforeCreate = tipoOperacaoRepository.findAll().size();
        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);
        restTipoOperacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TipoOperacao in the database
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeCreate + 1);
        TipoOperacao testTipoOperacao = tipoOperacaoList.get(tipoOperacaoList.size() - 1);
        assertThat(testTipoOperacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createTipoOperacaoWithExistingId() throws Exception {
        // Create the TipoOperacao with an existing ID
        tipoOperacao.setId(1L);
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        int databaseSizeBeforeCreate = tipoOperacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoOperacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoOperacao in the database
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoOperacaoRepository.findAll().size();
        // set the field null
        tipoOperacao.setDescricao(null);

        // Create the TipoOperacao, which fails.
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        restTipoOperacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoOperacaos() throws Exception {
        // Initialize the database
        tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        // Get all the tipoOperacaoList
        restTipoOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoOperacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getTipoOperacao() throws Exception {
        // Initialize the database
        tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        // Get the tipoOperacao
        restTipoOperacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoOperacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoOperacao.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingTipoOperacao() throws Exception {
        // Get the tipoOperacao
        restTipoOperacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoOperacao() throws Exception {
        // Initialize the database
        tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        int databaseSizeBeforeUpdate = tipoOperacaoRepository.findAll().size();

        // Update the tipoOperacao
        TipoOperacao updatedTipoOperacao = tipoOperacaoRepository.findById(tipoOperacao.getId()).get();
        // Disconnect from session so that the updates on updatedTipoOperacao are not directly saved in db
        em.detach(updatedTipoOperacao);
        updatedTipoOperacao.descricao(UPDATED_DESCRICAO);
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(updatedTipoOperacao);

        restTipoOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoOperacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoOperacao in the database
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeUpdate);
        TipoOperacao testTipoOperacao = tipoOperacaoList.get(tipoOperacaoList.size() - 1);
        assertThat(testTipoOperacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingTipoOperacao() throws Exception {
        int databaseSizeBeforeUpdate = tipoOperacaoRepository.findAll().size();
        tipoOperacao.setId(count.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoOperacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoOperacao in the database
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoOperacao() throws Exception {
        int databaseSizeBeforeUpdate = tipoOperacaoRepository.findAll().size();
        tipoOperacao.setId(count.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoOperacao in the database
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoOperacao() throws Exception {
        int databaseSizeBeforeUpdate = tipoOperacaoRepository.findAll().size();
        tipoOperacao.setId(count.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoOperacao in the database
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoOperacaoWithPatch() throws Exception {
        // Initialize the database
        tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        int databaseSizeBeforeUpdate = tipoOperacaoRepository.findAll().size();

        // Update the tipoOperacao using partial update
        TipoOperacao partialUpdatedTipoOperacao = new TipoOperacao();
        partialUpdatedTipoOperacao.setId(tipoOperacao.getId());

        restTipoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoOperacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoOperacao))
            )
            .andExpect(status().isOk());

        // Validate the TipoOperacao in the database
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeUpdate);
        TipoOperacao testTipoOperacao = tipoOperacaoList.get(tipoOperacaoList.size() - 1);
        assertThat(testTipoOperacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateTipoOperacaoWithPatch() throws Exception {
        // Initialize the database
        tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        int databaseSizeBeforeUpdate = tipoOperacaoRepository.findAll().size();

        // Update the tipoOperacao using partial update
        TipoOperacao partialUpdatedTipoOperacao = new TipoOperacao();
        partialUpdatedTipoOperacao.setId(tipoOperacao.getId());

        partialUpdatedTipoOperacao.descricao(UPDATED_DESCRICAO);

        restTipoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoOperacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoOperacao))
            )
            .andExpect(status().isOk());

        // Validate the TipoOperacao in the database
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeUpdate);
        TipoOperacao testTipoOperacao = tipoOperacaoList.get(tipoOperacaoList.size() - 1);
        assertThat(testTipoOperacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingTipoOperacao() throws Exception {
        int databaseSizeBeforeUpdate = tipoOperacaoRepository.findAll().size();
        tipoOperacao.setId(count.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoOperacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoOperacao in the database
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoOperacao() throws Exception {
        int databaseSizeBeforeUpdate = tipoOperacaoRepository.findAll().size();
        tipoOperacao.setId(count.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoOperacao in the database
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoOperacao() throws Exception {
        int databaseSizeBeforeUpdate = tipoOperacaoRepository.findAll().size();
        tipoOperacao.setId(count.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoOperacao in the database
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoOperacao() throws Exception {
        // Initialize the database
        tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        int databaseSizeBeforeDelete = tipoOperacaoRepository.findAll().size();

        // Delete the tipoOperacao
        restTipoOperacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoOperacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoOperacao> tipoOperacaoList = tipoOperacaoRepository.findAll();
        assertThat(tipoOperacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
