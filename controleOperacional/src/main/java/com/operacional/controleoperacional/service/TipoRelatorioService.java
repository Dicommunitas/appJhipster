package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.TipoRelatorioDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.TipoRelatorio}.
 */
public interface TipoRelatorioService {
    /**
     * Save a tipoRelatorio.
     *
     * @param tipoRelatorioDTO the entity to save.
     * @return the persisted entity.
     */
    TipoRelatorioDTO save(TipoRelatorioDTO tipoRelatorioDTO);

    /**
     * Partially updates a tipoRelatorio.
     *
     * @param tipoRelatorioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoRelatorioDTO> partialUpdate(TipoRelatorioDTO tipoRelatorioDTO);

    /**
     * Get all the tipoRelatorios.
     *
     * @return the list of entities.
     */
    List<TipoRelatorioDTO> findAll();

    /**
     * Get all the tipoRelatorios with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoRelatorioDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" tipoRelatorio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoRelatorioDTO> findOne(Long id);

    /**
     * Delete the "id" tipoRelatorio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
