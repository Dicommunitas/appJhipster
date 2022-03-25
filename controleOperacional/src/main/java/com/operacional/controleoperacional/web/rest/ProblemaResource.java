package com.operacional.controleoperacional.web.rest;

import com.operacional.controleoperacional.repository.ProblemaRepository;
import com.operacional.controleoperacional.service.ProblemaQueryService;
import com.operacional.controleoperacional.service.ProblemaService;
import com.operacional.controleoperacional.service.criteria.ProblemaCriteria;
import com.operacional.controleoperacional.service.dto.ProblemaDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.operacional.controleoperacional.domain.Problema}.
 */
@RestController
@RequestMapping("/api")
public class ProblemaResource {

    private final Logger log = LoggerFactory.getLogger(ProblemaResource.class);

    private static final String ENTITY_NAME = "problema";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProblemaService problemaService;

    private final ProblemaRepository problemaRepository;

    private final ProblemaQueryService problemaQueryService;

    public ProblemaResource(
        ProblemaService problemaService,
        ProblemaRepository problemaRepository,
        ProblemaQueryService problemaQueryService
    ) {
        this.problemaService = problemaService;
        this.problemaRepository = problemaRepository;
        this.problemaQueryService = problemaQueryService;
    }

    /**
     * {@code POST  /problemas} : Create a new problema.
     *
     * @param problemaDTO the problemaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new problemaDTO, or with status {@code 400 (Bad Request)} if the problema has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/problemas")
    public ResponseEntity<ProblemaDTO> createProblema(@Valid @RequestBody ProblemaDTO problemaDTO) throws URISyntaxException {
        log.debug("REST request to save Problema : {}", problemaDTO);
        if (problemaDTO.getId() != null) {
            throw new BadRequestAlertException("A new problema cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProblemaDTO result = problemaService.save(problemaDTO);
        return ResponseEntity
            .created(new URI("/api/problemas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /problemas/:id} : Updates an existing problema.
     *
     * @param id the id of the problemaDTO to save.
     * @param problemaDTO the problemaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated problemaDTO,
     * or with status {@code 400 (Bad Request)} if the problemaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the problemaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/problemas/{id}")
    public ResponseEntity<ProblemaDTO> updateProblema(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProblemaDTO problemaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Problema : {}, {}", id, problemaDTO);
        if (problemaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, problemaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!problemaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProblemaDTO result = problemaService.save(problemaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, problemaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /problemas/:id} : Partial updates given fields of an existing problema, field will ignore if it is null
     *
     * @param id the id of the problemaDTO to save.
     * @param problemaDTO the problemaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated problemaDTO,
     * or with status {@code 400 (Bad Request)} if the problemaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the problemaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the problemaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/problemas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProblemaDTO> partialUpdateProblema(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProblemaDTO problemaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Problema partially : {}, {}", id, problemaDTO);
        if (problemaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, problemaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!problemaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProblemaDTO> result = problemaService.partialUpdate(problemaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, problemaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /problemas} : get all the problemas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of problemas in body.
     */
    @GetMapping("/problemas")
    public ResponseEntity<List<ProblemaDTO>> getAllProblemas(
        ProblemaCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Problemas by criteria: {}", criteria);
        Page<ProblemaDTO> page = problemaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /problemas/count} : count all the problemas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/problemas/count")
    public ResponseEntity<Long> countProblemas(ProblemaCriteria criteria) {
        log.debug("REST request to count Problemas by criteria: {}", criteria);
        return ResponseEntity.ok().body(problemaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /problemas/:id} : get the "id" problema.
     *
     * @param id the id of the problemaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the problemaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/problemas/{id}")
    public ResponseEntity<ProblemaDTO> getProblema(@PathVariable Long id) {
        log.debug("REST request to get Problema : {}", id);
        Optional<ProblemaDTO> problemaDTO = problemaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(problemaDTO);
    }

    /**
     * {@code DELETE  /problemas/:id} : delete the "id" problema.
     *
     * @param id the id of the problemaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/problemas/{id}")
    public ResponseEntity<Void> deleteProblema(@PathVariable Long id) {
        log.debug("REST request to delete Problema : {}", id);
        problemaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
