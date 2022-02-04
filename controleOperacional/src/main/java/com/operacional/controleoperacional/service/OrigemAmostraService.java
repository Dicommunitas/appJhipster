package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.OrigemAmostraDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.OrigemAmostra}.
 */
public interface OrigemAmostraService {
    /**
     * Save a origemAmostra.
     *
     * @param origemAmostraDTO the entity to save.
     * @return the persisted entity.
     */
    OrigemAmostraDTO save(OrigemAmostraDTO origemAmostraDTO);

    /**
     * Partially updates a origemAmostra.
     *
     * @param origemAmostraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrigemAmostraDTO> partialUpdate(OrigemAmostraDTO origemAmostraDTO);

    /**
     * Get all the origemAmostras.
     *
     * @return the list of entities.
     */
    List<OrigemAmostraDTO> findAll();

    /**
     * Get the "id" origemAmostra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrigemAmostraDTO> findOne(Long id);

    /**
     * Delete the "id" origemAmostra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
