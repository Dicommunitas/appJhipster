package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.domain.C;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    Mono<C> save(C c);

    /**
     * Partially updates a c.
     *
     * @param c the entity to update partially.
     * @return the persisted entity.
     */
    Mono<C> partialUpdate(C c);

    /**
     * Get all the cS.
     *
     * @return the list of entities.
     */
    Flux<C> findAll();

    /**
     * Returns the number of cS available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" c.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<C> findOne(Long id);

    /**
     * Delete the "id" c.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
