package com.operacional.controleoperacional.web.rest;

import com.operacional.controleoperacional.repository.RelatorioRepository;
import com.operacional.controleoperacional.service.RelatorioQueryService;
import com.operacional.controleoperacional.service.RelatorioService;
import com.operacional.controleoperacional.service.criteria.RelatorioCriteria;
import com.operacional.controleoperacional.service.dto.RelatorioDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.operacional.controleoperacional.domain.Relatorio}.
 */
@RestController
@RequestMapping("/api")
public class RelatorioResource {

    private final Logger log = LoggerFactory.getLogger(RelatorioResource.class);

    private static final String ENTITY_NAME = "relatorio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RelatorioService relatorioService;

    private final RelatorioRepository relatorioRepository;

    private final RelatorioQueryService relatorioQueryService;

    public RelatorioResource(
        RelatorioService relatorioService,
        RelatorioRepository relatorioRepository,
        RelatorioQueryService relatorioQueryService
    ) {
        this.relatorioService = relatorioService;
        this.relatorioRepository = relatorioRepository;
        this.relatorioQueryService = relatorioQueryService;
    }

    /**
     * {@code POST  /relatorios} : Create a new relatorio.
     *
     * @param relatorioDTO the relatorioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new relatorioDTO, or with status {@code 400 (Bad Request)} if the relatorio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/relatorios")
    public ResponseEntity<RelatorioDTO> createRelatorio(@Valid @RequestBody RelatorioDTO relatorioDTO) throws URISyntaxException {
        log.debug("REST request to save Relatorio : {}", relatorioDTO);
        if (relatorioDTO.getId() != null) {
            throw new BadRequestAlertException("A new relatorio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RelatorioDTO result = relatorioService.save(relatorioDTO);
        return ResponseEntity
            .created(new URI("/api/relatorios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /relatorios/:id} : Updates an existing relatorio.
     *
     * @param id the id of the relatorioDTO to save.
     * @param relatorioDTO the relatorioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatorioDTO,
     * or with status {@code 400 (Bad Request)} if the relatorioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the relatorioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/relatorios/{id}")
    public ResponseEntity<RelatorioDTO> updateRelatorio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RelatorioDTO relatorioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Relatorio : {}, {}", id, relatorioDTO);
        if (relatorioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatorioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relatorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RelatorioDTO result = relatorioService.save(relatorioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relatorioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /relatorios/:id} : Partial updates given fields of an existing relatorio, field will ignore if it is null
     *
     * @param id the id of the relatorioDTO to save.
     * @param relatorioDTO the relatorioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatorioDTO,
     * or with status {@code 400 (Bad Request)} if the relatorioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the relatorioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the relatorioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/relatorios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RelatorioDTO> partialUpdateRelatorio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RelatorioDTO relatorioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Relatorio partially : {}, {}", id, relatorioDTO);
        if (relatorioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatorioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relatorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RelatorioDTO> result = relatorioService.partialUpdate(relatorioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relatorioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /relatorios} : get all the relatorios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of relatorios in body.
     */
    @GetMapping("/relatorios")
    public ResponseEntity<List<RelatorioDTO>> getAllRelatorios(RelatorioCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Relatorios by criteria: {}", criteria);
        Page<RelatorioDTO> page = relatorioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /relatorios/count} : count all the relatorios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/relatorios/count")
    public ResponseEntity<Long> countRelatorios(RelatorioCriteria criteria) {
        log.debug("REST request to count Relatorios by criteria: {}", criteria);
        return ResponseEntity.ok().body(relatorioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /relatorios/:id} : get the "id" relatorio.
     *
     * @param id the id of the relatorioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the relatorioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/relatorios/{id}")
    public ResponseEntity<RelatorioDTO> getRelatorio(@PathVariable Long id) {
        log.debug("REST request to get Relatorio : {}", id);
        Optional<RelatorioDTO> relatorioDTO = relatorioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(relatorioDTO);
    }

    /**
     * {@code DELETE  /relatorios/:id} : delete the "id" relatorio.
     *
     * @param id the id of the relatorioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/relatorios/{id}")
    public ResponseEntity<Void> deleteRelatorio(@PathVariable Long id) {
        log.debug("REST request to delete Relatorio : {}", id);
        relatorioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
