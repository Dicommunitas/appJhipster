package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.AlertaProduto;
import com.operacional.controleoperacional.repository.AlertaProdutoRepository;
import com.operacional.controleoperacional.service.dto.AlertaProdutoDTO;
import com.operacional.controleoperacional.service.mapper.AlertaProdutoMapper;
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
 * Integration tests for the {@link AlertaProdutoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlertaProdutoResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/alerta-produtos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlertaProdutoRepository alertaProdutoRepository;

    @Autowired
    private AlertaProdutoMapper alertaProdutoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlertaProdutoMockMvc;

    private AlertaProduto alertaProduto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlertaProduto createEntity(EntityManager em) {
        AlertaProduto alertaProduto = new AlertaProduto().descricao(DEFAULT_DESCRICAO);
        return alertaProduto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlertaProduto createUpdatedEntity(EntityManager em) {
        AlertaProduto alertaProduto = new AlertaProduto().descricao(UPDATED_DESCRICAO);
        return alertaProduto;
    }

    @BeforeEach
    public void initTest() {
        alertaProduto = createEntity(em);
    }

    @Test
    @Transactional
    void createAlertaProduto() throws Exception {
        int databaseSizeBeforeCreate = alertaProdutoRepository.findAll().size();
        // Create the AlertaProduto
        AlertaProdutoDTO alertaProdutoDTO = alertaProdutoMapper.toDto(alertaProduto);
        restAlertaProdutoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertaProdutoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AlertaProduto in the database
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeCreate + 1);
        AlertaProduto testAlertaProduto = alertaProdutoList.get(alertaProdutoList.size() - 1);
        assertThat(testAlertaProduto.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createAlertaProdutoWithExistingId() throws Exception {
        // Create the AlertaProduto with an existing ID
        alertaProduto.setId(1L);
        AlertaProdutoDTO alertaProdutoDTO = alertaProdutoMapper.toDto(alertaProduto);

        int databaseSizeBeforeCreate = alertaProdutoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlertaProdutoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertaProdutoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertaProduto in the database
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alertaProdutoRepository.findAll().size();
        // set the field null
        alertaProduto.setDescricao(null);

        // Create the AlertaProduto, which fails.
        AlertaProdutoDTO alertaProdutoDTO = alertaProdutoMapper.toDto(alertaProduto);

        restAlertaProdutoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertaProdutoDTO))
            )
            .andExpect(status().isBadRequest());

        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlertaProdutos() throws Exception {
        // Initialize the database
        alertaProdutoRepository.saveAndFlush(alertaProduto);

        // Get all the alertaProdutoList
        restAlertaProdutoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alertaProduto.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getAlertaProduto() throws Exception {
        // Initialize the database
        alertaProdutoRepository.saveAndFlush(alertaProduto);

        // Get the alertaProduto
        restAlertaProdutoMockMvc
            .perform(get(ENTITY_API_URL_ID, alertaProduto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alertaProduto.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingAlertaProduto() throws Exception {
        // Get the alertaProduto
        restAlertaProdutoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAlertaProduto() throws Exception {
        // Initialize the database
        alertaProdutoRepository.saveAndFlush(alertaProduto);

        int databaseSizeBeforeUpdate = alertaProdutoRepository.findAll().size();

        // Update the alertaProduto
        AlertaProduto updatedAlertaProduto = alertaProdutoRepository.findById(alertaProduto.getId()).get();
        // Disconnect from session so that the updates on updatedAlertaProduto are not directly saved in db
        em.detach(updatedAlertaProduto);
        updatedAlertaProduto.descricao(UPDATED_DESCRICAO);
        AlertaProdutoDTO alertaProdutoDTO = alertaProdutoMapper.toDto(updatedAlertaProduto);

        restAlertaProdutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertaProdutoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alertaProdutoDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlertaProduto in the database
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeUpdate);
        AlertaProduto testAlertaProduto = alertaProdutoList.get(alertaProdutoList.size() - 1);
        assertThat(testAlertaProduto.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingAlertaProduto() throws Exception {
        int databaseSizeBeforeUpdate = alertaProdutoRepository.findAll().size();
        alertaProduto.setId(count.incrementAndGet());

        // Create the AlertaProduto
        AlertaProdutoDTO alertaProdutoDTO = alertaProdutoMapper.toDto(alertaProduto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertaProdutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertaProdutoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alertaProdutoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertaProduto in the database
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlertaProduto() throws Exception {
        int databaseSizeBeforeUpdate = alertaProdutoRepository.findAll().size();
        alertaProduto.setId(count.incrementAndGet());

        // Create the AlertaProduto
        AlertaProdutoDTO alertaProdutoDTO = alertaProdutoMapper.toDto(alertaProduto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertaProdutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alertaProdutoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertaProduto in the database
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlertaProduto() throws Exception {
        int databaseSizeBeforeUpdate = alertaProdutoRepository.findAll().size();
        alertaProduto.setId(count.incrementAndGet());

        // Create the AlertaProduto
        AlertaProdutoDTO alertaProdutoDTO = alertaProdutoMapper.toDto(alertaProduto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertaProdutoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertaProdutoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlertaProduto in the database
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlertaProdutoWithPatch() throws Exception {
        // Initialize the database
        alertaProdutoRepository.saveAndFlush(alertaProduto);

        int databaseSizeBeforeUpdate = alertaProdutoRepository.findAll().size();

        // Update the alertaProduto using partial update
        AlertaProduto partialUpdatedAlertaProduto = new AlertaProduto();
        partialUpdatedAlertaProduto.setId(alertaProduto.getId());

        restAlertaProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlertaProduto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlertaProduto))
            )
            .andExpect(status().isOk());

        // Validate the AlertaProduto in the database
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeUpdate);
        AlertaProduto testAlertaProduto = alertaProdutoList.get(alertaProdutoList.size() - 1);
        assertThat(testAlertaProduto.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateAlertaProdutoWithPatch() throws Exception {
        // Initialize the database
        alertaProdutoRepository.saveAndFlush(alertaProduto);

        int databaseSizeBeforeUpdate = alertaProdutoRepository.findAll().size();

        // Update the alertaProduto using partial update
        AlertaProduto partialUpdatedAlertaProduto = new AlertaProduto();
        partialUpdatedAlertaProduto.setId(alertaProduto.getId());

        partialUpdatedAlertaProduto.descricao(UPDATED_DESCRICAO);

        restAlertaProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlertaProduto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlertaProduto))
            )
            .andExpect(status().isOk());

        // Validate the AlertaProduto in the database
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeUpdate);
        AlertaProduto testAlertaProduto = alertaProdutoList.get(alertaProdutoList.size() - 1);
        assertThat(testAlertaProduto.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingAlertaProduto() throws Exception {
        int databaseSizeBeforeUpdate = alertaProdutoRepository.findAll().size();
        alertaProduto.setId(count.incrementAndGet());

        // Create the AlertaProduto
        AlertaProdutoDTO alertaProdutoDTO = alertaProdutoMapper.toDto(alertaProduto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertaProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alertaProdutoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alertaProdutoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertaProduto in the database
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlertaProduto() throws Exception {
        int databaseSizeBeforeUpdate = alertaProdutoRepository.findAll().size();
        alertaProduto.setId(count.incrementAndGet());

        // Create the AlertaProduto
        AlertaProdutoDTO alertaProdutoDTO = alertaProdutoMapper.toDto(alertaProduto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertaProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alertaProdutoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertaProduto in the database
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlertaProduto() throws Exception {
        int databaseSizeBeforeUpdate = alertaProdutoRepository.findAll().size();
        alertaProduto.setId(count.incrementAndGet());

        // Create the AlertaProduto
        AlertaProdutoDTO alertaProdutoDTO = alertaProdutoMapper.toDto(alertaProduto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertaProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alertaProdutoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlertaProduto in the database
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlertaProduto() throws Exception {
        // Initialize the database
        alertaProdutoRepository.saveAndFlush(alertaProduto);

        int databaseSizeBeforeDelete = alertaProdutoRepository.findAll().size();

        // Delete the alertaProduto
        restAlertaProdutoMockMvc
            .perform(delete(ENTITY_API_URL_ID, alertaProduto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AlertaProduto> alertaProdutoList = alertaProdutoRepository.findAll();
        assertThat(alertaProdutoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
