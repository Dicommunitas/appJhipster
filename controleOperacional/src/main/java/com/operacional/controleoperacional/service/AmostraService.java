package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.AmostraDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.Amostra}.
 */
public interface AmostraService {
    /**
     * Save a amostra.
     *
     * @param amostraDTO the entity to save.
     * @return the persisted entity.
     */
    AmostraDTO save(AmostraDTO amostraDTO);

    /**
     * Partially updates a amostra.
     *
     * @param amostraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AmostraDTO> partialUpdate(AmostraDTO amostraDTO);

    /**
     * Get all the amostras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AmostraDTO> findAll(Pageable pageable);

    /**
     * Get the "id" amostra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AmostraDTO> findOne(Long id);

    /**
     * Delete the "id" amostra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
