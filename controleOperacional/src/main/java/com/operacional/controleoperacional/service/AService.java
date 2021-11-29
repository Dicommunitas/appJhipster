package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.domain.A;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link A}.
 */
public interface AService {
    /**
     * Save a a.
     *
     * @param a the entity to save.
     * @return the persisted entity.
     */
    Mono<A> save(A a);

    /**
     * Partially updates a a.
     *
     * @param a the entity to update partially.
     * @return the persisted entity.
     */
    Mono<A> partialUpdate(A a);

    /**
     * Get all the aS.
     *
     * @return the list of entities.
     */
    Flux<A> findAll();

    /**
     * Returns the number of aS available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" a.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<A> findOne(Long id);

    /**
     * Delete the "id" a.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
