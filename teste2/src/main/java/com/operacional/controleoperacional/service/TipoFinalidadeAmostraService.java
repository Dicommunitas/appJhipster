package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.TipoFinalidadeAmostraDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.TipoFinalidadeAmostra}.
 */
public interface TipoFinalidadeAmostraService {
    /**
     * Save a tipoFinalidadeAmostra.
     *
     * @param tipoFinalidadeAmostraDTO the entity to save.
     * @return the persisted entity.
     */
    TipoFinalidadeAmostraDTO save(TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO);

    /**
     * Updates a tipoFinalidadeAmostra.
     *
     * @param tipoFinalidadeAmostraDTO the entity to update.
     * @return the persisted entity.
     */
    TipoFinalidadeAmostraDTO update(TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO);

    /**
     * Partially updates a tipoFinalidadeAmostra.
     *
     * @param tipoFinalidadeAmostraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoFinalidadeAmostraDTO> partialUpdate(TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO);

    /**
     * Get all the tipoFinalidadeAmostras.
     *
     * @return the list of entities.
     */
    List<TipoFinalidadeAmostraDTO> findAll();

    /**
     * Get the "id" tipoFinalidadeAmostra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoFinalidadeAmostraDTO> findOne(Long id);

    /**
     * Delete the "id" tipoFinalidadeAmostra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
