package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.service.dto.AlertaProdutoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.operacional.controleoperacional.domain.AlertaProduto}.
 */
public interface AlertaProdutoService {
    /**
     * Save a alertaProduto.
     *
     * @param alertaProdutoDTO the entity to save.
     * @return the persisted entity.
     */
    AlertaProdutoDTO save(AlertaProdutoDTO alertaProdutoDTO);

    /**
     * Updates a alertaProduto.
     *
     * @param alertaProdutoDTO the entity to update.
     * @return the persisted entity.
     */
    AlertaProdutoDTO update(AlertaProdutoDTO alertaProdutoDTO);

    /**
     * Partially updates a alertaProduto.
     *
     * @param alertaProdutoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlertaProdutoDTO> partialUpdate(AlertaProdutoDTO alertaProdutoDTO);

    /**
     * Get all the alertaProdutos.
     *
     * @return the list of entities.
     */
    List<AlertaProdutoDTO> findAll();

    /**
     * Get the "id" alertaProduto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlertaProdutoDTO> findOne(Long id);

    /**
     * Delete the "id" alertaProduto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
