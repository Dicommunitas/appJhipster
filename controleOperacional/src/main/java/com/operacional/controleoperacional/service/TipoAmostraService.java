package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.TipoAmostraDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.TipoAmostra}.
 */
public interface TipoAmostraService {
    /**
     * Save a tipoAmostra.
     *
     * @param tipoAmostraDTO the entity to save.
     * @return the persisted entity.
     */
    TipoAmostraDTO save(TipoAmostraDTO tipoAmostraDTO);

    /**
     * Partially updates a tipoAmostra.
     *
     * @param tipoAmostraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoAmostraDTO> partialUpdate(TipoAmostraDTO tipoAmostraDTO);

    /**
     * Get all the tipoAmostras.
     *
     * @return the list of entities.
     */
    List<TipoAmostraDTO> findAll();

    /**
     * Get the "id" tipoAmostra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoAmostraDTO> findOne(Long id);

    /**
     * Delete the "id" tipoAmostra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
