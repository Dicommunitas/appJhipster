package com.operacional.controleoperacional.web.rest;

import com.operacional.controleoperacional.repository.AlertaProdutoRepository;
import com.operacional.controleoperacional.service.AlertaProdutoService;
import com.operacional.controleoperacional.service.dto.AlertaProdutoDTO;
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
 * REST controller for managing {@link com.operacional.controleoperacional.domain.AlertaProduto}.
 */
@RestController
@RequestMapping("/api")
public class AlertaProdutoResource {

    private final Logger log = LoggerFactory.getLogger(AlertaProdutoResource.class);

    private static final String ENTITY_NAME = "alertaProduto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlertaProdutoService alertaProdutoService;

    private final AlertaProdutoRepository alertaProdutoRepository;

    public AlertaProdutoResource(AlertaProdutoService alertaProdutoService, AlertaProdutoRepository alertaProdutoRepository) {
        this.alertaProdutoService = alertaProdutoService;
        this.alertaProdutoRepository = alertaProdutoRepository;
    }

    /**
     * {@code POST  /alerta-produtos} : Create a new alertaProduto.
     *
     * @param alertaProdutoDTO the alertaProdutoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alertaProdutoDTO, or with status {@code 400 (Bad Request)} if the alertaProduto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alerta-produtos")
    public ResponseEntity<AlertaProdutoDTO> createAlertaProduto(@Valid @RequestBody AlertaProdutoDTO alertaProdutoDTO)
        throws URISyntaxException {
        log.debug("REST request to save AlertaProduto : {}", alertaProdutoDTO);
        if (alertaProdutoDTO.getId() != null) {
            throw new BadRequestAlertException("A new alertaProduto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlertaProdutoDTO result = alertaProdutoService.save(alertaProdutoDTO);
        return ResponseEntity
            .created(new URI("/api/alerta-produtos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alerta-produtos/:id} : Updates an existing alertaProduto.
     *
     * @param id the id of the alertaProdutoDTO to save.
     * @param alertaProdutoDTO the alertaProdutoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alertaProdutoDTO,
     * or with status {@code 400 (Bad Request)} if the alertaProdutoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alertaProdutoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alerta-produtos/{id}")
    public ResponseEntity<AlertaProdutoDTO> updateAlertaProduto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlertaProdutoDTO alertaProdutoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AlertaProduto : {}, {}", id, alertaProdutoDTO);
        if (alertaProdutoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alertaProdutoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alertaProdutoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AlertaProdutoDTO result = alertaProdutoService.save(alertaProdutoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alertaProdutoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /alerta-produtos/:id} : Partial updates given fields of an existing alertaProduto, field will ignore if it is null
     *
     * @param id the id of the alertaProdutoDTO to save.
     * @param alertaProdutoDTO the alertaProdutoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alertaProdutoDTO,
     * or with status {@code 400 (Bad Request)} if the alertaProdutoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alertaProdutoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alertaProdutoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/alerta-produtos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlertaProdutoDTO> partialUpdateAlertaProduto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlertaProdutoDTO alertaProdutoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AlertaProduto partially : {}, {}", id, alertaProdutoDTO);
        if (alertaProdutoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alertaProdutoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alertaProdutoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlertaProdutoDTO> result = alertaProdutoService.partialUpdate(alertaProdutoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alertaProdutoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /alerta-produtos} : get all the alertaProdutos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alertaProdutos in body.
     */
    @GetMapping("/alerta-produtos")
    public List<AlertaProdutoDTO> getAllAlertaProdutos() {
        log.debug("REST request to get all AlertaProdutos");
        return alertaProdutoService.findAll();
    }

    /**
     * {@code GET  /alerta-produtos/:id} : get the "id" alertaProduto.
     *
     * @param id the id of the alertaProdutoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alertaProdutoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alerta-produtos/{id}")
    public ResponseEntity<AlertaProdutoDTO> getAlertaProduto(@PathVariable Long id) {
        log.debug("REST request to get AlertaProduto : {}", id);
        Optional<AlertaProdutoDTO> alertaProdutoDTO = alertaProdutoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alertaProdutoDTO);
    }

    /**
     * {@code DELETE  /alerta-produtos/:id} : delete the "id" alertaProduto.
     *
     * @param id the id of the alertaProdutoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alerta-produtos/{id}")
    public ResponseEntity<Void> deleteAlertaProduto(@PathVariable Long id) {
        log.debug("REST request to delete AlertaProduto : {}", id);
        alertaProdutoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
