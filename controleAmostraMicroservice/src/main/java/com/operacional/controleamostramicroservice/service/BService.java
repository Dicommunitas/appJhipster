package com.operacional.controleamostramicroservice.service;

import com.operacional.controleamostramicroservice.domain.B;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link B}.
 */
public interface BService {
    /**
     * Save a b.
     *
     * @param b the entity to save.
     * @return the persisted entity.
     */
    B save(B b);

    /**
     * Partially updates a b.
     *
     * @param b the entity to update partially.
     * @return the persisted entity.
     */
    Optional<B> partialUpdate(B b);

    /**
     * Get all the bS.
     *
     * @return the list of entities.
     */
    List<B> findAll();

    /**
     * Get the "id" b.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<B> findOne(Long id);

    /**
     * Delete the "id" b.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
