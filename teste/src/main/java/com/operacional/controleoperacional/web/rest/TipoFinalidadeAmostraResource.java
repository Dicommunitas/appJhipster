package com.operacional.controleoperacional.web.rest;

import com.operacional.controleoperacional.repository.TipoFinalidadeAmostraRepository;
import com.operacional.controleoperacional.service.TipoFinalidadeAmostraService;
import com.operacional.controleoperacional.service.dto.TipoFinalidadeAmostraDTO;
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
 * REST controller for managing {@link com.operacional.controleoperacional.domain.TipoFinalidadeAmostra}.
 */
@RestController
@RequestMapping("/api")
public class TipoFinalidadeAmostraResource {

    private final Logger log = LoggerFactory.getLogger(TipoFinalidadeAmostraResource.class);

    private static final String ENTITY_NAME = "tipoFinalidadeAmostra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoFinalidadeAmostraService tipoFinalidadeAmostraService;

    private final TipoFinalidadeAmostraRepository tipoFinalidadeAmostraRepository;

    public TipoFinalidadeAmostraResource(
        TipoFinalidadeAmostraService tipoFinalidadeAmostraService,
        TipoFinalidadeAmostraRepository tipoFinalidadeAmostraRepository
    ) {
        this.tipoFinalidadeAmostraService = tipoFinalidadeAmostraService;
        this.tipoFinalidadeAmostraRepository = tipoFinalidadeAmostraRepository;
    }

    /**
     * {@code POST  /tipo-finalidade-amostras} : Create a new tipoFinalidadeAmostra.
     *
     * @param tipoFinalidadeAmostraDTO the tipoFinalidadeAmostraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoFinalidadeAmostraDTO, or with status {@code 400 (Bad Request)} if the tipoFinalidadeAmostra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-finalidade-amostras")
    public ResponseEntity<TipoFinalidadeAmostraDTO> createTipoFinalidadeAmostra(
        @Valid @RequestBody TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TipoFinalidadeAmostra : {}", tipoFinalidadeAmostraDTO);
        if (tipoFinalidadeAmostraDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoFinalidadeAmostra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoFinalidadeAmostraDTO result = tipoFinalidadeAmostraService.save(tipoFinalidadeAmostraDTO);
        return ResponseEntity
            .created(new URI("/api/tipo-finalidade-amostras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-finalidade-amostras/:id} : Updates an existing tipoFinalidadeAmostra.
     *
     * @param id the id of the tipoFinalidadeAmostraDTO to save.
     * @param tipoFinalidadeAmostraDTO the tipoFinalidadeAmostraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoFinalidadeAmostraDTO,
     * or with status {@code 400 (Bad Request)} if the tipoFinalidadeAmostraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoFinalidadeAmostraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-finalidade-amostras/{id}")
    public ResponseEntity<TipoFinalidadeAmostraDTO> updateTipoFinalidadeAmostra(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoFinalidadeAmostra : {}, {}", id, tipoFinalidadeAmostraDTO);
        if (tipoFinalidadeAmostraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoFinalidadeAmostraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoFinalidadeAmostraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoFinalidadeAmostraDTO result = tipoFinalidadeAmostraService.update(tipoFinalidadeAmostraDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoFinalidadeAmostraDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-finalidade-amostras/:id} : Partial updates given fields of an existing tipoFinalidadeAmostra, field will ignore if it is null
     *
     * @param id the id of the tipoFinalidadeAmostraDTO to save.
     * @param tipoFinalidadeAmostraDTO the tipoFinalidadeAmostraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoFinalidadeAmostraDTO,
     * or with status {@code 400 (Bad Request)} if the tipoFinalidadeAmostraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoFinalidadeAmostraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoFinalidadeAmostraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-finalidade-amostras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoFinalidadeAmostraDTO> partialUpdateTipoFinalidadeAmostra(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoFinalidadeAmostra partially : {}, {}", id, tipoFinalidadeAmostraDTO);
        if (tipoFinalidadeAmostraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoFinalidadeAmostraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoFinalidadeAmostraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoFinalidadeAmostraDTO> result = tipoFinalidadeAmostraService.partialUpdate(tipoFinalidadeAmostraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoFinalidadeAmostraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-finalidade-amostras} : get all the tipoFinalidadeAmostras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoFinalidadeAmostras in body.
     */
    @GetMapping("/tipo-finalidade-amostras")
    public List<TipoFinalidadeAmostraDTO> getAllTipoFinalidadeAmostras() {
        log.debug("REST request to get all TipoFinalidadeAmostras");
        return tipoFinalidadeAmostraService.findAll();
    }

    /**
     * {@code GET  /tipo-finalidade-amostras/:id} : get the "id" tipoFinalidadeAmostra.
     *
     * @param id the id of the tipoFinalidadeAmostraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoFinalidadeAmostraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-finalidade-amostras/{id}")
    public ResponseEntity<TipoFinalidadeAmostraDTO> getTipoFinalidadeAmostra(@PathVariable Long id) {
        log.debug("REST request to get TipoFinalidadeAmostra : {}", id);
        Optional<TipoFinalidadeAmostraDTO> tipoFinalidadeAmostraDTO = tipoFinalidadeAmostraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoFinalidadeAmostraDTO);
    }

    /**
     * {@code DELETE  /tipo-finalidade-amostras/:id} : delete the "id" tipoFinalidadeAmostra.
     *
     * @param id the id of the tipoFinalidadeAmostraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-finalidade-amostras/{id}")
    public ResponseEntity<Void> deleteTipoFinalidadeAmostra(@PathVariable Long id) {
        log.debug("REST request to delete TipoFinalidadeAmostra : {}", id);
        tipoFinalidadeAmostraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
