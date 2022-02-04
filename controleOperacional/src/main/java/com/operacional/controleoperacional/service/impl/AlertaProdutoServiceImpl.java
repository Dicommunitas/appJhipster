package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.AlertaProduto;
import com.operacional.controleoperacional.repository.AlertaProdutoRepository;
import com.operacional.controleoperacional.service.AlertaProdutoService;
import com.operacional.controleoperacional.service.dto.AlertaProdutoDTO;
import com.operacional.controleoperacional.service.mapper.AlertaProdutoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AlertaProduto}.
 */
@Service
@Transactional
public class AlertaProdutoServiceImpl implements AlertaProdutoService {

    private final Logger log = LoggerFactory.getLogger(AlertaProdutoServiceImpl.class);

    private final AlertaProdutoRepository alertaProdutoRepository;

    private final AlertaProdutoMapper alertaProdutoMapper;

    public AlertaProdutoServiceImpl(AlertaProdutoRepository alertaProdutoRepository, AlertaProdutoMapper alertaProdutoMapper) {
        this.alertaProdutoRepository = alertaProdutoRepository;
        this.alertaProdutoMapper = alertaProdutoMapper;
    }

    @Override
    public AlertaProdutoDTO save(AlertaProdutoDTO alertaProdutoDTO) {
        log.debug("Request to save AlertaProduto : {}", alertaProdutoDTO);
        AlertaProduto alertaProduto = alertaProdutoMapper.toEntity(alertaProdutoDTO);
        alertaProduto = alertaProdutoRepository.save(alertaProduto);
        return alertaProdutoMapper.toDto(alertaProduto);
    }

    @Override
    public Optional<AlertaProdutoDTO> partialUpdate(AlertaProdutoDTO alertaProdutoDTO) {
        log.debug("Request to partially update AlertaProduto : {}", alertaProdutoDTO);

        return alertaProdutoRepository
            .findById(alertaProdutoDTO.getId())
            .map(existingAlertaProduto -> {
                alertaProdutoMapper.partialUpdate(existingAlertaProduto, alertaProdutoDTO);

                return existingAlertaProduto;
            })
            .map(alertaProdutoRepository::save)
            .map(alertaProdutoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaProdutoDTO> findAll() {
        log.debug("Request to get all AlertaProdutos");
        return alertaProdutoRepository.findAll().stream().map(alertaProdutoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlertaProdutoDTO> findOne(Long id) {
        log.debug("Request to get AlertaProduto : {}", id);
        return alertaProdutoRepository.findById(id).map(alertaProdutoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AlertaProduto : {}", id);
        alertaProdutoRepository.deleteById(id);
    }
}
