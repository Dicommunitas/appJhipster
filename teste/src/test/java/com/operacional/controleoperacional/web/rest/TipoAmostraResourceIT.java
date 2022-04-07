package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.TipoAmostra;
import com.operacional.controleoperacional.repository.TipoAmostraRepository;
import com.operacional.controleoperacional.service.dto.TipoAmostraDTO;
import com.operacional.controleoperacional.service.mapper.TipoAmostraMapper;
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
 * Integration tests for the {@link TipoAmostraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoAmostraResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-amostras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoAmostraRepository tipoAmostraRepository;

    @Autowired
    private TipoAmostraMapper tipoAmostraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoAmostraMockMvc;

    private TipoAmostra tipoAmostra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoAmostra createEntity(EntityManager em) {
        TipoAmostra tipoAmostra = new TipoAmostra().descricao(DEFAULT_DESCRICAO);
        return tipoAmostra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoAmostra createUpdatedEntity(EntityManager em) {
        TipoAmostra tipoAmostra = new TipoAmostra().descricao(UPDATED_DESCRICAO);
        return tipoAmostra;
    }

    @BeforeEach
    public void initTest() {
        tipoAmostra = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoAmostra() throws Exception {
        int databaseSizeBeforeCreate = tipoAmostraRepository.findAll().size();
        // Create the TipoAmostra
        TipoAmostraDTO tipoAmostraDTO = tipoAmostraMapper.toDto(tipoAmostra);
        restTipoAmostraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoAmostraDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TipoAmostra in the database
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeCreate + 1);
        TipoAmostra testTipoAmostra = tipoAmostraList.get(tipoAmostraList.size() - 1);
        assertThat(testTipoAmostra.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createTipoAmostraWithExistingId() throws Exception {
        // Create the TipoAmostra with an existing ID
        tipoAmostra.setId(1L);
        TipoAmostraDTO tipoAmostraDTO = tipoAmostraMapper.toDto(tipoAmostra);

        int databaseSizeBeforeCreate = tipoAmostraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoAmostraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoAmostra in the database
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoAmostraRepository.findAll().size();
        // set the field null
        tipoAmostra.setDescricao(null);

        // Create the TipoAmostra, which fails.
        TipoAmostraDTO tipoAmostraDTO = tipoAmostraMapper.toDto(tipoAmostra);

        restTipoAmostraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoAmostras() throws Exception {
        // Initialize the database
        tipoAmostraRepository.saveAndFlush(tipoAmostra);

        // Get all the tipoAmostraList
        restTipoAmostraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoAmostra.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getTipoAmostra() throws Exception {
        // Initialize the database
        tipoAmostraRepository.saveAndFlush(tipoAmostra);

        // Get the tipoAmostra
        restTipoAmostraMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoAmostra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoAmostra.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingTipoAmostra() throws Exception {
        // Get the tipoAmostra
        restTipoAmostraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoAmostra() throws Exception {
        // Initialize the database
        tipoAmostraRepository.saveAndFlush(tipoAmostra);

        int databaseSizeBeforeUpdate = tipoAmostraRepository.findAll().size();

        // Update the tipoAmostra
        TipoAmostra updatedTipoAmostra = tipoAmostraRepository.findById(tipoAmostra.getId()).get();
        // Disconnect from session so that the updates on updatedTipoAmostra are not directly saved in db
        em.detach(updatedTipoAmostra);
        updatedTipoAmostra.descricao(UPDATED_DESCRICAO);
        TipoAmostraDTO tipoAmostraDTO = tipoAmostraMapper.toDto(updatedTipoAmostra);

        restTipoAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoAmostraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoAmostraDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoAmostra in the database
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeUpdate);
        TipoAmostra testTipoAmostra = tipoAmostraList.get(tipoAmostraList.size() - 1);
        assertThat(testTipoAmostra.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingTipoAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoAmostraRepository.findAll().size();
        tipoAmostra.setId(count.incrementAndGet());

        // Create the TipoAmostra
        TipoAmostraDTO tipoAmostraDTO = tipoAmostraMapper.toDto(tipoAmostra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoAmostraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoAmostra in the database
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoAmostraRepository.findAll().size();
        tipoAmostra.setId(count.incrementAndGet());

        // Create the TipoAmostra
        TipoAmostraDTO tipoAmostraDTO = tipoAmostraMapper.toDto(tipoAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoAmostra in the database
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoAmostraRepository.findAll().size();
        tipoAmostra.setId(count.incrementAndGet());

        // Create the TipoAmostra
        TipoAmostraDTO tipoAmostraDTO = tipoAmostraMapper.toDto(tipoAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoAmostraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoAmostraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoAmostra in the database
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoAmostraWithPatch() throws Exception {
        // Initialize the database
        tipoAmostraRepository.saveAndFlush(tipoAmostra);

        int databaseSizeBeforeUpdate = tipoAmostraRepository.findAll().size();

        // Update the tipoAmostra using partial update
        TipoAmostra partialUpdatedTipoAmostra = new TipoAmostra();
        partialUpdatedTipoAmostra.setId(tipoAmostra.getId());

        partialUpdatedTipoAmostra.descricao(UPDATED_DESCRICAO);

        restTipoAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoAmostra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoAmostra))
            )
            .andExpect(status().isOk());

        // Validate the TipoAmostra in the database
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeUpdate);
        TipoAmostra testTipoAmostra = tipoAmostraList.get(tipoAmostraList.size() - 1);
        assertThat(testTipoAmostra.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateTipoAmostraWithPatch() throws Exception {
        // Initialize the database
        tipoAmostraRepository.saveAndFlush(tipoAmostra);

        int databaseSizeBeforeUpdate = tipoAmostraRepository.findAll().size();

        // Update the tipoAmostra using partial update
        TipoAmostra partialUpdatedTipoAmostra = new TipoAmostra();
        partialUpdatedTipoAmostra.setId(tipoAmostra.getId());

        partialUpdatedTipoAmostra.descricao(UPDATED_DESCRICAO);

        restTipoAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoAmostra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoAmostra))
            )
            .andExpect(status().isOk());

        // Validate the TipoAmostra in the database
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeUpdate);
        TipoAmostra testTipoAmostra = tipoAmostraList.get(tipoAmostraList.size() - 1);
        assertThat(testTipoAmostra.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingTipoAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoAmostraRepository.findAll().size();
        tipoAmostra.setId(count.incrementAndGet());

        // Create the TipoAmostra
        TipoAmostraDTO tipoAmostraDTO = tipoAmostraMapper.toDto(tipoAmostra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoAmostraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoAmostra in the database
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoAmostraRepository.findAll().size();
        tipoAmostra.setId(count.incrementAndGet());

        // Create the TipoAmostra
        TipoAmostraDTO tipoAmostraDTO = tipoAmostraMapper.toDto(tipoAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoAmostra in the database
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoAmostraRepository.findAll().size();
        tipoAmostra.setId(count.incrementAndGet());

        // Create the TipoAmostra
        TipoAmostraDTO tipoAmostraDTO = tipoAmostraMapper.toDto(tipoAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoAmostraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoAmostra in the database
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoAmostra() throws Exception {
        // Initialize the database
        tipoAmostraRepository.saveAndFlush(tipoAmostra);

        int databaseSizeBeforeDelete = tipoAmostraRepository.findAll().size();

        // Delete the tipoAmostra
        restTipoAmostraMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoAmostra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoAmostra> tipoAmostraList = tipoAmostraRepository.findAll();
        assertThat(tipoAmostraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
