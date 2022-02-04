package com.operacional.controleoperacional.web.rest;

import com.operacional.controleoperacional.repository.OrigemAmostraRepository;
import com.operacional.controleoperacional.service.OrigemAmostraService;
import com.operacional.controleoperacional.service.dto.OrigemAmostraDTO;
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
 * REST controller for managing {@link com.operacional.controleoperacional.domain.OrigemAmostra}.
 */
@RestController
@RequestMapping("/api")
public class OrigemAmostraResource {

    private final Logger log = LoggerFactory.getLogger(OrigemAmostraResource.class);

    private static final String ENTITY_NAME = "origemAmostra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrigemAmostraService origemAmostraService;

    private final OrigemAmostraRepository origemAmostraRepository;

    public OrigemAmostraResource(OrigemAmostraService origemAmostraService, OrigemAmostraRepository origemAmostraRepository) {
        this.origemAmostraService = origemAmostraService;
        this.origemAmostraRepository = origemAmostraRepository;
    }

    /**
     * {@code POST  /origem-amostras} : Create a new origemAmostra.
     *
     * @param origemAmostraDTO the origemAmostraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new origemAmostraDTO, or with status {@code 400 (Bad Request)} if the origemAmostra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/origem-amostras")
    public ResponseEntity<OrigemAmostraDTO> createOrigemAmostra(@Valid @RequestBody OrigemAmostraDTO origemAmostraDTO)
        throws URISyntaxException {
        log.debug("REST request to save OrigemAmostra : {}", origemAmostraDTO);
        if (origemAmostraDTO.getId() != null) {
            throw new BadRequestAlertException("A new origemAmostra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrigemAmostraDTO result = origemAmostraService.save(origemAmostraDTO);
        return ResponseEntity
            .created(new URI("/api/origem-amostras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /origem-amostras/:id} : Updates an existing origemAmostra.
     *
     * @param id the id of the origemAmostraDTO to save.
     * @param origemAmostraDTO the origemAmostraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated origemAmostraDTO,
     * or with status {@code 400 (Bad Request)} if the origemAmostraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the origemAmostraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/origem-amostras/{id}")
    public ResponseEntity<OrigemAmostraDTO> updateOrigemAmostra(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrigemAmostraDTO origemAmostraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrigemAmostra : {}, {}", id, origemAmostraDTO);
        if (origemAmostraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, origemAmostraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!origemAmostraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrigemAmostraDTO result = origemAmostraService.save(origemAmostraDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, origemAmostraDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /origem-amostras/:id} : Partial updates given fields of an existing origemAmostra, field will ignore if it is null
     *
     * @param id the id of the origemAmostraDTO to save.
     * @param origemAmostraDTO the origemAmostraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated origemAmostraDTO,
     * or with status {@code 400 (Bad Request)} if the origemAmostraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the origemAmostraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the origemAmostraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/origem-amostras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrigemAmostraDTO> partialUpdateOrigemAmostra(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrigemAmostraDTO origemAmostraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrigemAmostra partially : {}, {}", id, origemAmostraDTO);
        if (origemAmostraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, origemAmostraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!origemAmostraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrigemAmostraDTO> result = origemAmostraService.partialUpdate(origemAmostraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, origemAmostraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /origem-amostras} : get all the origemAmostras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of origemAmostras in body.
     */
    @GetMapping("/origem-amostras")
    public List<OrigemAmostraDTO> getAllOrigemAmostras() {
        log.debug("REST request to get all OrigemAmostras");
        return origemAmostraService.findAll();
    }

    /**
     * {@code GET  /origem-amostras/:id} : get the "id" origemAmostra.
     *
     * @param id the id of the origemAmostraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the origemAmostraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/origem-amostras/{id}")
    public ResponseEntity<OrigemAmostraDTO> getOrigemAmostra(@PathVariable Long id) {
        log.debug("REST request to get OrigemAmostra : {}", id);
        Optional<OrigemAmostraDTO> origemAmostraDTO = origemAmostraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(origemAmostraDTO);
    }

    /**
     * {@code DELETE  /origem-amostras/:id} : delete the "id" origemAmostra.
     *
     * @param id the id of the origemAmostraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/origem-amostras/{id}")
    public ResponseEntity<Void> deleteOrigemAmostra(@PathVariable Long id) {
        log.debug("REST request to delete OrigemAmostra : {}", id);
        origemAmostraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
