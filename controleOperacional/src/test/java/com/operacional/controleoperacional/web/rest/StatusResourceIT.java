package com.operacional.controleoperacional.web.rest;

import static com.operacional.controleoperacional.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.Problema;
import com.operacional.controleoperacional.domain.Status;
import com.operacional.controleoperacional.domain.Usuario;
import com.operacional.controleoperacional.repository.StatusRepository;
import com.operacional.controleoperacional.service.dto.StatusDTO;
import com.operacional.controleoperacional.service.mapper.StatusMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link StatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StatusResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PRAZO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PRAZO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_RESOLVIDO = false;
    private static final Boolean UPDATED_RESOLVIDO = true;

    private static final String ENTITY_API_URL = "/api/statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private StatusMapper statusMapper;

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
        Status status = new Status().descricao(DEFAULT_DESCRICAO).prazo(DEFAULT_PRAZO).resolvido(DEFAULT_RESOLVIDO);
        // Add required entity
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createEntity(em);
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        status.setRelator(usuario);
        // Add required entity
        status.setResponsavel(usuario);
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
        Status status = new Status().descricao(UPDATED_DESCRICAO).prazo(UPDATED_PRAZO).resolvido(UPDATED_RESOLVIDO);
        // Add required entity
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createUpdatedEntity(em);
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        status.setRelator(usuario);
        // Add required entity
        status.setResponsavel(usuario);
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
        assertThat(testStatus.getResolvido()).isEqualTo(DEFAULT_RESOLVIDO);
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
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = statusRepository.findAll().size();
        // set the field null
        status.setDescricao(null);

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
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].prazo").value(hasItem(sameInstant(DEFAULT_PRAZO))))
            .andExpect(jsonPath("$.[*].resolvido").value(hasItem(DEFAULT_RESOLVIDO.booleanValue())));
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
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.prazo").value(sameInstant(DEFAULT_PRAZO)))
            .andExpect(jsonPath("$.resolvido").value(DEFAULT_RESOLVIDO.booleanValue()));
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
        updatedStatus.descricao(UPDATED_DESCRICAO).prazo(UPDATED_PRAZO).resolvido(UPDATED_RESOLVIDO);
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
        assertThat(testStatus.getResolvido()).isEqualTo(UPDATED_RESOLVIDO);
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

        partialUpdatedStatus.resolvido(UPDATED_RESOLVIDO);

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
        assertThat(testStatus.getResolvido()).isEqualTo(UPDATED_RESOLVIDO);
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

        partialUpdatedStatus.descricao(UPDATED_DESCRICAO).prazo(UPDATED_PRAZO).resolvido(UPDATED_RESOLVIDO);

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
        assertThat(testStatus.getResolvido()).isEqualTo(UPDATED_RESOLVIDO);
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
