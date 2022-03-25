package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.TipoFinalidadeAmostra;
import com.operacional.controleoperacional.repository.TipoFinalidadeAmostraRepository;
import com.operacional.controleoperacional.service.dto.TipoFinalidadeAmostraDTO;
import com.operacional.controleoperacional.service.mapper.TipoFinalidadeAmostraMapper;
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
 * Integration tests for the {@link TipoFinalidadeAmostraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoFinalidadeAmostraResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OBRIGATORIO_LACRE = false;
    private static final Boolean UPDATED_OBRIGATORIO_LACRE = true;

    private static final String ENTITY_API_URL = "/api/tipo-finalidade-amostras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoFinalidadeAmostraRepository tipoFinalidadeAmostraRepository;

    @Autowired
    private TipoFinalidadeAmostraMapper tipoFinalidadeAmostraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoFinalidadeAmostraMockMvc;

    private TipoFinalidadeAmostra tipoFinalidadeAmostra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoFinalidadeAmostra createEntity(EntityManager em) {
        TipoFinalidadeAmostra tipoFinalidadeAmostra = new TipoFinalidadeAmostra()
            .descricao(DEFAULT_DESCRICAO)
            .obrigatorioLacre(DEFAULT_OBRIGATORIO_LACRE);
        return tipoFinalidadeAmostra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoFinalidadeAmostra createUpdatedEntity(EntityManager em) {
        TipoFinalidadeAmostra tipoFinalidadeAmostra = new TipoFinalidadeAmostra()
            .descricao(UPDATED_DESCRICAO)
            .obrigatorioLacre(UPDATED_OBRIGATORIO_LACRE);
        return tipoFinalidadeAmostra;
    }

    @BeforeEach
    public void initTest() {
        tipoFinalidadeAmostra = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeCreate = tipoFinalidadeAmostraRepository.findAll().size();
        // Create the TipoFinalidadeAmostra
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);
        restTipoFinalidadeAmostraMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFinalidadeAmostraDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TipoFinalidadeAmostra in the database
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeCreate + 1);
        TipoFinalidadeAmostra testTipoFinalidadeAmostra = tipoFinalidadeAmostraList.get(tipoFinalidadeAmostraList.size() - 1);
        assertThat(testTipoFinalidadeAmostra.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTipoFinalidadeAmostra.getObrigatorioLacre()).isEqualTo(DEFAULT_OBRIGATORIO_LACRE);
    }

    @Test
    @Transactional
    void createTipoFinalidadeAmostraWithExistingId() throws Exception {
        // Create the TipoFinalidadeAmostra with an existing ID
        tipoFinalidadeAmostra.setId(1L);
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);

        int databaseSizeBeforeCreate = tipoFinalidadeAmostraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoFinalidadeAmostraMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFinalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFinalidadeAmostra in the database
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoFinalidadeAmostraRepository.findAll().size();
        // set the field null
        tipoFinalidadeAmostra.setDescricao(null);

        // Create the TipoFinalidadeAmostra, which fails.
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);

        restTipoFinalidadeAmostraMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFinalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkObrigatorioLacreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoFinalidadeAmostraRepository.findAll().size();
        // set the field null
        tipoFinalidadeAmostra.setObrigatorioLacre(null);

        // Create the TipoFinalidadeAmostra, which fails.
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);

        restTipoFinalidadeAmostraMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFinalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoFinalidadeAmostras() throws Exception {
        // Initialize the database
        tipoFinalidadeAmostraRepository.saveAndFlush(tipoFinalidadeAmostra);

        // Get all the tipoFinalidadeAmostraList
        restTipoFinalidadeAmostraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoFinalidadeAmostra.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].obrigatorioLacre").value(hasItem(DEFAULT_OBRIGATORIO_LACRE.booleanValue())));
    }

    @Test
    @Transactional
    void getTipoFinalidadeAmostra() throws Exception {
        // Initialize the database
        tipoFinalidadeAmostraRepository.saveAndFlush(tipoFinalidadeAmostra);

        // Get the tipoFinalidadeAmostra
        restTipoFinalidadeAmostraMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoFinalidadeAmostra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoFinalidadeAmostra.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.obrigatorioLacre").value(DEFAULT_OBRIGATORIO_LACRE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTipoFinalidadeAmostra() throws Exception {
        // Get the tipoFinalidadeAmostra
        restTipoFinalidadeAmostraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoFinalidadeAmostra() throws Exception {
        // Initialize the database
        tipoFinalidadeAmostraRepository.saveAndFlush(tipoFinalidadeAmostra);

        int databaseSizeBeforeUpdate = tipoFinalidadeAmostraRepository.findAll().size();

        // Update the tipoFinalidadeAmostra
        TipoFinalidadeAmostra updatedTipoFinalidadeAmostra = tipoFinalidadeAmostraRepository.findById(tipoFinalidadeAmostra.getId()).get();
        // Disconnect from session so that the updates on updatedTipoFinalidadeAmostra are not directly saved in db
        em.detach(updatedTipoFinalidadeAmostra);
        updatedTipoFinalidadeAmostra.descricao(UPDATED_DESCRICAO).obrigatorioLacre(UPDATED_OBRIGATORIO_LACRE);
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraMapper.toDto(updatedTipoFinalidadeAmostra);

        restTipoFinalidadeAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoFinalidadeAmostraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFinalidadeAmostraDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoFinalidadeAmostra in the database
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
        TipoFinalidadeAmostra testTipoFinalidadeAmostra = tipoFinalidadeAmostraList.get(tipoFinalidadeAmostraList.size() - 1);
        assertThat(testTipoFinalidadeAmostra.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTipoFinalidadeAmostra.getObrigatorioLacre()).isEqualTo(UPDATED_OBRIGATORIO_LACRE);
    }

    @Test
    @Transactional
    void putNonExistingTipoFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoFinalidadeAmostraRepository.findAll().size();
        tipoFinalidadeAmostra.setId(count.incrementAndGet());

        // Create the TipoFinalidadeAmostra
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoFinalidadeAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoFinalidadeAmostraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFinalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFinalidadeAmostra in the database
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoFinalidadeAmostraRepository.findAll().size();
        tipoFinalidadeAmostra.setId(count.incrementAndGet());

        // Create the TipoFinalidadeAmostra
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFinalidadeAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFinalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFinalidadeAmostra in the database
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoFinalidadeAmostraRepository.findAll().size();
        tipoFinalidadeAmostra.setId(count.incrementAndGet());

        // Create the TipoFinalidadeAmostra
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFinalidadeAmostraMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoFinalidadeAmostraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoFinalidadeAmostra in the database
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoFinalidadeAmostraWithPatch() throws Exception {
        // Initialize the database
        tipoFinalidadeAmostraRepository.saveAndFlush(tipoFinalidadeAmostra);

        int databaseSizeBeforeUpdate = tipoFinalidadeAmostraRepository.findAll().size();

        // Update the tipoFinalidadeAmostra using partial update
        TipoFinalidadeAmostra partialUpdatedTipoFinalidadeAmostra = new TipoFinalidadeAmostra();
        partialUpdatedTipoFinalidadeAmostra.setId(tipoFinalidadeAmostra.getId());

        partialUpdatedTipoFinalidadeAmostra.descricao(UPDATED_DESCRICAO);

        restTipoFinalidadeAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoFinalidadeAmostra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoFinalidadeAmostra))
            )
            .andExpect(status().isOk());

        // Validate the TipoFinalidadeAmostra in the database
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
        TipoFinalidadeAmostra testTipoFinalidadeAmostra = tipoFinalidadeAmostraList.get(tipoFinalidadeAmostraList.size() - 1);
        assertThat(testTipoFinalidadeAmostra.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTipoFinalidadeAmostra.getObrigatorioLacre()).isEqualTo(DEFAULT_OBRIGATORIO_LACRE);
    }

    @Test
    @Transactional
    void fullUpdateTipoFinalidadeAmostraWithPatch() throws Exception {
        // Initialize the database
        tipoFinalidadeAmostraRepository.saveAndFlush(tipoFinalidadeAmostra);

        int databaseSizeBeforeUpdate = tipoFinalidadeAmostraRepository.findAll().size();

        // Update the tipoFinalidadeAmostra using partial update
        TipoFinalidadeAmostra partialUpdatedTipoFinalidadeAmostra = new TipoFinalidadeAmostra();
        partialUpdatedTipoFinalidadeAmostra.setId(tipoFinalidadeAmostra.getId());

        partialUpdatedTipoFinalidadeAmostra.descricao(UPDATED_DESCRICAO).obrigatorioLacre(UPDATED_OBRIGATORIO_LACRE);

        restTipoFinalidadeAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoFinalidadeAmostra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoFinalidadeAmostra))
            )
            .andExpect(status().isOk());

        // Validate the TipoFinalidadeAmostra in the database
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
        TipoFinalidadeAmostra testTipoFinalidadeAmostra = tipoFinalidadeAmostraList.get(tipoFinalidadeAmostraList.size() - 1);
        assertThat(testTipoFinalidadeAmostra.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTipoFinalidadeAmostra.getObrigatorioLacre()).isEqualTo(UPDATED_OBRIGATORIO_LACRE);
    }

    @Test
    @Transactional
    void patchNonExistingTipoFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoFinalidadeAmostraRepository.findAll().size();
        tipoFinalidadeAmostra.setId(count.incrementAndGet());

        // Create the TipoFinalidadeAmostra
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoFinalidadeAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoFinalidadeAmostraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoFinalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFinalidadeAmostra in the database
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoFinalidadeAmostraRepository.findAll().size();
        tipoFinalidadeAmostra.setId(count.incrementAndGet());

        // Create the TipoFinalidadeAmostra
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFinalidadeAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoFinalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoFinalidadeAmostra in the database
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = tipoFinalidadeAmostraRepository.findAll().size();
        tipoFinalidadeAmostra.setId(count.incrementAndGet());

        // Create the TipoFinalidadeAmostra
        TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoFinalidadeAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoFinalidadeAmostraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoFinalidadeAmostra in the database
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoFinalidadeAmostra() throws Exception {
        // Initialize the database
        tipoFinalidadeAmostraRepository.saveAndFlush(tipoFinalidadeAmostra);

        int databaseSizeBeforeDelete = tipoFinalidadeAmostraRepository.findAll().size();

        // Delete the tipoFinalidadeAmostra
        restTipoFinalidadeAmostraMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoFinalidadeAmostra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoFinalidadeAmostra> tipoFinalidadeAmostraList = tipoFinalidadeAmostraRepository.findAll();
        assertThat(tipoFinalidadeAmostraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
