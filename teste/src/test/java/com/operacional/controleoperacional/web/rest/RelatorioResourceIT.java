package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.Relatorio;
import com.operacional.controleoperacional.domain.TipoRelatorio;
import com.operacional.controleoperacional.domain.User;
import com.operacional.controleoperacional.repository.RelatorioRepository;
import com.operacional.controleoperacional.service.RelatorioService;
import com.operacional.controleoperacional.service.criteria.RelatorioCriteria;
import com.operacional.controleoperacional.service.dto.RelatorioDTO;
import com.operacional.controleoperacional.service.mapper.RelatorioMapper;
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
 * Integration tests for the {@link RelatorioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RelatorioResourceIT {

    private static final Instant DEFAULT_DATA_HORA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RELATO = "AAAAAAAAAA";
    private static final String UPDATED_RELATO = "BBBBBBBBBB";

    private static final String DEFAULT_LINKS_EXTERNOS = "AAAAAAAAAA";
    private static final String UPDATED_LINKS_EXTERNOS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/relatorios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Mock
    private RelatorioRepository relatorioRepositoryMock;

    @Autowired
    private RelatorioMapper relatorioMapper;

    @Mock
    private RelatorioService relatorioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRelatorioMockMvc;

    private Relatorio relatorio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relatorio createEntity(EntityManager em) {
        Relatorio relatorio = new Relatorio().dataHora(DEFAULT_DATA_HORA).relato(DEFAULT_RELATO).linksExternos(DEFAULT_LINKS_EXTERNOS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        relatorio.setResponsavel(user);
        return relatorio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relatorio createUpdatedEntity(EntityManager em) {
        Relatorio relatorio = new Relatorio().dataHora(UPDATED_DATA_HORA).relato(UPDATED_RELATO).linksExternos(UPDATED_LINKS_EXTERNOS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        relatorio.setResponsavel(user);
        return relatorio;
    }

    @BeforeEach
    public void initTest() {
        relatorio = createEntity(em);
    }

    @Test
    @Transactional
    void createRelatorio() throws Exception {
        int databaseSizeBeforeCreate = relatorioRepository.findAll().size();
        // Create the Relatorio
        RelatorioDTO relatorioDTO = relatorioMapper.toDto(relatorio);
        restRelatorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatorioDTO)))
            .andExpect(status().isCreated());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeCreate + 1);
        Relatorio testRelatorio = relatorioList.get(relatorioList.size() - 1);
        assertThat(testRelatorio.getDataHora()).isEqualTo(DEFAULT_DATA_HORA);
        assertThat(testRelatorio.getRelato()).isEqualTo(DEFAULT_RELATO);
        assertThat(testRelatorio.getLinksExternos()).isEqualTo(DEFAULT_LINKS_EXTERNOS);
    }

    @Test
    @Transactional
    void createRelatorioWithExistingId() throws Exception {
        // Create the Relatorio with an existing ID
        relatorio.setId(1L);
        RelatorioDTO relatorioDTO = relatorioMapper.toDto(relatorio);

        int databaseSizeBeforeCreate = relatorioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelatorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatorioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataHoraIsRequired() throws Exception {
        int databaseSizeBeforeTest = relatorioRepository.findAll().size();
        // set the field null
        relatorio.setDataHora(null);

        // Create the Relatorio, which fails.
        RelatorioDTO relatorioDTO = relatorioMapper.toDto(relatorio);

        restRelatorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatorioDTO)))
            .andExpect(status().isBadRequest());

        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRelatorios() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList
        restRelatorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatorio.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHora").value(hasItem(DEFAULT_DATA_HORA.toString())))
            .andExpect(jsonPath("$.[*].relato").value(hasItem(DEFAULT_RELATO.toString())))
            .andExpect(jsonPath("$.[*].linksExternos").value(hasItem(DEFAULT_LINKS_EXTERNOS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRelatoriosWithEagerRelationshipsIsEnabled() throws Exception {
        when(relatorioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRelatorioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(relatorioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRelatoriosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(relatorioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRelatorioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(relatorioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getRelatorio() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get the relatorio
        restRelatorioMockMvc
            .perform(get(ENTITY_API_URL_ID, relatorio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(relatorio.getId().intValue()))
            .andExpect(jsonPath("$.dataHora").value(DEFAULT_DATA_HORA.toString()))
            .andExpect(jsonPath("$.relato").value(DEFAULT_RELATO.toString()))
            .andExpect(jsonPath("$.linksExternos").value(DEFAULT_LINKS_EXTERNOS.toString()));
    }

    @Test
    @Transactional
    void getRelatoriosByIdFiltering() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        Long id = relatorio.getId();

        defaultRelatorioShouldBeFound("id.equals=" + id);
        defaultRelatorioShouldNotBeFound("id.notEquals=" + id);

        defaultRelatorioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRelatorioShouldNotBeFound("id.greaterThan=" + id);

        defaultRelatorioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRelatorioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRelatoriosByDataHoraIsEqualToSomething() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList where dataHora equals to DEFAULT_DATA_HORA
        defaultRelatorioShouldBeFound("dataHora.equals=" + DEFAULT_DATA_HORA);

        // Get all the relatorioList where dataHora equals to UPDATED_DATA_HORA
        defaultRelatorioShouldNotBeFound("dataHora.equals=" + UPDATED_DATA_HORA);
    }

    @Test
    @Transactional
    void getAllRelatoriosByDataHoraIsNotEqualToSomething() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList where dataHora not equals to DEFAULT_DATA_HORA
        defaultRelatorioShouldNotBeFound("dataHora.notEquals=" + DEFAULT_DATA_HORA);

        // Get all the relatorioList where dataHora not equals to UPDATED_DATA_HORA
        defaultRelatorioShouldBeFound("dataHora.notEquals=" + UPDATED_DATA_HORA);
    }

    @Test
    @Transactional
    void getAllRelatoriosByDataHoraIsInShouldWork() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList where dataHora in DEFAULT_DATA_HORA or UPDATED_DATA_HORA
        defaultRelatorioShouldBeFound("dataHora.in=" + DEFAULT_DATA_HORA + "," + UPDATED_DATA_HORA);

        // Get all the relatorioList where dataHora equals to UPDATED_DATA_HORA
        defaultRelatorioShouldNotBeFound("dataHora.in=" + UPDATED_DATA_HORA);
    }

    @Test
    @Transactional
    void getAllRelatoriosByDataHoraIsNullOrNotNull() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList where dataHora is not null
        defaultRelatorioShouldBeFound("dataHora.specified=true");

        // Get all the relatorioList where dataHora is null
        defaultRelatorioShouldNotBeFound("dataHora.specified=false");
    }

    @Test
    @Transactional
    void getAllRelatoriosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);
        TipoRelatorio tipo;
        if (TestUtil.findAll(em, TipoRelatorio.class).isEmpty()) {
            tipo = TipoRelatorioResourceIT.createEntity(em);
            em.persist(tipo);
            em.flush();
        } else {
            tipo = TestUtil.findAll(em, TipoRelatorio.class).get(0);
        }
        em.persist(tipo);
        em.flush();
        relatorio.setTipo(tipo);
        relatorioRepository.saveAndFlush(relatorio);
        Long tipoId = tipo.getId();

        // Get all the relatorioList where tipo equals to tipoId
        defaultRelatorioShouldBeFound("tipoId.equals=" + tipoId);

        // Get all the relatorioList where tipo equals to (tipoId + 1)
        defaultRelatorioShouldNotBeFound("tipoId.equals=" + (tipoId + 1));
    }

    @Test
    @Transactional
    void getAllRelatoriosByResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);
        User responsavel;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            responsavel = UserResourceIT.createEntity(em);
            em.persist(responsavel);
            em.flush();
        } else {
            responsavel = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(responsavel);
        em.flush();
        relatorio.setResponsavel(responsavel);
        relatorioRepository.saveAndFlush(relatorio);
        Long responsavelId = responsavel.getId();

        // Get all the relatorioList where responsavel equals to responsavelId
        defaultRelatorioShouldBeFound("responsavelId.equals=" + responsavelId);

        // Get all the relatorioList where responsavel equals to (responsavelId + 1)
        defaultRelatorioShouldNotBeFound("responsavelId.equals=" + (responsavelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRelatorioShouldBeFound(String filter) throws Exception {
        restRelatorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatorio.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataHora").value(hasItem(DEFAULT_DATA_HORA.toString())))
            .andExpect(jsonPath("$.[*].relato").value(hasItem(DEFAULT_RELATO.toString())))
            .andExpect(jsonPath("$.[*].linksExternos").value(hasItem(DEFAULT_LINKS_EXTERNOS.toString())));

        // Check, that the count call also returns 1
        restRelatorioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRelatorioShouldNotBeFound(String filter) throws Exception {
        restRelatorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRelatorioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRelatorio() throws Exception {
        // Get the relatorio
        restRelatorioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRelatorio() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();

        // Update the relatorio
        Relatorio updatedRelatorio = relatorioRepository.findById(relatorio.getId()).get();
        // Disconnect from session so that the updates on updatedRelatorio are not directly saved in db
        em.detach(updatedRelatorio);
        updatedRelatorio.dataHora(UPDATED_DATA_HORA).relato(UPDATED_RELATO).linksExternos(UPDATED_LINKS_EXTERNOS);
        RelatorioDTO relatorioDTO = relatorioMapper.toDto(updatedRelatorio);

        restRelatorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, relatorioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatorioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
        Relatorio testRelatorio = relatorioList.get(relatorioList.size() - 1);
        assertThat(testRelatorio.getDataHora()).isEqualTo(UPDATED_DATA_HORA);
        assertThat(testRelatorio.getRelato()).isEqualTo(UPDATED_RELATO);
        assertThat(testRelatorio.getLinksExternos()).isEqualTo(UPDATED_LINKS_EXTERNOS);
    }

    @Test
    @Transactional
    void putNonExistingRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // Create the Relatorio
        RelatorioDTO relatorioDTO = relatorioMapper.toDto(relatorio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, relatorioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // Create the Relatorio
        RelatorioDTO relatorioDTO = relatorioMapper.toDto(relatorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // Create the Relatorio
        RelatorioDTO relatorioDTO = relatorioMapper.toDto(relatorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatorioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRelatorioWithPatch() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();

        // Update the relatorio using partial update
        Relatorio partialUpdatedRelatorio = new Relatorio();
        partialUpdatedRelatorio.setId(relatorio.getId());

        partialUpdatedRelatorio.dataHora(UPDATED_DATA_HORA).relato(UPDATED_RELATO);

        restRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelatorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelatorio))
            )
            .andExpect(status().isOk());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
        Relatorio testRelatorio = relatorioList.get(relatorioList.size() - 1);
        assertThat(testRelatorio.getDataHora()).isEqualTo(UPDATED_DATA_HORA);
        assertThat(testRelatorio.getRelato()).isEqualTo(UPDATED_RELATO);
        assertThat(testRelatorio.getLinksExternos()).isEqualTo(DEFAULT_LINKS_EXTERNOS);
    }

    @Test
    @Transactional
    void fullUpdateRelatorioWithPatch() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();

        // Update the relatorio using partial update
        Relatorio partialUpdatedRelatorio = new Relatorio();
        partialUpdatedRelatorio.setId(relatorio.getId());

        partialUpdatedRelatorio.dataHora(UPDATED_DATA_HORA).relato(UPDATED_RELATO).linksExternos(UPDATED_LINKS_EXTERNOS);

        restRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelatorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelatorio))
            )
            .andExpect(status().isOk());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
        Relatorio testRelatorio = relatorioList.get(relatorioList.size() - 1);
        assertThat(testRelatorio.getDataHora()).isEqualTo(UPDATED_DATA_HORA);
        assertThat(testRelatorio.getRelato()).isEqualTo(UPDATED_RELATO);
        assertThat(testRelatorio.getLinksExternos()).isEqualTo(UPDATED_LINKS_EXTERNOS);
    }

    @Test
    @Transactional
    void patchNonExistingRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // Create the Relatorio
        RelatorioDTO relatorioDTO = relatorioMapper.toDto(relatorio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, relatorioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relatorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // Create the Relatorio
        RelatorioDTO relatorioDTO = relatorioMapper.toDto(relatorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relatorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // Create the Relatorio
        RelatorioDTO relatorioDTO = relatorioMapper.toDto(relatorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(relatorioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRelatorio() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        int databaseSizeBeforeDelete = relatorioRepository.findAll().size();

        // Delete the relatorio
        restRelatorioMockMvc
            .perform(delete(ENTITY_API_URL_ID, relatorio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
