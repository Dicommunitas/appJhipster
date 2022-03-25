package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.FinalidadeAmostraDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.FinalidadeAmostra}.
 */
public interface FinalidadeAmostraService {
    /**
     * Save a finalidadeAmostra.
     *
     * @param finalidadeAmostraDTO the entity to save.
     * @return the persisted entity.
     */
    FinalidadeAmostraDTO save(FinalidadeAmostraDTO finalidadeAmostraDTO);

    /**
     * Partially updates a finalidadeAmostra.
     *
     * @param finalidadeAmostraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FinalidadeAmostraDTO> partialUpdate(FinalidadeAmostraDTO finalidadeAmostraDTO);

    /**
     * Get all the finalidadeAmostras.
     *
     * @return the list of entities.
     */
    List<FinalidadeAmostraDTO> findAll();

    /**
     * Get the "id" finalidadeAmostra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FinalidadeAmostraDTO> findOne(Long id);

    /**
     * Delete the "id" finalidadeAmostra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
