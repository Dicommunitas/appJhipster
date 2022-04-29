package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.Problema;
import com.operacional.controleoperacional.domain.Status;
import com.operacional.controleoperacional.domain.User;
import com.operacional.controleoperacional.repository.StatusRepository;
import com.operacional.controleoperacional.service.StatusService;
import com.operacional.controleoperacional.service.dto.StatusDTO;
import com.operacional.controleoperacional.service.mapper.StatusMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link StatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StatusResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PRAZO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRAZO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_RESOLUCAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_RESOLUCAO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StatusRepository statusRepository;

    @Mock
    private StatusRepository statusRepositoryMock;

    @Autowired
    private StatusMapper statusMapper;

    @Mock
    private StatusService statusServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatusMockMvc;

    private Status status;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Status createEntity(EntityManager em) {
        Status status = new Status()
            .descricao(DEFAULT_DESCRICAO)
            .prazo(DEFAULT_PRAZO)
            .dataResolucao(DEFAULT_DATA_RESOLUCAO);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        status.setRelator(user);
        // Add required entity
        status.setResponsavel(user);
        // Add required entity
        Problema problema;
        if (TestUtil.findAll(em, Problema.class).isEmpty()) {
            problema = ProblemaResourceIT.createEntity(em);
            em.persist(problema);
            em.flush();
        } else {
            problema = TestUtil.findAll(em, Problema.class).get(0);
        }
        status.setProblema(problema);
        return status;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Status createUpdatedEntity(EntityManager em) {
        Status status = new Status()
            .descricao(UPDATED_DESCRICAO)
            .prazo(UPDATED_PRAZO)
            .dataResolucao(UPDATED_DATA_RESOLUCAO);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        status.setRelator(user);
        // Add required entity
        status.setResponsavel(user);
        // Add required entity
        Problema problema;
        if (TestUtil.findAll(em, Problema.class).isEmpty()) {
            problema = ProblemaResourceIT.createUpdatedEntity(em);
            em.persist(problema);
            em.flush();
        } else {
            problema = TestUtil.findAll(em, Problema.class).get(0);
        }
        status.setProblema(problema);
        return status;
    }

    @BeforeEach
    public void initTest() {
        status = createEntity(em);
    }

    @Test
    @Transactional
    void createStatus() throws Exception {
        int databaseSizeBeforeCreate = statusRepository.findAll().size();
        // Create the Status
        StatusDTO statusDTO = statusMapper.toDto(status);
        restStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusDTO)))
            .andExpect(status().isCreated());

        // Validate the Status in the database
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeCreate + 1);
        Status testStatus = statusList.get(statusList.size() - 1);
        assertThat(testStatus.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testStatus.getPrazo()).isEqualTo(DEFAULT_PRAZO);
        assertThat(testStatus.getDataResolucao()).isEqualTo(DEFAULT_DATA_RESOLUCAO);
        assertThat(testStatus.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testStatus.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testStatus.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testStatus.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createStatusWithExistingId() throws Exception {
        // Create the Status with an existing ID
        status.setId(1L);
        StatusDTO statusDTO = statusMapper.toDto(status);

        int databaseSizeBeforeCreate = statusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Status in the database
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrazoIsRequired() throws Exception {
        int databaseSizeBeforeTest = statusRepository.findAll().size();
        // set the field null
        status.setPrazo(null);

        // Create the Status, which fails.
        StatusDTO statusDTO = statusMapper.toDto(status);

        restStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusDTO)))
            .andExpect(status().isBadRequest());

        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStatuses() throws Exception {
        // Initialize the database
        statusRepository.saveAndFlush(status);

        // Get all the statusList
        restStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(status.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].prazo").value(hasItem(DEFAULT_PRAZO.toString())))
            .andExpect(jsonPath("$.[*].dataResolucao").value(hasItem(DEFAULT_DATA_RESOLUCAO.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatusesWithEagerRelationshipsIsEnabled() throws Exception {
        when(statusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(statusServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatusesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(statusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(statusServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getStatus() throws Exception {
        // Initialize the database
        statusRepository.saveAndFlush(status);

        // Get the status
        restStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, status.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(status.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.prazo").value(DEFAULT_PRAZO.toString()))
            .andExpect(jsonPath("$.dataResolucao").value(DEFAULT_DATA_RESOLUCAO.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStatus() throws Exception {
        // Get the status
        restStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStatus() throws Exception {
        // Initialize the database
        statusRepository.saveAndFlush(status);

        int databaseSizeBeforeUpdate = statusRepository.findAll().size();

        // Update the status
        Status updatedStatus = statusRepository.findById(status.getId()).get();
        // Disconnect from session so that the updates on updatedStatus are not directly saved in db
        em.detach(updatedStatus);
        updatedStatus
            .descricao(UPDATED_DESCRICAO)
            .prazo(UPDATED_PRAZO)
            .dataResolucao(UPDATED_DATA_RESOLUCAO);
        StatusDTO statusDTO = statusMapper.toDto(updatedStatus);

        restStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusDTO))
            )
            .andExpect(status().isOk());

        // Validate the Status in the database
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeUpdate);
        Status testStatus = statusList.get(statusList.size() - 1);
        assertThat(testStatus.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testStatus.getPrazo()).isEqualTo(UPDATED_PRAZO);
        assertThat(testStatus.getDataResolucao()).isEqualTo(UPDATED_DATA_RESOLUCAO);
        assertThat(testStatus.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStatus.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testStatus.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testStatus.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStatus() throws Exception {
        int databaseSizeBeforeUpdate = statusRepository.findAll().size();
        status.setId(count.incrementAndGet());

        // Create the Status
        StatusDTO statusDTO = statusMapper.toDto(status);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Status in the database
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStatus() throws Exception {
        int databaseSizeBeforeUpdate = statusRepository.findAll().size();
        status.setId(count.incrementAndGet());

        // Create the Status
        StatusDTO statusDTO = statusMapper.toDto(status);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Status in the database
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStatus() throws Exception {
        int databaseSizeBeforeUpdate = statusRepository.findAll().size();
        status.setId(count.incrementAndGet());

        // Create the Status
        StatusDTO statusDTO = statusMapper.toDto(status);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Status in the database
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatusWithPatch() throws Exception {
        // Initialize the database
        statusRepository.saveAndFlush(status);

        int databaseSizeBeforeUpdate = statusRepository.findAll().size();

        // Update the status using partial update
        Status partialUpdatedStatus = new Status();
        partialUpdatedStatus.setId(status.getId());

        partialUpdatedStatus.dataResolucao(UPDATED_DATA_RESOLUCAO);

        restStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatus))
            )
            .andExpect(status().isOk());

        // Validate the Status in the database
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeUpdate);
        Status testStatus = statusList.get(statusList.size() - 1);
        assertThat(testStatus.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testStatus.getPrazo()).isEqualTo(DEFAULT_PRAZO);
        assertThat(testStatus.getDataResolucao()).isEqualTo(UPDATED_DATA_RESOLUCAO);
        assertThat(testStatus.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testStatus.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testStatus.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testStatus.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStatusWithPatch() throws Exception {
        // Initialize the database
        statusRepository.saveAndFlush(status);

        int databaseSizeBeforeUpdate = statusRepository.findAll().size();

        // Update the status using partial update
        Status partialUpdatedStatus = new Status();
        partialUpdatedStatus.setId(status.getId());

        partialUpdatedStatus
            .descricao(UPDATED_DESCRICAO)
            .prazo(UPDATED_PRAZO)
            .dataResolucao(UPDATED_DATA_RESOLUCAO);

        restStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatus))
            )
            .andExpect(status().isOk());

        // Validate the Status in the database
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeUpdate);
        Status testStatus = statusList.get(statusList.size() - 1);
        assertThat(testStatus.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testStatus.getPrazo()).isEqualTo(UPDATED_PRAZO);
        assertThat(testStatus.getDataResolucao()).isEqualTo(UPDATED_DATA_RESOLUCAO);
        assertThat(testStatus.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStatus.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testStatus.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testStatus.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStatus() throws Exception {
        int databaseSizeBeforeUpdate = statusRepository.findAll().size();
        status.setId(count.incrementAndGet());

        // Create the Status
        StatusDTO statusDTO = statusMapper.toDto(status);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, statusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Status in the database
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStatus() throws Exception {
        int databaseSizeBeforeUpdate = statusRepository.findAll().size();
        status.setId(count.incrementAndGet());

        // Create the Status
        StatusDTO statusDTO = statusMapper.toDto(status);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Status in the database
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStatus() throws Exception {
        int databaseSizeBeforeUpdate = statusRepository.findAll().size();
        status.setId(count.incrementAndGet());

        // Create the Status
        StatusDTO statusDTO = statusMapper.toDto(status);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(statusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Status in the database
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStatus() throws Exception {
        // Initialize the database
        statusRepository.saveAndFlush(status);

        int databaseSizeBeforeDelete = statusRepository.findAll().size();

        // Delete the status
        restStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, status.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Status> statusList = statusRepository.findAll();
        assertThat(statusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
