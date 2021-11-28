package com.operacional.controleamostramicroservice.web.rest;

import com.operacional.controleamostramicroservice.repository.ARepository;
import com.operacional.controleamostramicroservice.service.AService;
import com.operacional.controleamostramicroservice.service.dto.ADTO;
import com.operacional.controleamostramicroservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.operacional.controleamostramicroservice.domain.A}.
 */
@RestController
@RequestMapping("/api")
public class AResource {

    private final Logger log = LoggerFactory.getLogger(AResource.class);

    private static final String ENTITY_NAME = "controleAmostraMicroserviceA";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AService aService;

    private final ARepository aRepository;

    public AResource(AService aService, ARepository aRepository) {
        this.aService = aService;
        this.aRepository = aRepository;
    }

    /**
     * {@code POST  /as} : Create a new a.
     *
     * @param aDTO the aDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aDTO, or with status {@code 400 (Bad Request)} if the a has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/as")
    public ResponseEntity<ADTO> createA(@RequestBody ADTO aDTO) throws URISyntaxException {
        log.debug("REST request to save A : {}", aDTO);
        if (aDTO.getId() != null) {
            throw new BadRequestAlertException("A new a cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ADTO result = aService.save(aDTO);
        return ResponseEntity
            .created(new URI("/api/as/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /as/:id} : Updates an existing a.
     *
     * @param id the id of the aDTO to save.
     * @param aDTO the aDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aDTO,
     * or with status {@code 400 (Bad Request)} if the aDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/as/{id}")
    public ResponseEntity<ADTO> updateA(@PathVariable(value = "id", required = false) final Long id, @RequestBody ADTO aDTO)
        throws URISyntaxException {
        log.debug("REST request to update A : {}, {}", id, aDTO);
        if (aDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ADTO result = aService.save(aDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /as/:id} : Partial updates given fields of an existing a, field will ignore if it is null
     *
     * @param id the id of the aDTO to save.
     * @param aDTO the aDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aDTO,
     * or with status {@code 400 (Bad Request)} if the aDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/as/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ADTO> partialUpdateA(@PathVariable(value = "id", required = false) final Long id, @RequestBody ADTO aDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update A partially : {}, {}", id, aDTO);
        if (aDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ADTO> result = aService.partialUpdate(aDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /as} : get all the aS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aS in body.
     */
    @GetMapping("/as")
    public List<ADTO> getAllAS() {
        log.debug("REST request to get all AS");
        return aService.findAll();
    }

    /**
     * {@code GET  /as/:id} : get the "id" a.
     *
     * @param id the id of the aDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/as/{id}")
    public ResponseEntity<ADTO> getA(@PathVariable Long id) {
        log.debug("REST request to get A : {}", id);
        Optional<ADTO> aDTO = aService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aDTO);
    }

    /**
     * {@code DELETE  /as/:id} : delete the "id" a.
     *
     * @param id the id of the aDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/as/{id}")
    public ResponseEntity<Void> deleteA(@PathVariable Long id) {
        log.debug("REST request to delete A : {}", id);
        aService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
