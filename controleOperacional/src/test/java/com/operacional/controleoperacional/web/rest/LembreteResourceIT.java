package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.Lembrete;
import com.operacional.controleoperacional.repository.LembreteRepository;
import com.operacional.controleoperacional.service.LembreteService;
import com.operacional.controleoperacional.service.dto.LembreteDTO;
import com.operacional.controleoperacional.service.mapper.LembreteMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link LembreteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LembreteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/lembretes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LembreteRepository lembreteRepository;

    @Mock
    private LembreteRepository lembreteRepositoryMock;

    @Autowired
    private LembreteMapper lembreteMapper;

    @Mock
    private LembreteService lembreteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLembreteMockMvc;

    private Lembrete lembrete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lembrete createEntity(EntityManager em) {
        Lembrete lembrete = new Lembrete()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return lembrete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lembrete createUpdatedEntity(EntityManager em) {
        Lembrete lembrete = new Lembrete()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return lembrete;
    }

    @BeforeEach
    public void initTest() {
        lembrete = createEntity(em);
    }

    @Test
    @Transactional
    void createLembrete() throws Exception {
        int databaseSizeBeforeCreate = lembreteRepository.findAll().size();
        // Create the Lembrete
        LembreteDTO lembreteDTO = lembreteMapper.toDto(lembrete);
        restLembreteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lembreteDTO)))
            .andExpect(status().isCreated());

        // Validate the Lembrete in the database
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeCreate + 1);
        Lembrete testLembrete = lembreteList.get(lembreteList.size() - 1);
        assertThat(testLembrete.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testLembrete.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testLembrete.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLembrete.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testLembrete.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testLembrete.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createLembreteWithExistingId() throws Exception {
        // Create the Lembrete with an existing ID
        lembrete.setId(1L);
        LembreteDTO lembreteDTO = lembreteMapper.toDto(lembrete);

        int databaseSizeBeforeCreate = lembreteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLembreteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lembreteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lembrete in the database
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lembreteRepository.findAll().size();
        // set the field null
        lembrete.setNome(null);

        // Create the Lembrete, which fails.
        LembreteDTO lembreteDTO = lembreteMapper.toDto(lembrete);

        restLembreteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lembreteDTO)))
            .andExpect(status().isBadRequest());

        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLembretes() throws Exception {
        // Initialize the database
        lembreteRepository.saveAndFlush(lembrete);

        // Get all the lembreteList
        restLembreteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lembrete.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLembretesWithEagerRelationshipsIsEnabled() throws Exception {
        when(lembreteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLembreteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lembreteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLembretesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(lembreteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLembreteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lembreteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLembrete() throws Exception {
        // Initialize the database
        lembreteRepository.saveAndFlush(lembrete);

        // Get the lembrete
        restLembreteMockMvc
            .perform(get(ENTITY_API_URL_ID, lembrete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lembrete.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLembrete() throws Exception {
        // Get the lembrete
        restLembreteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLembrete() throws Exception {
        // Initialize the database
        lembreteRepository.saveAndFlush(lembrete);

        int databaseSizeBeforeUpdate = lembreteRepository.findAll().size();

        // Update the lembrete
        Lembrete updatedLembrete = lembreteRepository.findById(lembrete.getId()).get();
        // Disconnect from session so that the updates on updatedLembrete are not directly saved in db
        em.detach(updatedLembrete);
        updatedLembrete
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        LembreteDTO lembreteDTO = lembreteMapper.toDto(updatedLembrete);

        restLembreteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lembreteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lembreteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Lembrete in the database
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeUpdate);
        Lembrete testLembrete = lembreteList.get(lembreteList.size() - 1);
        assertThat(testLembrete.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLembrete.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testLembrete.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLembrete.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLembrete.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testLembrete.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingLembrete() throws Exception {
        int databaseSizeBeforeUpdate = lembreteRepository.findAll().size();
        lembrete.setId(count.incrementAndGet());

        // Create the Lembrete
        LembreteDTO lembreteDTO = lembreteMapper.toDto(lembrete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLembreteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lembreteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lembreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lembrete in the database
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLembrete() throws Exception {
        int databaseSizeBeforeUpdate = lembreteRepository.findAll().size();
        lembrete.setId(count.incrementAndGet());

        // Create the Lembrete
        LembreteDTO lembreteDTO = lembreteMapper.toDto(lembrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLembreteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lembreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lembrete in the database
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLembrete() throws Exception {
        int databaseSizeBeforeUpdate = lembreteRepository.findAll().size();
        lembrete.setId(count.incrementAndGet());

        // Create the Lembrete
        LembreteDTO lembreteDTO = lembreteMapper.toDto(lembrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLembreteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lembreteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lembrete in the database
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLembreteWithPatch() throws Exception {
        // Initialize the database
        lembreteRepository.saveAndFlush(lembrete);

        int databaseSizeBeforeUpdate = lembreteRepository.findAll().size();

        // Update the lembrete using partial update
        Lembrete partialUpdatedLembrete = new Lembrete();
        partialUpdatedLembrete.setId(lembrete.getId());

        partialUpdatedLembrete
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restLembreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLembrete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLembrete))
            )
            .andExpect(status().isOk());

        // Validate the Lembrete in the database
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeUpdate);
        Lembrete testLembrete = lembreteList.get(lembreteList.size() - 1);
        assertThat(testLembrete.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLembrete.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testLembrete.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLembrete.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLembrete.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testLembrete.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateLembreteWithPatch() throws Exception {
        // Initialize the database
        lembreteRepository.saveAndFlush(lembrete);

        int databaseSizeBeforeUpdate = lembreteRepository.findAll().size();

        // Update the lembrete using partial update
        Lembrete partialUpdatedLembrete = new Lembrete();
        partialUpdatedLembrete.setId(lembrete.getId());

        partialUpdatedLembrete
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restLembreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLembrete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLembrete))
            )
            .andExpect(status().isOk());

        // Validate the Lembrete in the database
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeUpdate);
        Lembrete testLembrete = lembreteList.get(lembreteList.size() - 1);
        assertThat(testLembrete.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLembrete.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testLembrete.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLembrete.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLembrete.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testLembrete.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingLembrete() throws Exception {
        int databaseSizeBeforeUpdate = lembreteRepository.findAll().size();
        lembrete.setId(count.incrementAndGet());

        // Create the Lembrete
        LembreteDTO lembreteDTO = lembreteMapper.toDto(lembrete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLembreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lembreteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lembreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lembrete in the database
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLembrete() throws Exception {
        int databaseSizeBeforeUpdate = lembreteRepository.findAll().size();
        lembrete.setId(count.incrementAndGet());

        // Create the Lembrete
        LembreteDTO lembreteDTO = lembreteMapper.toDto(lembrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLembreteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lembreteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lembrete in the database
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLembrete() throws Exception {
        int databaseSizeBeforeUpdate = lembreteRepository.findAll().size();
        lembrete.setId(count.incrementAndGet());

        // Create the Lembrete
        LembreteDTO lembreteDTO = lembreteMapper.toDto(lembrete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLembreteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lembreteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lembrete in the database
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLembrete() throws Exception {
        // Initialize the database
        lembreteRepository.saveAndFlush(lembrete);

        int databaseSizeBeforeDelete = lembreteRepository.findAll().size();

        // Delete the lembrete
        restLembreteMockMvc
            .perform(delete(ENTITY_API_URL_ID, lembrete.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lembrete> lembreteList = lembreteRepository.findAll();
        assertThat(lembreteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
