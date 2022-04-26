package com.operacional.controleoperacional.web.rest;

import com.operacional.controleoperacional.repository.TipoRelatorioRepository;
import com.operacional.controleoperacional.service.TipoRelatorioService;
import com.operacional.controleoperacional.service.dto.TipoRelatorioDTO;
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
 * REST controller for managing {@link com.operacional.controleoperacional.domain.TipoRelatorio}.
 */
@RestController
@RequestMapping("/api")
public class TipoRelatorioResource {

    private final Logger log = LoggerFactory.getLogger(TipoRelatorioResource.class);

    private static final String ENTITY_NAME = "tipoRelatorio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoRelatorioService tipoRelatorioService;

    private final TipoRelatorioRepository tipoRelatorioRepository;

    public TipoRelatorioResource(TipoRelatorioService tipoRelatorioService, TipoRelatorioRepository tipoRelatorioRepository) {
        this.tipoRelatorioService = tipoRelatorioService;
        this.tipoRelatorioRepository = tipoRelatorioRepository;
    }

    /**
     * {@code POST  /tipo-relatorios} : Create a new tipoRelatorio.
     *
     * @param tipoRelatorioDTO the tipoRelatorioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoRelatorioDTO, or with status {@code 400 (Bad Request)} if the tipoRelatorio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-relatorios")
    public ResponseEntity<TipoRelatorioDTO> createTipoRelatorio(@Valid @RequestBody TipoRelatorioDTO tipoRelatorioDTO)
        throws URISyntaxException {
        log.debug("REST request to save TipoRelatorio : {}", tipoRelatorioDTO);
        if (tipoRelatorioDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoRelatorio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoRelatorioDTO result = tipoRelatorioService.save(tipoRelatorioDTO);
        return ResponseEntity
            .created(new URI("/api/tipo-relatorios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-relatorios/:id} : Updates an existing tipoRelatorio.
     *
     * @param id the id of the tipoRelatorioDTO to save.
     * @param tipoRelatorioDTO the tipoRelatorioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoRelatorioDTO,
     * or with status {@code 400 (Bad Request)} if the tipoRelatorioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoRelatorioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-relatorios/{id}")
    public ResponseEntity<TipoRelatorioDTO> updateTipoRelatorio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoRelatorioDTO tipoRelatorioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoRelatorio : {}, {}", id, tipoRelatorioDTO);
        if (tipoRelatorioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoRelatorioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoRelatorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoRelatorioDTO result = tipoRelatorioService.update(tipoRelatorioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoRelatorioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-relatorios/:id} : Partial updates given fields of an existing tipoRelatorio, field will ignore if it is null
     *
     * @param id the id of the tipoRelatorioDTO to save.
     * @param tipoRelatorioDTO the tipoRelatorioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoRelatorioDTO,
     * or with status {@code 400 (Bad Request)} if the tipoRelatorioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoRelatorioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoRelatorioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-relatorios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoRelatorioDTO> partialUpdateTipoRelatorio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoRelatorioDTO tipoRelatorioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoRelatorio partially : {}, {}", id, tipoRelatorioDTO);
        if (tipoRelatorioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoRelatorioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoRelatorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoRelatorioDTO> result = tipoRelatorioService.partialUpdate(tipoRelatorioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoRelatorioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-relatorios} : get all the tipoRelatorios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoRelatorios in body.
     */
    @GetMapping("/tipo-relatorios")
    public List<TipoRelatorioDTO> getAllTipoRelatorios(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all TipoRelatorios");
        return tipoRelatorioService.findAll();
    }

    /**
     * {@code GET  /tipo-relatorios/:id} : get the "id" tipoRelatorio.
     *
     * @param id the id of the tipoRelatorioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoRelatorioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-relatorios/{id}")
    public ResponseEntity<TipoRelatorioDTO> getTipoRelatorio(@PathVariable Long id) {
        log.debug("REST request to get TipoRelatorio : {}", id);
        Optional<TipoRelatorioDTO> tipoRelatorioDTO = tipoRelatorioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoRelatorioDTO);
    }

    /**
     * {@code DELETE  /tipo-relatorios/:id} : delete the "id" tipoRelatorio.
     *
     * @param id the id of the tipoRelatorioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-relatorios/{id}")
    public ResponseEntity<Void> deleteTipoRelatorio(@PathVariable Long id) {
        log.debug("REST request to delete TipoRelatorio : {}", id);
        tipoRelatorioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
