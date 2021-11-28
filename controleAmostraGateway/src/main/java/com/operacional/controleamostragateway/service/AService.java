package com.operacional.controleamostragateway.service;

import com.operacional.controleamostragateway.service.dto.ADTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.operacional.controleamostragateway.domain.A}.
 */
public interface AService {
    /**
     * Save a a.
     *
     * @param aDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ADTO> save(ADTO aDTO);

    /**
     * Partially updates a a.
     *
     * @param aDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ADTO> partialUpdate(ADTO aDTO);

    /**
     * Get all the aS.
     *
     * @return the list of entities.
     */
    Flux<ADTO> findAll();

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
    Mono<ADTO> findOne(Long id);

    /**
     * Delete the "id" a.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
