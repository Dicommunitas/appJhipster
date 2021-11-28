package com.operacional.controleamostragateway.service;

import com.operacional.controleamostragateway.domain.B;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    Mono<B> save(B b);

    /**
     * Partially updates a b.
     *
     * @param b the entity to update partially.
     * @return the persisted entity.
     */
    Mono<B> partialUpdate(B b);

    /**
     * Get all the bS.
     *
     * @return the list of entities.
     */
    Flux<B> findAll();

    /**
     * Returns the number of bS available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" b.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<B> findOne(Long id);

    /**
     * Delete the "id" b.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
