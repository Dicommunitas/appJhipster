package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.ProblemaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.Problema}.
 */
public interface ProblemaService {
    /**
     * Save a problema.
     *
     * @param problemaDTO the entity to save.
     * @return the persisted entity.
     */
    ProblemaDTO save(ProblemaDTO problemaDTO);

    /**
     * Partially updates a problema.
     *
     * @param problemaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProblemaDTO> partialUpdate(ProblemaDTO problemaDTO);

    /**
     * Get all the problemas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProblemaDTO> findAll(Pageable pageable);

    /**
     * Get all the problemas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProblemaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" problema.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProblemaDTO> findOne(Long id);

    /**
     * Delete the "id" problema.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
