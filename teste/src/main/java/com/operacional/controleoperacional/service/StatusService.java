package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.StatusDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.Status}.
 */
public interface StatusService {
    /**
     * Save a status.
     *
     * @param statusDTO the entity to save.
     * @return the persisted entity.
     */
    StatusDTO save(StatusDTO statusDTO);

    /**
     * Updates a status.
     *
     * @param statusDTO the entity to update.
     * @return the persisted entity.
     */
    StatusDTO update(StatusDTO statusDTO);

    /**
     * Partially updates a status.
     *
     * @param statusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StatusDTO> partialUpdate(StatusDTO statusDTO);

    /**
     * Get all the statuses.
     *
     * @return the list of entities.
     */
    List<StatusDTO> findAll();

    /**
     * Get all the statuses with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StatusDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" status.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StatusDTO> findOne(Long id);

    /**
     * Delete the "id" status.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
