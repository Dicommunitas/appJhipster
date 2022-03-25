package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.OperacaoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.Operacao}.
 */
public interface OperacaoService {
    /**
     * Save a operacao.
     *
     * @param operacaoDTO the entity to save.
     * @return the persisted entity.
     */
    OperacaoDTO save(OperacaoDTO operacaoDTO);

    /**
     * Partially updates a operacao.
     *
     * @param operacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OperacaoDTO> partialUpdate(OperacaoDTO operacaoDTO);

    /**
     * Get all the operacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OperacaoDTO> findAll(Pageable pageable);

    /**
     * Get all the operacaos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OperacaoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" operacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OperacaoDTO> findOne(Long id);

    /**
     * Delete the "id" operacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
