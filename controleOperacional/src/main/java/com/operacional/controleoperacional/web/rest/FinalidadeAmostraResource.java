package com.operacional.controleoperacional.web.rest;

import com.operacional.controleoperacional.repository.FinalidadeAmostraRepository;
import com.operacional.controleoperacional.service.FinalidadeAmostraService;
import com.operacional.controleoperacional.service.dto.FinalidadeAmostraDTO;
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
 * REST controller for managing {@link com.operacional.controleoperacional.domain.FinalidadeAmostra}.
 */
@RestController
@RequestMapping("/api")
public class FinalidadeAmostraResource {

    private final Logger log = LoggerFactory.getLogger(FinalidadeAmostraResource.class);

    private static final String ENTITY_NAME = "finalidadeAmostra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinalidadeAmostraService finalidadeAmostraService;

    private final FinalidadeAmostraRepository finalidadeAmostraRepository;

    public FinalidadeAmostraResource(
        FinalidadeAmostraService finalidadeAmostraService,
        FinalidadeAmostraRepository finalidadeAmostraRepository
    ) {
        this.finalidadeAmostraService = finalidadeAmostraService;
        this.finalidadeAmostraRepository = finalidadeAmostraRepository;
    }

    /**
     * {@code POST  /finalidade-amostras} : Create a new finalidadeAmostra.
     *
     * @param finalidadeAmostraDTO the finalidadeAmostraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new finalidadeAmostraDTO, or with status {@code 400 (Bad Request)} if the finalidadeAmostra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/finalidade-amostras")
    public ResponseEntity<FinalidadeAmostraDTO> createFinalidadeAmostra(@Valid @RequestBody FinalidadeAmostraDTO finalidadeAmostraDTO)
        throws URISyntaxException {
        log.debug("REST request to save FinalidadeAmostra : {}", finalidadeAmostraDTO);
        if (finalidadeAmostraDTO.getId() != null) {
            throw new BadRequestAlertException("A new finalidadeAmostra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FinalidadeAmostraDTO result = finalidadeAmostraService.save(finalidadeAmostraDTO);
        return ResponseEntity
            .created(new URI("/api/finalidade-amostras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /finalidade-amostras/:id} : Updates an existing finalidadeAmostra.
     *
     * @param id the id of the finalidadeAmostraDTO to save.
     * @param finalidadeAmostraDTO the finalidadeAmostraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated finalidadeAmostraDTO,
     * or with status {@code 400 (Bad Request)} if the finalidadeAmostraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the finalidadeAmostraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/finalidade-amostras/{id}")
    public ResponseEntity<FinalidadeAmostraDTO> updateFinalidadeAmostra(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FinalidadeAmostraDTO finalidadeAmostraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FinalidadeAmostra : {}, {}", id, finalidadeAmostraDTO);
        if (finalidadeAmostraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, finalidadeAmostraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!finalidadeAmostraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FinalidadeAmostraDTO result = finalidadeAmostraService.update(finalidadeAmostraDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, finalidadeAmostraDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /finalidade-amostras/:id} : Partial updates given fields of an existing finalidadeAmostra, field will ignore if it is null
     *
     * @param id the id of the finalidadeAmostraDTO to save.
     * @param finalidadeAmostraDTO the finalidadeAmostraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated finalidadeAmostraDTO,
     * or with status {@code 400 (Bad Request)} if the finalidadeAmostraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the finalidadeAmostraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the finalidadeAmostraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/finalidade-amostras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FinalidadeAmostraDTO> partialUpdateFinalidadeAmostra(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FinalidadeAmostraDTO finalidadeAmostraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FinalidadeAmostra partially : {}, {}", id, finalidadeAmostraDTO);
        if (finalidadeAmostraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, finalidadeAmostraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!finalidadeAmostraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FinalidadeAmostraDTO> result = finalidadeAmostraService.partialUpdate(finalidadeAmostraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, finalidadeAmostraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /finalidade-amostras} : get all the finalidadeAmostras.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of finalidadeAmostras in body.
     */
    @GetMapping("/finalidade-amostras")
    public List<FinalidadeAmostraDTO> getAllFinalidadeAmostras(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all FinalidadeAmostras");
        return finalidadeAmostraService.findAll();
    }

    /**
     * {@code GET  /finalidade-amostras/:id} : get the "id" finalidadeAmostra.
     *
     * @param id the id of the finalidadeAmostraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the finalidadeAmostraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/finalidade-amostras/{id}")
    public ResponseEntity<FinalidadeAmostraDTO> getFinalidadeAmostra(@PathVariable Long id) {
        log.debug("REST request to get FinalidadeAmostra : {}", id);
        Optional<FinalidadeAmostraDTO> finalidadeAmostraDTO = finalidadeAmostraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(finalidadeAmostraDTO);
    }

    /**
     * {@code DELETE  /finalidade-amostras/:id} : delete the "id" finalidadeAmostra.
     *
     * @param id the id of the finalidadeAmostraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/finalidade-amostras/{id}")
    public ResponseEntity<Void> deleteFinalidadeAmostra(@PathVariable Long id) {
        log.debug("REST request to delete FinalidadeAmostra : {}", id);
        finalidadeAmostraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
