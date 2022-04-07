package com.operacional.controleoperacional.web.rest;

import com.operacional.controleoperacional.repository.OperacaoRepository;
import com.operacional.controleoperacional.service.OperacaoQueryService;
import com.operacional.controleoperacional.service.OperacaoService;
import com.operacional.controleoperacional.service.criteria.OperacaoCriteria;
import com.operacional.controleoperacional.service.dto.OperacaoDTO;
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
 * REST controller for managing {@link com.operacional.controleoperacional.domain.Operacao}.
 */
@RestController
@RequestMapping("/api")
public class OperacaoResource {

    private final Logger log = LoggerFactory.getLogger(OperacaoResource.class);

    private static final String ENTITY_NAME = "operacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperacaoService operacaoService;

    private final OperacaoRepository operacaoRepository;

    private final OperacaoQueryService operacaoQueryService;

    public OperacaoResource(
        OperacaoService operacaoService,
        OperacaoRepository operacaoRepository,
        OperacaoQueryService operacaoQueryService
    ) {
        this.operacaoService = operacaoService;
        this.operacaoRepository = operacaoRepository;
        this.operacaoQueryService = operacaoQueryService;
    }

    /**
     * {@code POST  /operacaos} : Create a new operacao.
     *
     * @param operacaoDTO the operacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operacaoDTO, or with status {@code 400 (Bad Request)} if the operacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operacaos")
    public ResponseEntity<OperacaoDTO> createOperacao(@Valid @RequestBody OperacaoDTO operacaoDTO) throws URISyntaxException {
        log.debug("REST request to save Operacao : {}", operacaoDTO);
        if (operacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new operacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OperacaoDTO result = operacaoService.save(operacaoDTO);
        return ResponseEntity
            .created(new URI("/api/operacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operacaos/:id} : Updates an existing operacao.
     *
     * @param id the id of the operacaoDTO to save.
     * @param operacaoDTO the operacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operacaoDTO,
     * or with status {@code 400 (Bad Request)} if the operacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operacaos/{id}")
    public ResponseEntity<OperacaoDTO> updateOperacao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OperacaoDTO operacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Operacao : {}, {}", id, operacaoDTO);
        if (operacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OperacaoDTO result = operacaoService.update(operacaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /operacaos/:id} : Partial updates given fields of an existing operacao, field will ignore if it is null
     *
     * @param id the id of the operacaoDTO to save.
     * @param operacaoDTO the operacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operacaoDTO,
     * or with status {@code 400 (Bad Request)} if the operacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the operacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the operacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/operacaos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OperacaoDTO> partialUpdateOperacao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OperacaoDTO operacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Operacao partially : {}, {}", id, operacaoDTO);
        if (operacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OperacaoDTO> result = operacaoService.partialUpdate(operacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /operacaos} : get all the operacaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operacaos in body.
     */
    @GetMapping("/operacaos")
    public ResponseEntity<List<OperacaoDTO>> getAllOperacaos(
        OperacaoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Operacaos by criteria: {}", criteria);
        Page<OperacaoDTO> page = operacaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /operacaos/count} : count all the operacaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/operacaos/count")
    public ResponseEntity<Long> countOperacaos(OperacaoCriteria criteria) {
        log.debug("REST request to count Operacaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(operacaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /operacaos/:id} : get the "id" operacao.
     *
     * @param id the id of the operacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operacaos/{id}")
    public ResponseEntity<OperacaoDTO> getOperacao(@PathVariable Long id) {
        log.debug("REST request to get Operacao : {}", id);
        Optional<OperacaoDTO> operacaoDTO = operacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(operacaoDTO);
    }

    /**
     * {@code DELETE  /operacaos/:id} : delete the "id" operacao.
     *
     * @param id the id of the operacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operacaos/{id}")
    public ResponseEntity<Void> deleteOperacao(@PathVariable Long id) {
        log.debug("REST request to delete Operacao : {}", id);
        operacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
