package com.operacional.controleoperacional.web.rest;

import com.operacional.controleoperacional.repository.TipoAmostraRepository;
import com.operacional.controleoperacional.service.TipoAmostraService;
import com.operacional.controleoperacional.service.dto.TipoAmostraDTO;
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
 * REST controller for managing {@link com.operacional.controleoperacional.domain.TipoAmostra}.
 */
@RestController
@RequestMapping("/api")
public class TipoAmostraResource {

    private final Logger log = LoggerFactory.getLogger(TipoAmostraResource.class);

    private static final String ENTITY_NAME = "tipoAmostra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoAmostraService tipoAmostraService;

    private final TipoAmostraRepository tipoAmostraRepository;

    public TipoAmostraResource(TipoAmostraService tipoAmostraService, TipoAmostraRepository tipoAmostraRepository) {
        this.tipoAmostraService = tipoAmostraService;
        this.tipoAmostraRepository = tipoAmostraRepository;
    }

    /**
     * {@code POST  /tipo-amostras} : Create a new tipoAmostra.
     *
     * @param tipoAmostraDTO the tipoAmostraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoAmostraDTO, or with status {@code 400 (Bad Request)} if the tipoAmostra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-amostras")
    public ResponseEntity<TipoAmostraDTO> createTipoAmostra(@Valid @RequestBody TipoAmostraDTO tipoAmostraDTO) throws URISyntaxException {
        log.debug("REST request to save TipoAmostra : {}", tipoAmostraDTO);
        if (tipoAmostraDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoAmostra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoAmostraDTO result = tipoAmostraService.save(tipoAmostraDTO);
        return ResponseEntity
            .created(new URI("/api/tipo-amostras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-amostras/:id} : Updates an existing tipoAmostra.
     *
     * @param id the id of the tipoAmostraDTO to save.
     * @param tipoAmostraDTO the tipoAmostraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoAmostraDTO,
     * or with status {@code 400 (Bad Request)} if the tipoAmostraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoAmostraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-amostras/{id}")
    public ResponseEntity<TipoAmostraDTO> updateTipoAmostra(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoAmostraDTO tipoAmostraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoAmostra : {}, {}", id, tipoAmostraDTO);
        if (tipoAmostraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoAmostraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoAmostraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoAmostraDTO result = tipoAmostraService.update(tipoAmostraDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoAmostraDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-amostras/:id} : Partial updates given fields of an existing tipoAmostra, field will ignore if it is null
     *
     * @param id the id of the tipoAmostraDTO to save.
     * @param tipoAmostraDTO the tipoAmostraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoAmostraDTO,
     * or with status {@code 400 (Bad Request)} if the tipoAmostraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoAmostraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoAmostraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-amostras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoAmostraDTO> partialUpdateTipoAmostra(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoAmostraDTO tipoAmostraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoAmostra partially : {}, {}", id, tipoAmostraDTO);
        if (tipoAmostraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoAmostraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoAmostraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoAmostraDTO> result = tipoAmostraService.partialUpdate(tipoAmostraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoAmostraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-amostras} : get all the tipoAmostras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoAmostras in body.
     */
    @GetMapping("/tipo-amostras")
    public List<TipoAmostraDTO> getAllTipoAmostras() {
        log.debug("REST request to get all TipoAmostras");
        return tipoAmostraService.findAll();
    }

    /**
     * {@code GET  /tipo-amostras/:id} : get the "id" tipoAmostra.
     *
     * @param id the id of the tipoAmostraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoAmostraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-amostras/{id}")
    public ResponseEntity<TipoAmostraDTO> getTipoAmostra(@PathVariable Long id) {
        log.debug("REST request to get TipoAmostra : {}", id);
        Optional<TipoAmostraDTO> tipoAmostraDTO = tipoAmostraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoAmostraDTO);
    }

    /**
     * {@code DELETE  /tipo-amostras/:id} : delete the "id" tipoAmostra.
     *
     * @param id the id of the tipoAmostraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-amostras/{id}")
    public ResponseEntity<Void> deleteTipoAmostra(@PathVariable Long id) {
        log.debug("REST request to delete TipoAmostra : {}", id);
        tipoAmostraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
