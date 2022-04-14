package com.operacional.controleoperacional.web.rest;

import com.operacional.controleoperacional.repository.LembreteRepository;
import com.operacional.controleoperacional.service.LembreteService;
import com.operacional.controleoperacional.service.dto.LembreteDTO;
import com.operacional.controleoperacional.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.operacional.controleoperacional.domain.Lembrete}.
 */
@RestController
@RequestMapping("/api")
public class LembreteResource {

    private final Logger log = LoggerFactory.getLogger(LembreteResource.class);

    private static final String ENTITY_NAME = "lembrete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LembreteService lembreteService;

    private final LembreteRepository lembreteRepository;

    public LembreteResource(LembreteService lembreteService, LembreteRepository lembreteRepository) {
        this.lembreteService = lembreteService;
        this.lembreteRepository = lembreteRepository;
    }

    /**
     * {@code POST  /lembretes} : Create a new lembrete.
     *
     * @param lembreteDTO the lembreteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lembreteDTO, or with status {@code 400 (Bad Request)} if the lembrete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lembretes")
    public ResponseEntity<LembreteDTO> createLembrete(@Valid @RequestBody LembreteDTO lembreteDTO) throws URISyntaxException {
        log.debug("REST request to save Lembrete : {}", lembreteDTO);
        if (lembreteDTO.getId() != null) {
            throw new BadRequestAlertException("A new lembrete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LembreteDTO result = lembreteService.save(lembreteDTO);
        return ResponseEntity
            .created(new URI("/api/lembretes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lembretes/:id} : Updates an existing lembrete.
     *
     * @param id the id of the lembreteDTO to save.
     * @param lembreteDTO the lembreteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lembreteDTO,
     * or with status {@code 400 (Bad Request)} if the lembreteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lembreteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lembretes/{id}")
    public ResponseEntity<LembreteDTO> updateLembrete(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LembreteDTO lembreteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Lembrete : {}, {}", id, lembreteDTO);
        if (lembreteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lembreteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lembreteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LembreteDTO result = lembreteService.update(lembreteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lembreteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lembretes/:id} : Partial updates given fields of an existing lembrete, field will ignore if it is null
     *
     * @param id the id of the lembreteDTO to save.
     * @param lembreteDTO the lembreteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lembreteDTO,
     * or with status {@code 400 (Bad Request)} if the lembreteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lembreteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lembreteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lembretes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LembreteDTO> partialUpdateLembrete(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LembreteDTO lembreteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lembrete partially : {}, {}", id, lembreteDTO);
        if (lembreteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lembreteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lembreteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LembreteDTO> result = lembreteService.partialUpdate(lembreteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lembreteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lembretes} : get all the lembretes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lembretes in body.
     */
    @GetMapping("/lembretes")
    public List<LembreteDTO> getAllLembretes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Lembretes");
        return lembreteService.findAll();
    }

    /**
     * {@code GET  /lembretes/:id} : get the "id" lembrete.
     *
     * @param id the id of the lembreteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lembreteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lembretes/{id}")
    public ResponseEntity<LembreteDTO> getLembrete(@PathVariable Long id) {
        log.debug("REST request to get Lembrete : {}", id);
        Optional<LembreteDTO> lembreteDTO = lembreteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lembreteDTO);
    }

    /**
     * {@code DELETE  /lembretes/:id} : delete the "id" lembrete.
     *
     * @param id the id of the lembreteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lembretes/{id}")
    public ResponseEntity<Void> deleteLembrete(@PathVariable Long id) {
        log.debug("REST request to delete Lembrete : {}", id);
        lembreteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
