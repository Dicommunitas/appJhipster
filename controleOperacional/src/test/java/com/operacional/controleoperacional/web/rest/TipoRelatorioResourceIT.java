package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.TipoRelatorio;
import com.operacional.controleoperacional.repository.TipoRelatorioRepository;
import com.operacional.controleoperacional.service.TipoRelatorioService;
import com.operacional.controleoperacional.service.dto.TipoRelatorioDTO;
import com.operacional.controleoperacional.service.mapper.TipoRelatorioMapper;
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
 * Integration tests for the {@link TipoRelatorioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TipoRelatorioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-relatorios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoRelatorioRepository tipoRelatorioRepository;

    @Mock
    private TipoRelatorioRepository tipoRelatorioRepositoryMock;

    @Autowired
    private TipoRelatorioMapper tipoRelatorioMapper;

    @Mock
    private TipoRelatorioService tipoRelatorioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoRelatorioMockMvc;

    private TipoRelatorio tipoRelatorio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoRelatorio createEntity(EntityManager em) {
        TipoRelatorio tipoRelatorio = new TipoRelatorio().nome(DEFAULT_NOME);
        return tipoRelatorio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoRelatorio createUpdatedEntity(EntityManager em) {
        TipoRelatorio tipoRelatorio = new TipoRelatorio().nome(UPDATED_NOME);
        return tipoRelatorio;
    }

    @BeforeEach
    public void initTest() {
        tipoRelatorio = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoRelatorio() throws Exception {
        int databaseSizeBeforeCreate = tipoRelatorioRepository.findAll().size();
        // Create the TipoRelatorio
        TipoRelatorioDTO tipoRelatorioDTO = tipoRelatorioMapper.toDto(tipoRelatorio);
        restTipoRelatorioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoRelatorioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TipoRelatorio in the database
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeCreate + 1);
        TipoRelatorio testTipoRelatorio = tipoRelatorioList.get(tipoRelatorioList.size() - 1);
        assertThat(testTipoRelatorio.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void createTipoRelatorioWithExistingId() throws Exception {
        // Create the TipoRelatorio with an existing ID
        tipoRelatorio.setId(1L);
        TipoRelatorioDTO tipoRelatorioDTO = tipoRelatorioMapper.toDto(tipoRelatorio);

        int databaseSizeBeforeCreate = tipoRelatorioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoRelatorioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoRelatorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoRelatorio in the database
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoRelatorioRepository.findAll().size();
        // set the field null
        tipoRelatorio.setNome(null);

        // Create the TipoRelatorio, which fails.
        TipoRelatorioDTO tipoRelatorioDTO = tipoRelatorioMapper.toDto(tipoRelatorio);

        restTipoRelatorioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoRelatorioDTO))
            )
            .andExpect(status().isBadRequest());

        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoRelatorios() throws Exception {
        // Initialize the database
        tipoRelatorioRepository.saveAndFlush(tipoRelatorio);

        // Get all the tipoRelatorioList
        restTipoRelatorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoRelatorio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTipoRelatoriosWithEagerRelationshipsIsEnabled() throws Exception {
        when(tipoRelatorioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTipoRelatorioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tipoRelatorioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTipoRelatoriosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tipoRelatorioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTipoRelatorioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tipoRelatorioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTipoRelatorio() throws Exception {
        // Initialize the database
        tipoRelatorioRepository.saveAndFlush(tipoRelatorio);

        // Get the tipoRelatorio
        restTipoRelatorioMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoRelatorio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoRelatorio.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingTipoRelatorio() throws Exception {
        // Get the tipoRelatorio
        restTipoRelatorioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoRelatorio() throws Exception {
        // Initialize the database
        tipoRelatorioRepository.saveAndFlush(tipoRelatorio);

        int databaseSizeBeforeUpdate = tipoRelatorioRepository.findAll().size();

        // Update the tipoRelatorio
        TipoRelatorio updatedTipoRelatorio = tipoRelatorioRepository.findById(tipoRelatorio.getId()).get();
        // Disconnect from session so that the updates on updatedTipoRelatorio are not directly saved in db
        em.detach(updatedTipoRelatorio);
        updatedTipoRelatorio.nome(UPDATED_NOME);
        TipoRelatorioDTO tipoRelatorioDTO = tipoRelatorioMapper.toDto(updatedTipoRelatorio);

        restTipoRelatorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoRelatorioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoRelatorioDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoRelatorio in the database
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeUpdate);
        TipoRelatorio testTipoRelatorio = tipoRelatorioList.get(tipoRelatorioList.size() - 1);
        assertThat(testTipoRelatorio.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void putNonExistingTipoRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = tipoRelatorioRepository.findAll().size();
        tipoRelatorio.setId(count.incrementAndGet());

        // Create the TipoRelatorio
        TipoRelatorioDTO tipoRelatorioDTO = tipoRelatorioMapper.toDto(tipoRelatorio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoRelatorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoRelatorioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoRelatorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoRelatorio in the database
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = tipoRelatorioRepository.findAll().size();
        tipoRelatorio.setId(count.incrementAndGet());

        // Create the TipoRelatorio
        TipoRelatorioDTO tipoRelatorioDTO = tipoRelatorioMapper.toDto(tipoRelatorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoRelatorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoRelatorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoRelatorio in the database
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = tipoRelatorioRepository.findAll().size();
        tipoRelatorio.setId(count.incrementAndGet());

        // Create the TipoRelatorio
        TipoRelatorioDTO tipoRelatorioDTO = tipoRelatorioMapper.toDto(tipoRelatorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoRelatorioMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoRelatorioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoRelatorio in the database
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoRelatorioWithPatch() throws Exception {
        // Initialize the database
        tipoRelatorioRepository.saveAndFlush(tipoRelatorio);

        int databaseSizeBeforeUpdate = tipoRelatorioRepository.findAll().size();

        // Update the tipoRelatorio using partial update
        TipoRelatorio partialUpdatedTipoRelatorio = new TipoRelatorio();
        partialUpdatedTipoRelatorio.setId(tipoRelatorio.getId());

        partialUpdatedTipoRelatorio.nome(UPDATED_NOME);

        restTipoRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoRelatorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoRelatorio))
            )
            .andExpect(status().isOk());

        // Validate the TipoRelatorio in the database
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeUpdate);
        TipoRelatorio testTipoRelatorio = tipoRelatorioList.get(tipoRelatorioList.size() - 1);
        assertThat(testTipoRelatorio.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void fullUpdateTipoRelatorioWithPatch() throws Exception {
        // Initialize the database
        tipoRelatorioRepository.saveAndFlush(tipoRelatorio);

        int databaseSizeBeforeUpdate = tipoRelatorioRepository.findAll().size();

        // Update the tipoRelatorio using partial update
        TipoRelatorio partialUpdatedTipoRelatorio = new TipoRelatorio();
        partialUpdatedTipoRelatorio.setId(tipoRelatorio.getId());

        partialUpdatedTipoRelatorio.nome(UPDATED_NOME);

        restTipoRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoRelatorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoRelatorio))
            )
            .andExpect(status().isOk());

        // Validate the TipoRelatorio in the database
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeUpdate);
        TipoRelatorio testTipoRelatorio = tipoRelatorioList.get(tipoRelatorioList.size() - 1);
        assertThat(testTipoRelatorio.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void patchNonExistingTipoRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = tipoRelatorioRepository.findAll().size();
        tipoRelatorio.setId(count.incrementAndGet());

        // Create the TipoRelatorio
        TipoRelatorioDTO tipoRelatorioDTO = tipoRelatorioMapper.toDto(tipoRelatorio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoRelatorioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoRelatorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoRelatorio in the database
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = tipoRelatorioRepository.findAll().size();
        tipoRelatorio.setId(count.incrementAndGet());

        // Create the TipoRelatorio
        TipoRelatorioDTO tipoRelatorioDTO = tipoRelatorioMapper.toDto(tipoRelatorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoRelatorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoRelatorio in the database
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = tipoRelatorioRepository.findAll().size();
        tipoRelatorio.setId(count.incrementAndGet());

        // Create the TipoRelatorio
        TipoRelatorioDTO tipoRelatorioDTO = tipoRelatorioMapper.toDto(tipoRelatorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoRelatorioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoRelatorio in the database
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoRelatorio() throws Exception {
        // Initialize the database
        tipoRelatorioRepository.saveAndFlush(tipoRelatorio);

        int databaseSizeBeforeDelete = tipoRelatorioRepository.findAll().size();

        // Delete the tipoRelatorio
        restTipoRelatorioMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoRelatorio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoRelatorio> tipoRelatorioList = tipoRelatorioRepository.findAll();
        assertThat(tipoRelatorioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
