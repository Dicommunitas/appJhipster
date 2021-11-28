package com.operacional.myapp2.service;

import com.operacional.myapp2.domain.C;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link C}.
 */
public interface CService {
    /**
     * Save a c.
     *
     * @param c the entity to save.
     * @return the persisted entity.
     */
    C save(C c);

    /**
     * Partially updates a c.
     *
     * @param c the entity to update partially.
     * @return the persisted entity.
     */
    Optional<C> partialUpdate(C c);

    /**
     * Get all the cS.
     *
     * @return the list of entities.
     */
    List<C> findAll();

    /**
     * Get the "id" c.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<C> findOne(Long id);

    /**
     * Delete the "id" c.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
