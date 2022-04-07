package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.OrigemAmostra;
import com.operacional.controleoperacional.repository.OrigemAmostraRepository;
import com.operacional.controleoperacional.service.dto.OrigemAmostraDTO;
import com.operacional.controleoperacional.service.mapper.OrigemAmostraMapper;
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
 * Integration tests for the {@link OrigemAmostraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrigemAmostraResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/origem-amostras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrigemAmostraRepository origemAmostraRepository;

    @Autowired
    private OrigemAmostraMapper origemAmostraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrigemAmostraMockMvc;

    private OrigemAmostra origemAmostra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrigemAmostra createEntity(EntityManager em) {
        OrigemAmostra origemAmostra = new OrigemAmostra().descricao(DEFAULT_DESCRICAO);
        return origemAmostra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrigemAmostra createUpdatedEntity(EntityManager em) {
        OrigemAmostra origemAmostra = new OrigemAmostra().descricao(UPDATED_DESCRICAO);
        return origemAmostra;
    }

    @BeforeEach
    public void initTest() {
        origemAmostra = createEntity(em);
    }

    @Test
    @Transactional
    void createOrigemAmostra() throws Exception {
        int databaseSizeBeforeCreate = origemAmostraRepository.findAll().size();
        // Create the OrigemAmostra
        OrigemAmostraDTO origemAmostraDTO = origemAmostraMapper.toDto(origemAmostra);
        restOrigemAmostraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(origemAmostraDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrigemAmostra in the database
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeCreate + 1);
        OrigemAmostra testOrigemAmostra = origemAmostraList.get(origemAmostraList.size() - 1);
        assertThat(testOrigemAmostra.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createOrigemAmostraWithExistingId() throws Exception {
        // Create the OrigemAmostra with an existing ID
        origemAmostra.setId(1L);
        OrigemAmostraDTO origemAmostraDTO = origemAmostraMapper.toDto(origemAmostra);

        int databaseSizeBeforeCreate = origemAmostraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrigemAmostraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(origemAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrigemAmostra in the database
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = origemAmostraRepository.findAll().size();
        // set the field null
        origemAmostra.setDescricao(null);

        // Create the OrigemAmostra, which fails.
        OrigemAmostraDTO origemAmostraDTO = origemAmostraMapper.toDto(origemAmostra);

        restOrigemAmostraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(origemAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrigemAmostras() throws Exception {
        // Initialize the database
        origemAmostraRepository.saveAndFlush(origemAmostra);

        // Get all the origemAmostraList
        restOrigemAmostraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(origemAmostra.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getOrigemAmostra() throws Exception {
        // Initialize the database
        origemAmostraRepository.saveAndFlush(origemAmostra);

        // Get the origemAmostra
        restOrigemAmostraMockMvc
            .perform(get(ENTITY_API_URL_ID, origemAmostra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(origemAmostra.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingOrigemAmostra() throws Exception {
        // Get the origemAmostra
        restOrigemAmostraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrigemAmostra() throws Exception {
        // Initialize the database
        origemAmostraRepository.saveAndFlush(origemAmostra);

        int databaseSizeBeforeUpdate = origemAmostraRepository.findAll().size();

        // Update the origemAmostra
        OrigemAmostra updatedOrigemAmostra = origemAmostraRepository.findById(origemAmostra.getId()).get();
        // Disconnect from session so that the updates on updatedOrigemAmostra are not directly saved in db
        em.detach(updatedOrigemAmostra);
        updatedOrigemAmostra.descricao(UPDATED_DESCRICAO);
        OrigemAmostraDTO origemAmostraDTO = origemAmostraMapper.toDto(updatedOrigemAmostra);

        restOrigemAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, origemAmostraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(origemAmostraDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrigemAmostra in the database
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeUpdate);
        OrigemAmostra testOrigemAmostra = origemAmostraList.get(origemAmostraList.size() - 1);
        assertThat(testOrigemAmostra.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingOrigemAmostra() throws Exception {
        int databaseSizeBeforeUpdate = origemAmostraRepository.findAll().size();
        origemAmostra.setId(count.incrementAndGet());

        // Create the OrigemAmostra
        OrigemAmostraDTO origemAmostraDTO = origemAmostraMapper.toDto(origemAmostra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrigemAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, origemAmostraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(origemAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrigemAmostra in the database
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrigemAmostra() throws Exception {
        int databaseSizeBeforeUpdate = origemAmostraRepository.findAll().size();
        origemAmostra.setId(count.incrementAndGet());

        // Create the OrigemAmostra
        OrigemAmostraDTO origemAmostraDTO = origemAmostraMapper.toDto(origemAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrigemAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(origemAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrigemAmostra in the database
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrigemAmostra() throws Exception {
        int databaseSizeBeforeUpdate = origemAmostraRepository.findAll().size();
        origemAmostra.setId(count.incrementAndGet());

        // Create the OrigemAmostra
        OrigemAmostraDTO origemAmostraDTO = origemAmostraMapper.toDto(origemAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrigemAmostraMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(origemAmostraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrigemAmostra in the database
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrigemAmostraWithPatch() throws Exception {
        // Initialize the database
        origemAmostraRepository.saveAndFlush(origemAmostra);

        int databaseSizeBeforeUpdate = origemAmostraRepository.findAll().size();

        // Update the origemAmostra using partial update
        OrigemAmostra partialUpdatedOrigemAmostra = new OrigemAmostra();
        partialUpdatedOrigemAmostra.setId(origemAmostra.getId());

        partialUpdatedOrigemAmostra.descricao(UPDATED_DESCRICAO);

        restOrigemAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrigemAmostra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrigemAmostra))
            )
            .andExpect(status().isOk());

        // Validate the OrigemAmostra in the database
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeUpdate);
        OrigemAmostra testOrigemAmostra = origemAmostraList.get(origemAmostraList.size() - 1);
        assertThat(testOrigemAmostra.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateOrigemAmostraWithPatch() throws Exception {
        // Initialize the database
        origemAmostraRepository.saveAndFlush(origemAmostra);

        int databaseSizeBeforeUpdate = origemAmostraRepository.findAll().size();

        // Update the origemAmostra using partial update
        OrigemAmostra partialUpdatedOrigemAmostra = new OrigemAmostra();
        partialUpdatedOrigemAmostra.setId(origemAmostra.getId());

        partialUpdatedOrigemAmostra.descricao(UPDATED_DESCRICAO);

        restOrigemAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrigemAmostra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrigemAmostra))
            )
            .andExpect(status().isOk());

        // Validate the OrigemAmostra in the database
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeUpdate);
        OrigemAmostra testOrigemAmostra = origemAmostraList.get(origemAmostraList.size() - 1);
        assertThat(testOrigemAmostra.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingOrigemAmostra() throws Exception {
        int databaseSizeBeforeUpdate = origemAmostraRepository.findAll().size();
        origemAmostra.setId(count.incrementAndGet());

        // Create the OrigemAmostra
        OrigemAmostraDTO origemAmostraDTO = origemAmostraMapper.toDto(origemAmostra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrigemAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, origemAmostraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(origemAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrigemAmostra in the database
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrigemAmostra() throws Exception {
        int databaseSizeBeforeUpdate = origemAmostraRepository.findAll().size();
        origemAmostra.setId(count.incrementAndGet());

        // Create the OrigemAmostra
        OrigemAmostraDTO origemAmostraDTO = origemAmostraMapper.toDto(origemAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrigemAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(origemAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrigemAmostra in the database
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrigemAmostra() throws Exception {
        int databaseSizeBeforeUpdate = origemAmostraRepository.findAll().size();
        origemAmostra.setId(count.incrementAndGet());

        // Create the OrigemAmostra
        OrigemAmostraDTO origemAmostraDTO = origemAmostraMapper.toDto(origemAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrigemAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(origemAmostraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrigemAmostra in the database
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrigemAmostra() throws Exception {
        // Initialize the database
        origemAmostraRepository.saveAndFlush(origemAmostra);

        int databaseSizeBeforeDelete = origemAmostraRepository.findAll().size();

        // Delete the origemAmostra
        restOrigemAmostraMockMvc
            .perform(delete(ENTITY_API_URL_ID, origemAmostra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrigemAmostra> origemAmostraList = origemAmostraRepository.findAll();
        assertThat(origemAmostraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
