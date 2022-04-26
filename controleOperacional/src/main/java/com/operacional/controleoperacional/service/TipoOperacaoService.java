package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.TipoOperacaoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.TipoOperacao}.
 */
public interface TipoOperacaoService {
    /**
     * Save a tipoOperacao.
     *
     * @param tipoOperacaoDTO the entity to save.
     * @return the persisted entity.
     */
    TipoOperacaoDTO save(TipoOperacaoDTO tipoOperacaoDTO);

    /**
     * Updates a tipoOperacao.
     *
     * @param tipoOperacaoDTO the entity to update.
     * @return the persisted entity.
     */
    TipoOperacaoDTO update(TipoOperacaoDTO tipoOperacaoDTO);

    /**
     * Partially updates a tipoOperacao.
     *
     * @param tipoOperacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoOperacaoDTO> partialUpdate(TipoOperacaoDTO tipoOperacaoDTO);

    /**
     * Get all the tipoOperacaos.
     *
     * @return the list of entities.
     */
    List<TipoOperacaoDTO> findAll();

    /**
     * Get the "id" tipoOperacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoOperacaoDTO> findOne(Long id);

    /**
     * Delete the "id" tipoOperacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
