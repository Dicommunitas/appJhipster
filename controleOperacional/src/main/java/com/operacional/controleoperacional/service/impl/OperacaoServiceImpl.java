package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.Operacao;
import com.operacional.controleoperacional.repository.OperacaoRepository;
import com.operacional.controleoperacional.service.OperacaoService;
import com.operacional.controleoperacional.service.dto.OperacaoDTO;
import com.operacional.controleoperacional.service.mapper.OperacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Operacao}.
 */
@Service
@Transactional
public class OperacaoServiceImpl implements OperacaoService {

    private final Logger log = LoggerFactory.getLogger(OperacaoServiceImpl.class);

    private final OperacaoRepository operacaoRepository;

    private final OperacaoMapper operacaoMapper;

    public OperacaoServiceImpl(OperacaoRepository operacaoRepository, OperacaoMapper operacaoMapper) {
        this.operacaoRepository = operacaoRepository;
        this.operacaoMapper = operacaoMapper;
    }

    @Override
    public OperacaoDTO save(OperacaoDTO operacaoDTO) {
        log.debug("Request to save Operacao : {}", operacaoDTO);
        Operacao operacao = operacaoMapper.toEntity(operacaoDTO);
        operacao = operacaoRepository.save(operacao);
        return operacaoMapper.toDto(operacao);
    }

    @Override
    public Optional<OperacaoDTO> partialUpdate(OperacaoDTO operacaoDTO) {
        log.debug("Request to partially update Operacao : {}", operacaoDTO);

        return operacaoRepository
            .findById(operacaoDTO.getId())
            .map(existingOperacao -> {
                operacaoMapper.partialUpdate(existingOperacao, operacaoDTO);

                return existingOperacao;
            })
            .map(operacaoRepository::save)
            .map(operacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OperacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Operacaos");
        return operacaoRepository.findAll(pageable).map(operacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OperacaoDTO> findOne(Long id) {
        log.debug("Request to get Operacao : {}", id);
        return operacaoRepository.findById(id).map(operacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Operacao : {}", id);
        operacaoRepository.deleteById(id);
    }
}
