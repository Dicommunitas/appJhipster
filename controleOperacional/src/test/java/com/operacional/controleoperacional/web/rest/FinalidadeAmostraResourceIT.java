package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.Amostra;
import com.operacional.controleoperacional.domain.FinalidadeAmostra;
import com.operacional.controleoperacional.domain.TipoFinalidadeAmostra;
import com.operacional.controleoperacional.repository.FinalidadeAmostraRepository;
import com.operacional.controleoperacional.service.FinalidadeAmostraService;
import com.operacional.controleoperacional.service.dto.FinalidadeAmostraDTO;
import com.operacional.controleoperacional.service.mapper.FinalidadeAmostraMapper;
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
 * Integration tests for the {@link FinalidadeAmostraResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FinalidadeAmostraResourceIT {

    private static final Integer DEFAULT_LACRE = 1;
    private static final Integer UPDATED_LACRE = 2;

    private static final String ENTITY_API_URL = "/api/finalidade-amostras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FinalidadeAmostraRepository finalidadeAmostraRepository;

    @Mock
    private FinalidadeAmostraRepository finalidadeAmostraRepositoryMock;

    @Autowired
    private FinalidadeAmostraMapper finalidadeAmostraMapper;

    @Mock
    private FinalidadeAmostraService finalidadeAmostraServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFinalidadeAmostraMockMvc;

    private FinalidadeAmostra finalidadeAmostra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinalidadeAmostra createEntity(EntityManager em) {
        FinalidadeAmostra finalidadeAmostra = new FinalidadeAmostra().lacre(DEFAULT_LACRE);
        // Add required entity
        TipoFinalidadeAmostra tipoFinalidadeAmostra;
        if (TestUtil.findAll(em, TipoFinalidadeAmostra.class).isEmpty()) {
            tipoFinalidadeAmostra = TipoFinalidadeAmostraResourceIT.createEntity(em);
            em.persist(tipoFinalidadeAmostra);
            em.flush();
        } else {
            tipoFinalidadeAmostra = TestUtil.findAll(em, TipoFinalidadeAmostra.class).get(0);
        }
        finalidadeAmostra.setTipoFinalidadeAmostra(tipoFinalidadeAmostra);
        // Add required entity
        Amostra amostra;
        if (TestUtil.findAll(em, Amostra.class).isEmpty()) {
            amostra = AmostraResourceIT.createEntity(em);
            em.persist(amostra);
            em.flush();
        } else {
            amostra = TestUtil.findAll(em, Amostra.class).get(0);
        }
        finalidadeAmostra.setAmostra(amostra);
        return finalidadeAmostra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinalidadeAmostra createUpdatedEntity(EntityManager em) {
        FinalidadeAmostra finalidadeAmostra = new FinalidadeAmostra().lacre(UPDATED_LACRE);
        // Add required entity
        TipoFinalidadeAmostra tipoFinalidadeAmostra;
        if (TestUtil.findAll(em, TipoFinalidadeAmostra.class).isEmpty()) {
            tipoFinalidadeAmostra = TipoFinalidadeAmostraResourceIT.createUpdatedEntity(em);
            em.persist(tipoFinalidadeAmostra);
            em.flush();
        } else {
            tipoFinalidadeAmostra = TestUtil.findAll(em, TipoFinalidadeAmostra.class).get(0);
        }
        finalidadeAmostra.setTipoFinalidadeAmostra(tipoFinalidadeAmostra);
        // Add required entity
        Amostra amostra;
        if (TestUtil.findAll(em, Amostra.class).isEmpty()) {
            amostra = AmostraResourceIT.createUpdatedEntity(em);
            em.persist(amostra);
            em.flush();
        } else {
            amostra = TestUtil.findAll(em, Amostra.class).get(0);
        }
        finalidadeAmostra.setAmostra(amostra);
        return finalidadeAmostra;
    }

    @BeforeEach
    public void initTest() {
        finalidadeAmostra = createEntity(em);
    }

    @Test
    @Transactional
    void createFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeCreate = finalidadeAmostraRepository.findAll().size();
        // Create the FinalidadeAmostra
        FinalidadeAmostraDTO finalidadeAmostraDTO = finalidadeAmostraMapper.toDto(finalidadeAmostra);
        restFinalidadeAmostraMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finalidadeAmostraDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FinalidadeAmostra in the database
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeCreate + 1);
        FinalidadeAmostra testFinalidadeAmostra = finalidadeAmostraList.get(finalidadeAmostraList.size() - 1);
        assertThat(testFinalidadeAmostra.getLacre()).isEqualTo(DEFAULT_LACRE);
    }

    @Test
    @Transactional
    void createFinalidadeAmostraWithExistingId() throws Exception {
        // Create the FinalidadeAmostra with an existing ID
        finalidadeAmostra.setId(1L);
        FinalidadeAmostraDTO finalidadeAmostraDTO = finalidadeAmostraMapper.toDto(finalidadeAmostra);

        int databaseSizeBeforeCreate = finalidadeAmostraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinalidadeAmostraMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalidadeAmostra in the database
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFinalidadeAmostras() throws Exception {
        // Initialize the database
        finalidadeAmostraRepository.saveAndFlush(finalidadeAmostra);

        // Get all the finalidadeAmostraList
        restFinalidadeAmostraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finalidadeAmostra.getId().intValue())))
            .andExpect(jsonPath("$.[*].lacre").value(hasItem(DEFAULT_LACRE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFinalidadeAmostrasWithEagerRelationshipsIsEnabled() throws Exception {
        when(finalidadeAmostraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFinalidadeAmostraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(finalidadeAmostraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFinalidadeAmostrasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(finalidadeAmostraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFinalidadeAmostraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(finalidadeAmostraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFinalidadeAmostra() throws Exception {
        // Initialize the database
        finalidadeAmostraRepository.saveAndFlush(finalidadeAmostra);

        // Get the finalidadeAmostra
        restFinalidadeAmostraMockMvc
            .perform(get(ENTITY_API_URL_ID, finalidadeAmostra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(finalidadeAmostra.getId().intValue()))
            .andExpect(jsonPath("$.lacre").value(DEFAULT_LACRE));
    }

    @Test
    @Transactional
    void getNonExistingFinalidadeAmostra() throws Exception {
        // Get the finalidadeAmostra
        restFinalidadeAmostraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFinalidadeAmostra() throws Exception {
        // Initialize the database
        finalidadeAmostraRepository.saveAndFlush(finalidadeAmostra);

        int databaseSizeBeforeUpdate = finalidadeAmostraRepository.findAll().size();

        // Update the finalidadeAmostra
        FinalidadeAmostra updatedFinalidadeAmostra = finalidadeAmostraRepository.findById(finalidadeAmostra.getId()).get();
        // Disconnect from session so that the updates on updatedFinalidadeAmostra are not directly saved in db
        em.detach(updatedFinalidadeAmostra);
        updatedFinalidadeAmostra.lacre(UPDATED_LACRE);
        FinalidadeAmostraDTO finalidadeAmostraDTO = finalidadeAmostraMapper.toDto(updatedFinalidadeAmostra);

        restFinalidadeAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, finalidadeAmostraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finalidadeAmostraDTO))
            )
            .andExpect(status().isOk());

        // Validate the FinalidadeAmostra in the database
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
        FinalidadeAmostra testFinalidadeAmostra = finalidadeAmostraList.get(finalidadeAmostraList.size() - 1);
        assertThat(testFinalidadeAmostra.getLacre()).isEqualTo(UPDATED_LACRE);
    }

    @Test
    @Transactional
    void putNonExistingFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = finalidadeAmostraRepository.findAll().size();
        finalidadeAmostra.setId(count.incrementAndGet());

        // Create the FinalidadeAmostra
        FinalidadeAmostraDTO finalidadeAmostraDTO = finalidadeAmostraMapper.toDto(finalidadeAmostra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinalidadeAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, finalidadeAmostraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalidadeAmostra in the database
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = finalidadeAmostraRepository.findAll().size();
        finalidadeAmostra.setId(count.incrementAndGet());

        // Create the FinalidadeAmostra
        FinalidadeAmostraDTO finalidadeAmostraDTO = finalidadeAmostraMapper.toDto(finalidadeAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalidadeAmostraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalidadeAmostra in the database
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = finalidadeAmostraRepository.findAll().size();
        finalidadeAmostra.setId(count.incrementAndGet());

        // Create the FinalidadeAmostra
        FinalidadeAmostraDTO finalidadeAmostraDTO = finalidadeAmostraMapper.toDto(finalidadeAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalidadeAmostraMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(finalidadeAmostraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinalidadeAmostra in the database
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFinalidadeAmostraWithPatch() throws Exception {
        // Initialize the database
        finalidadeAmostraRepository.saveAndFlush(finalidadeAmostra);

        int databaseSizeBeforeUpdate = finalidadeAmostraRepository.findAll().size();

        // Update the finalidadeAmostra using partial update
        FinalidadeAmostra partialUpdatedFinalidadeAmostra = new FinalidadeAmostra();
        partialUpdatedFinalidadeAmostra.setId(finalidadeAmostra.getId());

        partialUpdatedFinalidadeAmostra.lacre(UPDATED_LACRE);

        restFinalidadeAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinalidadeAmostra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinalidadeAmostra))
            )
            .andExpect(status().isOk());

        // Validate the FinalidadeAmostra in the database
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
        FinalidadeAmostra testFinalidadeAmostra = finalidadeAmostraList.get(finalidadeAmostraList.size() - 1);
        assertThat(testFinalidadeAmostra.getLacre()).isEqualTo(UPDATED_LACRE);
    }

    @Test
    @Transactional
    void fullUpdateFinalidadeAmostraWithPatch() throws Exception {
        // Initialize the database
        finalidadeAmostraRepository.saveAndFlush(finalidadeAmostra);

        int databaseSizeBeforeUpdate = finalidadeAmostraRepository.findAll().size();

        // Update the finalidadeAmostra using partial update
        FinalidadeAmostra partialUpdatedFinalidadeAmostra = new FinalidadeAmostra();
        partialUpdatedFinalidadeAmostra.setId(finalidadeAmostra.getId());

        partialUpdatedFinalidadeAmostra.lacre(UPDATED_LACRE);

        restFinalidadeAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinalidadeAmostra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinalidadeAmostra))
            )
            .andExpect(status().isOk());

        // Validate the FinalidadeAmostra in the database
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
        FinalidadeAmostra testFinalidadeAmostra = finalidadeAmostraList.get(finalidadeAmostraList.size() - 1);
        assertThat(testFinalidadeAmostra.getLacre()).isEqualTo(UPDATED_LACRE);
    }

    @Test
    @Transactional
    void patchNonExistingFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = finalidadeAmostraRepository.findAll().size();
        finalidadeAmostra.setId(count.incrementAndGet());

        // Create the FinalidadeAmostra
        FinalidadeAmostraDTO finalidadeAmostraDTO = finalidadeAmostraMapper.toDto(finalidadeAmostra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinalidadeAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, finalidadeAmostraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(finalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalidadeAmostra in the database
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = finalidadeAmostraRepository.findAll().size();
        finalidadeAmostra.setId(count.incrementAndGet());

        // Create the FinalidadeAmostra
        FinalidadeAmostraDTO finalidadeAmostraDTO = finalidadeAmostraMapper.toDto(finalidadeAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalidadeAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(finalidadeAmostraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinalidadeAmostra in the database
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFinalidadeAmostra() throws Exception {
        int databaseSizeBeforeUpdate = finalidadeAmostraRepository.findAll().size();
        finalidadeAmostra.setId(count.incrementAndGet());

        // Create the FinalidadeAmostra
        FinalidadeAmostraDTO finalidadeAmostraDTO = finalidadeAmostraMapper.toDto(finalidadeAmostra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinalidadeAmostraMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(finalidadeAmostraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinalidadeAmostra in the database
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFinalidadeAmostra() throws Exception {
        // Initialize the database
        finalidadeAmostraRepository.saveAndFlush(finalidadeAmostra);

        int databaseSizeBeforeDelete = finalidadeAmostraRepository.findAll().size();

        // Delete the finalidadeAmostra
        restFinalidadeAmostraMockMvc
            .perform(delete(ENTITY_API_URL_ID, finalidadeAmostra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FinalidadeAmostra> finalidadeAmostraList = finalidadeAmostraRepository.findAll();
        assertThat(finalidadeAmostraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
