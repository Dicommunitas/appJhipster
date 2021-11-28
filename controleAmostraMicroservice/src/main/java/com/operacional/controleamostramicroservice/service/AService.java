package com.operacional.controleamostramicroservice.service;

import com.operacional.controleamostramicroservice.service.dto.ADTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.operacional.controleamostramicroservice.domain.A}.
 */
public interface AService {
    /**
     * Save a a.
     *
     * @param aDTO the entity to save.
     * @return the persisted entity.
     */
    ADTO save(ADTO aDTO);

    /**
     * Partially updates a a.
     *
     * @param aDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ADTO> partialUpdate(ADTO aDTO);

    /**
     * Get all the aS.
     *
     * @return the list of entities.
     */
    List<ADTO> findAll();

    /**
     * Get the "id" a.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ADTO> findOne(Long id);

    /**
     * Delete the "id" a.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
