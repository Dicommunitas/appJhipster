package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.domain.D;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link D}.
 */
public interface DService {
    /**
     * Save a d.
     *
     * @param d the entity to save.
     * @return the persisted entity.
     */
    Mono<D> save(D d);

    /**
     * Partially updates a d.
     *
     * @param d the entity to update partially.
     * @return the persisted entity.
     */
    Mono<D> partialUpdate(D d);

    /**
     * Get all the dS.
     *
     * @return the list of entities.
     */
    Flux<D> findAll();

    /**
     * Returns the number of dS available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" d.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<D> findOne(Long id);

    /**
     * Delete the "id" d.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
