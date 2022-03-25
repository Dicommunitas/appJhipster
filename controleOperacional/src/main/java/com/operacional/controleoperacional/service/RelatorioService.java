package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.RelatorioDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.Relatorio}.
 */
public interface RelatorioService {
    /**
     * Save a relatorio.
     *
     * @param relatorioDTO the entity to save.
     * @return the persisted entity.
     */
    RelatorioDTO save(RelatorioDTO relatorioDTO);

    /**
     * Partially updates a relatorio.
     *
     * @param relatorioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RelatorioDTO> partialUpdate(RelatorioDTO relatorioDTO);

    /**
     * Get all the relatorios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RelatorioDTO> findAll(Pageable pageable);

    /**
     * Get all the relatorios with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RelatorioDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" relatorio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RelatorioDTO> findOne(Long id);

    /**
     * Delete the "id" relatorio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
