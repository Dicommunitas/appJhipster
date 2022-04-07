package com.operacional.controleoperacional.web.rest;

import com.operacional.controleoperacional.repository.TipoOperacaoRepository;
import com.operacional.controleoperacional.service.TipoOperacaoService;
import com.operacional.controleoperacional.service.dto.TipoOperacaoDTO;
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
 * REST controller for managing {@link com.operacional.controleoperacional.domain.TipoOperacao}.
 */
@RestController
@RequestMapping("/api")
public class TipoOperacaoResource {

    private final Logger log = LoggerFactory.getLogger(TipoOperacaoResource.class);

    private static final String ENTITY_NAME = "tipoOperacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoOperacaoService tipoOperacaoService;

    private final TipoOperacaoRepository tipoOperacaoRepository;

    public TipoOperacaoResource(TipoOperacaoService tipoOperacaoService, TipoOperacaoRepository tipoOperacaoRepository) {
        this.tipoOperacaoService = tipoOperacaoService;
        this.tipoOperacaoRepository = tipoOperacaoRepository;
    }

    /**
     * {@code POST  /tipo-operacaos} : Create a new tipoOperacao.
     *
     * @param tipoOperacaoDTO the tipoOperacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoOperacaoDTO, or with status {@code 400 (Bad Request)} if the tipoOperacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-operacaos")
    public ResponseEntity<TipoOperacaoDTO> createTipoOperacao(@Valid @RequestBody TipoOperacaoDTO tipoOperacaoDTO)
        throws URISyntaxException {
        log.debug("REST request to save TipoOperacao : {}", tipoOperacaoDTO);
        if (tipoOperacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoOperacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoOperacaoDTO result = tipoOperacaoService.save(tipoOperacaoDTO);
        return ResponseEntity
            .created(new URI("/api/tipo-operacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-operacaos/:id} : Updates an existing tipoOperacao.
     *
     * @param id the id of the tipoOperacaoDTO to save.
     * @param tipoOperacaoDTO the tipoOperacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoOperacaoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoOperacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoOperacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-operacaos/{id}")
    public ResponseEntity<TipoOperacaoDTO> updateTipoOperacao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoOperacaoDTO tipoOperacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoOperacao : {}, {}", id, tipoOperacaoDTO);
        if (tipoOperacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoOperacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoOperacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoOperacaoDTO result = tipoOperacaoService.update(tipoOperacaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoOperacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-operacaos/:id} : Partial updates given fields of an existing tipoOperacao, field will ignore if it is null
     *
     * @param id the id of the tipoOperacaoDTO to save.
     * @param tipoOperacaoDTO the tipoOperacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoOperacaoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoOperacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoOperacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoOperacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-operacaos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoOperacaoDTO> partialUpdateTipoOperacao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoOperacaoDTO tipoOperacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoOperacao partially : {}, {}", id, tipoOperacaoDTO);
        if (tipoOperacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoOperacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoOperacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoOperacaoDTO> result = tipoOperacaoService.partialUpdate(tipoOperacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoOperacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-operacaos} : get all the tipoOperacaos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoOperacaos in body.
     */
    @GetMapping("/tipo-operacaos")
    public List<TipoOperacaoDTO> getAllTipoOperacaos() {
        log.debug("REST request to get all TipoOperacaos");
        return tipoOperacaoService.findAll();
    }

    /**
     * {@code GET  /tipo-operacaos/:id} : get the "id" tipoOperacao.
     *
     * @param id the id of the tipoOperacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoOperacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-operacaos/{id}")
    public ResponseEntity<TipoOperacaoDTO> getTipoOperacao(@PathVariable Long id) {
        log.debug("REST request to get TipoOperacao : {}", id);
        Optional<TipoOperacaoDTO> tipoOperacaoDTO = tipoOperacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoOperacaoDTO);
    }

    /**
     * {@code DELETE  /tipo-operacaos/:id} : delete the "id" tipoOperacao.
     *
     * @param id the id of the tipoOperacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-operacaos/{id}")
    public ResponseEntity<Void> deleteTipoOperacao(@PathVariable Long id) {
        log.debug("REST request to delete TipoOperacao : {}", id);
        tipoOperacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
