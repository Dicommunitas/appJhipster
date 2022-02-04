package com.operacional.controleoperacional.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.operacional.controleoperacional.IntegrationTest;
import com.operacional.controleoperacional.domain.Relatorio;
import com.operacional.controleoperacional.domain.TipoRelatorio;
import com.operacional.controleoperacional.domain.Usuario;
import com.operacional.controleoperacional.repository.RelatorioRepository;
import com.operacional.controleoperacional.service.criteria.RelatorioCriteria;
import com.operacional.controleoperacional.service.dto.RelatorioDTO;
import com.operacional.controleoperacional.service.mapper.RelatorioMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link RelatorioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RelatorioResourceIT {

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

    @Autowired
    private RelatorioMapper relatorioMapper;

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
        Relatorio relatorio = new Relatorio().relato(DEFAULT_RELATO).linksExternos(DEFAULT_LINKS_EXTERNOS);
        // Add required entity
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createEntity(em);
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        relatorio.setResponsavel(usuario);
        return relatorio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relatorio createUpdatedEntity(EntityManager em) {
        Relatorio relatorio = new Relatorio().relato(UPDATED_RELATO).linksExternos(UPDATED_LINKS_EXTERNOS);
        // Add required entity
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createUpdatedEntity(em);
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        relatorio.setResponsavel(usuario);
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
    void getAllRelatorios() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList
        restRelatorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatorio.getId().intValue())))
            .andExpect(jsonPath("$.[*].relato").value(hasItem(DEFAULT_RELATO.toString())))
            .andExpect(jsonPath("$.[*].linksExternos").value(hasItem(DEFAULT_LINKS_EXTERNOS)));
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
            .andExpect(jsonPath("$.relato").value(DEFAULT_RELATO.toString()))
            .andExpect(jsonPath("$.linksExternos").value(DEFAULT_LINKS_EXTERNOS));
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
    void getAllRelatoriosByLinksExternosIsEqualToSomething() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList where linksExternos equals to DEFAULT_LINKS_EXTERNOS
        defaultRelatorioShouldBeFound("linksExternos.equals=" + DEFAULT_LINKS_EXTERNOS);

        // Get all the relatorioList where linksExternos equals to UPDATED_LINKS_EXTERNOS
        defaultRelatorioShouldNotBeFound("linksExternos.equals=" + UPDATED_LINKS_EXTERNOS);
    }

    @Test
    @Transactional
    void getAllRelatoriosByLinksExternosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList where linksExternos not equals to DEFAULT_LINKS_EXTERNOS
        defaultRelatorioShouldNotBeFound("linksExternos.notEquals=" + DEFAULT_LINKS_EXTERNOS);

        // Get all the relatorioList where linksExternos not equals to UPDATED_LINKS_EXTERNOS
        defaultRelatorioShouldBeFound("linksExternos.notEquals=" + UPDATED_LINKS_EXTERNOS);
    }

    @Test
    @Transactional
    void getAllRelatoriosByLinksExternosIsInShouldWork() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList where linksExternos in DEFAULT_LINKS_EXTERNOS or UPDATED_LINKS_EXTERNOS
        defaultRelatorioShouldBeFound("linksExternos.in=" + DEFAULT_LINKS_EXTERNOS + "," + UPDATED_LINKS_EXTERNOS);

        // Get all the relatorioList where linksExternos equals to UPDATED_LINKS_EXTERNOS
        defaultRelatorioShouldNotBeFound("linksExternos.in=" + UPDATED_LINKS_EXTERNOS);
    }

    @Test
    @Transactional
    void getAllRelatoriosByLinksExternosIsNullOrNotNull() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList where linksExternos is not null
        defaultRelatorioShouldBeFound("linksExternos.specified=true");

        // Get all the relatorioList where linksExternos is null
        defaultRelatorioShouldNotBeFound("linksExternos.specified=false");
    }

    @Test
    @Transactional
    void getAllRelatoriosByLinksExternosContainsSomething() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList where linksExternos contains DEFAULT_LINKS_EXTERNOS
        defaultRelatorioShouldBeFound("linksExternos.contains=" + DEFAULT_LINKS_EXTERNOS);

        // Get all the relatorioList where linksExternos contains UPDATED_LINKS_EXTERNOS
        defaultRelatorioShouldNotBeFound("linksExternos.contains=" + UPDATED_LINKS_EXTERNOS);
    }

    @Test
    @Transactional
    void getAllRelatoriosByLinksExternosNotContainsSomething() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList where linksExternos does not contain DEFAULT_LINKS_EXTERNOS
        defaultRelatorioShouldNotBeFound("linksExternos.doesNotContain=" + DEFAULT_LINKS_EXTERNOS);

        // Get all the relatorioList where linksExternos does not contain UPDATED_LINKS_EXTERNOS
        defaultRelatorioShouldBeFound("linksExternos.doesNotContain=" + UPDATED_LINKS_EXTERNOS);
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
        Usuario responsavel;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            responsavel = UsuarioResourceIT.createEntity(em);
            em.persist(responsavel);
            em.flush();
        } else {
            responsavel = TestUtil.findAll(em, Usuario.class).get(0);
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
            .andExpect(jsonPath("$.[*].relato").value(hasItem(DEFAULT_RELATO.toString())))
            .andExpect(jsonPath("$.[*].linksExternos").value(hasItem(DEFAULT_LINKS_EXTERNOS)));

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
        updatedRelatorio.relato(UPDATED_RELATO).linksExternos(UPDATED_LINKS_EXTERNOS);
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

        partialUpdatedRelatorio.relato(UPDATED_RELATO).linksExternos(UPDATED_LINKS_EXTERNOS);

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
        assertThat(testRelatorio.getRelato()).isEqualTo(UPDATED_RELATO);
        assertThat(testRelatorio.getLinksExternos()).isEqualTo(UPDATED_LINKS_EXTERNOS);
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

        partialUpdatedRelatorio.relato(UPDATED_RELATO).linksExternos(UPDATED_LINKS_EXTERNOS);

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
