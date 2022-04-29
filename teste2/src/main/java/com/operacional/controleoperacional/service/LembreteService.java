package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.LembreteDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.Lembrete}.
 */
public interface LembreteService {
    /**
     * Save a lembrete.
     *
     * @param lembreteDTO the entity to save.
     * @return the persisted entity.
     */
    LembreteDTO save(LembreteDTO lembreteDTO);

    /**
     * Updates a lembrete.
     *
     * @param lembreteDTO the entity to update.
     * @return the persisted entity.
     */
    LembreteDTO update(LembreteDTO lembreteDTO);

    /**
     * Partially updates a lembrete.
     *
     * @param lembreteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LembreteDTO> partialUpdate(LembreteDTO lembreteDTO);

    /**
     * Get all the lembretes.
     *
     * @return the list of entities.
     */
    List<LembreteDTO> findAll();

    /**
     * Get all the lembretes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LembreteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" lembrete.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LembreteDTO> findOne(Long id);

    /**
     * Delete the "id" lembrete.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
